import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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
            // Obtener las claves de las dimensiones
            String dim1KeyValue = row.get(0).getValor(); // Suponiendo que el valor clave de la dimensión 1 está en la segunda columna
            String dim2KeyValue = row.get(1).getValor(); // Suponiendo que el valor clave de la dimensión 2 está en la tercera columna
            String dim3KeyValue = row.get(2).getValor(); // Suponiendo que el valor clave de la dimensión 3 está en la cuarta columna
    
            // Buscar las filas correspondientes en las tablas de dimensiones
            List<Celda> dim1Row = buscarEnDimension(dim1Data, dim1KeyValue);
            List<Celda> dim2Row = buscarEnDimension(dim2Data, dim2KeyValue);
            List<Celda> dim3Row = buscarEnDimension(dim3Data, dim3KeyValue);
    
            // Crear una nueva fila que contenga los datos de la factTable y las dimensiones,
            // omitiendo las columnas de clave de las dimensiones
            List<Celda> newRow = new ArrayList<>();
            // Agregar el primer dato de la factTable (suponiendo que es el ID)
            if (!row.isEmpty()) {
                newRow.add(row.get(0));
            }
            // Agregar los datos restantes de la factTable
            if (row.size() > 4) {
                newRow.addAll(row.subList(4, row.size()));
            }
            // Agregar los datos de la dimensión 1 (omitir la clave)
            if (dim1Row.size() > 1) {
                newRow.addAll(dim1Row.subList(1, dim1Row.size()));
            }
            // Agregar los datos de la dimensión 2 (omitir la clave)
            if (dim2Row.size() > 1) {
                newRow.addAll(dim2Row.subList(1, dim2Row.size()));
            }
            // Agregar los datos de la dimensión 3 (omitir la clave)
            if (dim3Row.size() > 1) {
                newRow.addAll(dim3Row.subList(1, dim3Row.size()));
            }
    
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