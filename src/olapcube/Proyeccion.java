package olapcube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import olapcube.estructura.Celda;
import olapcube.estructura.Cubo;
import olapcube.estructura.Dimension;
import olapcube.excepciones.HechoNotFoundException;

/**
 * Clase que representa una proyección de un cubo OLAP
 */
public class Proyeccion {
    private Cubo cubo; // Cubo sobre el que se realiza la proyección
    private int maxFilas = 10; // Máximo de filas a mostrar
    private int maxColumnas = 10; // Máximo de columnas a mostrar
    private String hecho; // Hecho a proyectar
    private String medida; // Medida a proyectar
    private List<String> hechos_del_cubo; // Hechos del cubo

    // Atributos para mostrar en consola
    private String formatoCelda = "%8.8s";
    private String separador = " | ";

    /**
     * Constructor de la clase
     * 
     * @param cubo Cubo sobre el que se realiza la proyección
     */
    public Proyeccion(Cubo cubo) {
        this.cubo = cubo;
        this.hechos_del_cubo = new ArrayList<>();
        this.hechos_del_cubo = cubo.getNombresHechos();
        this.hecho = cubo.getNombresHechos().get(0); // Selecciona el primer hecho por defecto
        this.medida = cubo.getMedidas().get(0); // Selecciona la primera medida por defecto
    }

    public void seleccionarHecho(String hecho) {
        if (!hechos_del_cubo.contains(hecho)) {
            throw new HechoNotFoundException("El hecho " + hecho + " no existe en el cubo.");
        }
        this.hecho = hecho;
    }

    public void seleccionarMedida(String medida) {
        this.medida = medida;
    }

    /**
     * Muestra la proyección de una dimensión
     * 
     * @param nombreDimension Nombre de la dimensión a proyectar
     */
    public void print(String nombreDimension) {
        Dimension dimension = cubo.getDimension(nombreDimension);
        System.out.println("Proyección de " + dimension.getNombre());

        String[] columnas = new String[] { hecho + " (" + medida + ")" };

        // Genera celdas de la proyección
        Double[][] valores = new Double[dimension.getValores().length][1];
        for (int i = 0; i < dimension.getValores().length; i++) {
            String valorDimension = dimension.getValores()[i];
            Celda celdaAgrupada = cubo.getCelda(dimension, valorDimension);
            valores[i][0] = cubo.getMedida(medida).calcular(celdaAgrupada.getValores(hecho));
        }

        // Filtrar valores cero
        String[] filteredValores = Arrays.stream(dimension.getValores())
            .filter(val -> {
                int index = Arrays.asList(dimension.getValores()).indexOf(val);
                return valores[index][0] != 0.0;
            }).toArray(String[]::new);

        Double[][] filteredResults = Arrays.stream(valores)
            .filter(row -> row[0] != 0.0)
            .toArray(Double[][]::new);

        // Muestra en consola
        printTablaConsola(filteredValores, columnas, filteredResults);
    }

