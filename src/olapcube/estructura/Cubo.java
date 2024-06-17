package olapcube.estructura;

import java.util.*;
import olapcube.Proyeccion;
import olapcube.configuration.ConfigCubo;
import olapcube.configuration.ConfigDimension;
import olapcube.excepciones.DimensionNotFoundException;
import olapcube.excepciones.MedidaNotFoundException;
import olapcube.metricas.*;

public class Cubo {
    private Map<String, Dimension> dimensiones;
    private Map<String, Medida> medidas;
    private List<Celda> celdas;
    private List<String> nombresHechos;
    private ConfigCubo config;

    private Cubo(ConfigCubo config) {
        this.config = config;
        this.dimensiones = new HashMap<>();
        this.celdas = new ArrayList<>();
        this.nombresHechos = new ArrayList<>();
        inicializarMedidas();
    }

    public static Cubo crearFromConfig(ConfigCubo config) {
        Cubo cubo = new Cubo(config);
        cubo.inicializarDimensiones(config);
        cubo.inicializarHechos(config);
        return cubo;
    }

    private void inicializarMedidas() {
        this.medidas = Map.of(
                "suma", new Suma(),
                "count", new Count(),
                "media", new Media(),
                "maximo", new Maximo(),
                "minimo", new Minimo());
    }

    private void inicializarDimensiones(ConfigCubo config) {
        for (ConfigDimension configDimension : config.getDimensiones()) {
            agregarDimension(Dimension.crear(configDimension));
        }
    }

    private void inicializarHechos(ConfigCubo config) {
        this.nombresHechos = List.of(config.getHechos().getNombresHechos());
        int indiceCelda = 0;
        for (String[] datos : config.getHechos().getDatasetReader().read()) {
            Celda celda = crearCelda(datos);
            agregarCelda(celda);
            asociarCeldaConDimensiones(datos, indiceCelda);
            indiceCelda++;
        }
    }

    private Celda crearCelda(String[] datos) {
        Celda celda = new Celda();
        for (String hecho : nombresHechos) {
            int columnaHecho = config.getHechos().getColumnaHecho(hecho);
            celda.agregarHecho(hecho, Double.parseDouble(datos[columnaHecho]));
        }
        return celda;
    }

    private void asociarCeldaConDimensiones(String[] datos, int indiceCelda) {
        for (Dimension dimension : dimensiones.values()) {
            int columnaFkHechos = dimension.getColumnaFkHechos();
            int fk = Integer.parseInt(datos[columnaFkHechos]);
            dimension.agregarHecho(fk, indiceCelda);
        }
    }

    private void copiarDimensiones(Cubo cubo) {
        for (Dimension dimension : dimensiones.values()) {
            cubo.agregarDimension(dimension.copiar());
        }
    }

    private Set<Integer> filtrarCeldas(String nombreDimension, String valor) {
        Dimension dimension = getDimension(nombreDimension);
        return dimension.getIndicesCeldas(valor);
    }

    private Set<Integer> filtrarCeldas(Map<String, Set<String>> condiciones) {
        Set<Integer> filteredIndices = null;
        for (Map.Entry<String, Set<String>> condicion : condiciones.entrySet()) {
            Set<Integer> dimensionFilteredIndices = obtenerIndicesFiltradosPorDimension(condicion.getKey(),
                    condicion.getValue());
            if (filteredIndices == null) {
                filteredIndices = new HashSet<>(dimensionFilteredIndices);
            } else {
                filteredIndices.retainAll(dimensionFilteredIndices);
            }
        }
        return filteredIndices != null ? filteredIndices : new HashSet<>();
    }

    private Set<Integer> obtenerIndicesFiltradosPorDimension(String dimensionNombre, Set<String> valores) {
        Dimension dimension = getDimension(dimensionNombre);
        Set<Integer> dimensionFilteredIndices = new HashSet<>();
        for (String valor : valores) {
            dimensionFilteredIndices.addAll(dimension.getIndicesCeldas(valor));
        }
        return dimensionFilteredIndices;
    }

    private void agregarCeldasFiltradas(Cubo cubo, Set<Integer> filteredCellIndices) {
        Map<Integer, Integer> oldToNewIndexMap = new HashMap<>();
        int newIndex = 0;
        for (Integer oldIndex : filteredCellIndices) {
            cubo.celdas.add(this.celdas.get(oldIndex));
            oldToNewIndexMap.put(oldIndex, newIndex);
            newIndex++;
        }
        actualizarDimensiones(cubo, oldToNewIndexMap);
    }

