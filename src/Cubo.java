import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import Carga.Cargador;
import Carga.Celda;
import Carga.Configurador;

public class Cubo {
    private List<List<Celda>> factData;
    private List<List<Celda>> dim1Data;
    private List<List<Celda>> dim2Data;
    private List<List<Celda>> dim3Data;
    private int[] dimLength = new int[4]; 

    public Cubo(Configurador configurador) throws IOException {
        Cargador cargador = new Cargador(configurador);
        this.factData = cargador.cargarFact();
        dimLength[0] = factData.get(0).size() - 1;

        this.dim1Data = cargador.cargarDim1();
        dimLength[1] = dim1Data.get(0).size() - 1;

        this.dim2Data = cargador.cargarDim2();
        dimLength[2] = dim2Data.get(0).size() - 1;

        this.dim3Data = cargador.cargarDim3();
        dimLength[3] = dim3Data.get(0).size() - 1;

    }

     // Método para buscar una fila en una dimensión según un valor clave
     public List<Celda> buscarEnDimension(List<List<Celda>> dimensionData, String keyValue) {
        for (List<Celda> row : dimensionData) {
            if (row.get(0).getValor().equals(keyValue)) { // Suponiendo que el valor clave está en la primera columna
                return row;
            }
        }
        return new ArrayList<>(); // Devolver una lista vacía si no se encuentra el valor clave
    }

    public List<List<Celda>> agregarDimensiones(List<List<Celda>> factTable) {
        List<List<Celda>> factTableConDimensiones = new ArrayList<>();
        for (List<Celda> row : factTable) {
            String dim1KeyValue = row.get(0).getValor(); // Suponiendo que el valor clave de la dimensión 1 está en la segunda columna
            List<Celda> dim1Row = buscarEnDimension(dim1Data, dim1KeyValue);

            String dim2KeyValue = row.get(0).getValor(); // Suponiendo que el valor clave de la dimensión 2 está en la tercera columna
            List<Celda> dim2Row = buscarEnDimension(dim2Data, dim2KeyValue);

            String dim3KeyValue = row.get(0).getValor(); // Suponiendo que el valor clave de la dimensión 3 está en la cuarta columna
            List<Celda> dim3Row = buscarEnDimension(dim3Data, dim3KeyValue);

            // Crear una nueva fila que contenga los datos de la factTable y las dimensiones
            List<Celda> newRow = new ArrayList<>();
            newRow.addAll(row); // Agregar los datos de la factTable
            newRow.addAll(dim1Row); // Agregar los datos de la dimensión 1
            newRow.addAll(dim2Row); // Agregar los datos de la dimensión 2
            newRow.addAll(dim3Row); // Agregar los datos de la dimensión 3

            // Agregar la nueva fila a la factTable con dimensiones
            factTableConDimensiones.add(newRow);
        }
        return factTableConDimensiones;
    }



    public List<List<Celda>> getFactData() {
        return factData;
    }

    public List<List<Celda>> getDim1Data() {
        return dim1Data;
    }

    public List<List<Celda>> getDim2Data() {
        return dim2Data;
    }

    public List<List<Celda>> getDim3Data() {
        return dim3Data;
    }


    public int getDimLength(int i) {
        return dimLength[i];
    }


}