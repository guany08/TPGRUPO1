package olapcube.estructura;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import olapcube.configuration.ConfigDimension;
import olapcube.excepciones.ValueNotFoundException;

public class Dimension {
    private final String nombre;
    private final Map<String, Set<Integer>> valoresToCeldas;
    private final Map<Integer, String> idToValores;
    private int columnaFkHechos;

    public Dimension(String nombre) {
        this.nombre = nombre;
        this.valoresToCeldas = new HashMap<>();
        this.idToValores = new HashMap<>();
    }

    public static Dimension crear(ConfigDimension configDimension) {
        Dimension dimension = new Dimension(configDimension.getNombre());
        dimension.columnaFkHechos = configDimension.getColumnaFkHechos();
        for (String[] datos : configDimension.getDatasetReader().read()) {
            int pkDimension = Integer.parseInt(datos[configDimension.getColumnaKey()]);
            String valor = datos[configDimension.getColumnaValor()];
            dimension.idToValores.put(pkDimension, valor);
            dimension.valoresToCeldas.putIfAbsent(valor, new HashSet<>());
        }
        return dimension;
    }

    public int length() {
        return getValores().length + 1;
    }

    public Dimension copiar() {
        Dimension newDimension = new Dimension(this.nombre);
        newDimension.setColumnaFkHechos(this.columnaFkHechos);
        newDimension.setIdToValores(new HashMap<>(this.idToValores));
        newDimension.setValoresToCeldas(new HashMap<>(this.valoresToCeldas));
        return newDimension;
    }

    public void setColumnaFkHechos(int columnaFkHechos) {
        this.columnaFkHechos = columnaFkHechos;
    }

    public void setIdToValores(Map<Integer, String> idToValores) {
        this.idToValores.putAll(idToValores);
    }

    public void setValoresToCeldas(Map<String, Set<Integer>> valoresToCeldas) {
        this.valoresToCeldas.putAll(valoresToCeldas);
    }

    public Map<Integer, String> getIdToValores() {
        return idToValores;
    }

    public Map<String, Set<Integer>> getValoresToCeldas() {
        return valoresToCeldas;
    }

    public String[] getValores() {
        return valoresToCeldas.keySet().toArray(new String[0]);
    }

    public Set<Integer> getIndicesCeldas(String valor) {
        if (!valoresToCeldas.containsKey(valor)) {
            throw new ValueNotFoundException("El valor " + valor + " no existe en la dimensión.");
        }
        return valoresToCeldas.get(valor);
    }

    public String getNombre() {
        return nombre;
    }

    public int getColumnaFkHechos() {
        return columnaFkHechos;
    }

    public void agregarHecho(int idValor, int indiceCelda) {
        if (!idToValores.containsKey(idValor)) {
            throw new IllegalArgumentException("El id " + idValor + " del valor no existe en la dimension " + nombre);
        }
        valoresToCeldas.get(idToValores.get(idValor)).add(indiceCelda);
    }

    public void actualizarIndices(Map<Integer, Integer> oldToNewIndexMap) {
        for (Map.Entry<String, Set<Integer>> entry : valoresToCeldas.entrySet()) {
            Set<Integer> newIndices = new HashSet<>();
            for (Integer oldIndex : entry.getValue()) {
                if (oldToNewIndexMap.containsKey(oldIndex)) {
                    newIndices.add(oldToNewIndexMap.get(oldIndex));
                }
            }
            entry.setValue(newIndices);
        }
    }

    public void filtrarValores(Set<Integer> filteredCellIndices) {
        for (Map.Entry<String, Set<Integer>> entry : valoresToCeldas.entrySet()) {
            entry.getValue().retainAll(filteredCellIndices);
        }
        valoresToCeldas.values().removeIf(Set::isEmpty);
    }
}