    private void actualizarDimensiones(Cubo cubo, Map<Integer, Integer> oldToNewIndexMap) {
        for (Dimension dim : cubo.dimensiones.values()) {
            dim.actualizarIndices(oldToNewIndexMap);
        }
    }

    public Cubo rollup(String nombreDimension) {
        // Clonar la configuración actual
        ConfigCubo nuevaConfig = config;

        // Reducir la granularidad de la dimensión seleccionada
        for (ConfigDimension dimensionConfig : nuevaConfig.getDimensiones()) {
            if (dimensionConfig.getNombre().equals(nombreDimension)) {
                if (dimensionConfig.getColumnaValor() == getDimension(nombreDimension).length()) {
                    System.out.println(dimensionConfig.getColumnaValor());
                    System.out.println(getDimension(nombreDimension).length());
                    break;
                } else {
                    dimensionConfig.setColumnaValor(dimensionConfig.getColumnaValor() + 1);
                    break;
                }
            }
        }

        // Crear un nuevo cubo con la configuración modificada
        return Cubo.crearFromConfig(nuevaConfig);
    }

    public Cubo drilldown(String nombreDimension) {
        // Clonar la configuración actual
        ConfigCubo nuevaConfig = config;

        // Aumentar la granularidad de la dimensión seleccionada
        for (ConfigDimension dimensionConfig : nuevaConfig.getDimensiones()) {
            if (dimensionConfig.getNombre().equals(nombreDimension)) {
                if (dimensionConfig.getColumnaValor() == 1) {
                    break;
                } else {
                    dimensionConfig.setColumnaValor(dimensionConfig.getColumnaValor() - 1);
                    break;
                }
            }
        }

        // Crear un nuevo cubo con la configuración modificada
        return Cubo.crearFromConfig(nuevaConfig);
    }

    public Cubo slice(String nombreDimension, String valor) {
        Cubo cubo = new Cubo(this.config);
        copiarDimensiones(cubo);
        Set<Integer> filteredCellIndices = filtrarCeldas(nombreDimension, valor);
        agregarCeldasFiltradas(cubo, filteredCellIndices);
        cubo.nombresHechos = new ArrayList<>(this.nombresHechos);
        return cubo;
    }

    public Cubo dice(Map<String, Set<String>> condiciones) {
        Cubo cubo = new Cubo(this.config);
        copiarDimensiones(cubo);
        Set<Integer> filteredCellIndices = filtrarCeldas(condiciones);
        agregarCeldasFiltradas(cubo, filteredCellIndices);
        cubo.nombresHechos = new ArrayList<>(this.nombresHechos);
        return cubo;
    }

    public List<String> getNombresHechos() {
        return nombresHechos;
    }

    public List<String> getNombresDimensiones() {
        return new ArrayList<>(dimensiones.keySet());
    }

    public List<String> getMedidas() {
        return new ArrayList<>(medidas.keySet());
    }

    public Medida getMedida(String nombre) {
        if (!medidas.containsKey(nombre)) {
            throw new MedidaNotFoundException("La medida " + nombre + " no existe.");
        }
        return medidas.get(nombre);
    }

    public Dimension getDimension(String nombre) {
        if (!dimensiones.containsKey(nombre)) {
            throw new DimensionNotFoundException("La dimensión " + nombre + " no existe.");
        }
        return dimensiones.get(nombre);
    }

    public void agregarDimension(Dimension dimension) {
        dimensiones.put(dimension.getNombre(), dimension);
    }

    public void agregarCelda(Celda celda) {
        celdas.add(celda);
    }

    public Celda getCelda(Dimension dimension, String valor) {
        return Celda.agrupar(celdasFromIndices(dimension.getIndicesCeldas(valor)));
    }

    public Celda getCelda(Dimension dim1, String valor1, Dimension dim2, String valor2) {
        Set<Integer> indicesComunes = celdasComunes(dim1.getIndicesCeldas(valor1), dim2.getIndicesCeldas(valor2));
        return Celda.agrupar(celdasFromIndices(indicesComunes));
    }

    private List<Celda> celdasFromIndices(Set<Integer> indices) {
        List<Celda> celdas = new ArrayList<>();
        for (Integer indice : indices) {
            celdas.add(this.celdas.get(indice));
        }
        return celdas;
    }

    private static Set<Integer> celdasComunes(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> nuevo = new HashSet<>(set1);
        nuevo.retainAll(set2);
        return nuevo;
    }

    @Override
    public String toString() {
        return String.format("Cubo [celdas=%d, dimensiones=%s, medidas=%d]", celdas.size(), dimensiones.keySet(),
                medidas.size());
    }

    public Proyeccion proyectar() {
        return new Proyeccion(this);
    }
}