    /**
     * Muestra la proyección de dos dimensiones
     * 
     * @param nombreDim1 Nombre de la primera dimensión (filas)
     * @param nombreDim2 Nombre de la segunda dimensión (columnas)
     */
    public void print(String nombreDim1, String nombreDim2) {
        Dimension dimension1 = cubo.getDimension(nombreDim1);
        Dimension dimension2 = cubo.getDimension(nombreDim2);
        System.out.println("Proyección de " + dimension1.getNombre() + " vs " + dimension2.getNombre() + " - " + hecho
                + " (" + medida + ")");

        // Genera celdas de la proyección
        Double[][] valores = new Double[dimension1.getValores().length][dimension2.getValores().length];
        for (int i = 0; i < dimension1.getValores().length; i++) {
            String valorDim1 = dimension1.getValores()[i];
            for (int j = 0; j < dimension2.getValores().length; j++) {
                String valorDim2 = dimension2.getValores()[j];
                Celda celdaAgrupada = cubo.getCelda(dimension1, valorDim1, dimension2, valorDim2);
                valores[i][j] = cubo.getMedida(medida).calcular(celdaAgrupada.getValores(hecho));
            }
        }

        // Filtrar filas y columnas con todos valores cero
        List<String> filteredRows = new ArrayList<>();
        List<String> filteredCols = new ArrayList<>(Arrays.asList(dimension2.getValores()));

        Double[][] filteredValues = Arrays.stream(valores)
            .filter(row -> {
                boolean hasNonZero = Arrays.stream(row).anyMatch(value -> value != 0.0);
                if (hasNonZero) {
                    int index = Arrays.asList(valores).indexOf(row);
                    filteredRows.add(dimension1.getValores()[index]);
                }
                return hasNonZero;
            }).toArray(Double[][]::new);

        List<Integer> validColIndexes = new ArrayList<>();
        for (int colIndex = 0; colIndex < dimension2.getValores().length; colIndex++) {
            boolean hasNonZero = false;
            for (Double[] row : filteredValues) {
                if (row[colIndex] != 0.0) {
                    hasNonZero = true;
                    break;
                }
            }
            if (hasNonZero) {
                validColIndexes.add(colIndex);
            }
        }

        String[] finalFilteredCols = validColIndexes.stream()
                .map(index -> dimension2.getValores()[index])
                .toArray(String[]::new);

        Double[][] finalValues = new Double[filteredValues.length][validColIndexes.size()];
        for (int i = 0; i < filteredValues.length; i++) {
            int colIndex = 0;
            for (int j : validColIndexes) {
                finalValues[i][colIndex++] = filteredValues[i][j];
            }
        }

        // Muestra en consola
        printTablaConsola(filteredRows.toArray(new String[0]), finalFilteredCols, finalValues);
    }

    /**
     * Muestra una tabla en consola
     * 
     * @param indice  Labels o valores de las filas
     * @param header  Labels o valores de las columnas
     * @param valores Valores de la tabla
     */
    private void printTablaConsola(String[] indice, String[] header, Double[][] valores) {
        if (indice.length > maxFilas) {
            indice = Arrays.copyOfRange(indice, 0, maxFilas);
        }
        if (header.length > maxColumnas) {
            header = Arrays.copyOfRange(header, 0, maxColumnas);
        }

        // Calcular el ancho máximo para cada columna
        int[] maxColWidths = new int[header.length + 1]; // +1 para la columna del índice
        maxColWidths[0] = Arrays.stream(indice).mapToInt(String::length).max().orElse(0);

        for (int i = 0; i < header.length; i++) {
            final int finalI = i;
            int maxHeaderWidth = header[i].length();
            int maxValueWidth = Arrays.stream(valores).mapToInt(row -> String.format("%.2f", row[finalI]).length())
                    .max().orElse(0);
            maxColWidths[i + 1] = Math.max(maxHeaderWidth, maxValueWidth);
        }

        // Imprimir separador
        System.out.print("+");
        for (int i = 0; i <= header.length; i++) {
            for (int j = 0; j < maxColWidths[i] + 2; j++) { // +2 para el espacio de relleno alrededor del texto
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();

        // Imprimir fila de encabezado
        System.out.print("|");
        System.out.printf(" %-" + maxColWidths[0] + "s |", "");
        for (int i = 0; i < header.length; i++) {
            System.out.printf(" %-" + maxColWidths[i + 1] + "s |", header[i]);
        }
        System.out.println();

        // Imprimir separador
        System.out.print("+");
        for (int i = 0; i <= header.length; i++) {
            for (int j = 0; j < maxColWidths[i] + 2; j++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();

        // Imprimir filas de datos
        for (int i = 0; i < indice.length; i++) {
            System.out.print("|");
            System.out.printf(" %-" + maxColWidths[0] + "s |", indice[i]);
            for (int j = 0; j < header.length; j++) {
                System.out.printf(" %-" + maxColWidths[j + 1] + ".2f |", valores[i][j]);
            }
            System.out.println();
        }

        // Imprimir separador
        System.out.print("+");
        for (int i = 0; i <= header.length; i++) {
            for (int j = 0; j < maxColWidths[i] + 2; j++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println('\n');
    }
}
