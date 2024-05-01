import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Carga.Configurador;
import Carga.Cubo;

public class App {
    public static void main(String[] args) throws Exception {
        String filePath = System.getProperty("user.dir");
        Configurador config = new Configurador(new String[]{filePath + "/ventas.csv", filePath +"/puntos_venta.csv",filePath + "/productos.csv",filePath + "/fechas.csv"});
        Cubo cubo = new Cubo(config);
        try {
            List<String[]> factData = cubo.cargarFact();
            List<String[]> dim1Data = cubo.cargarDim1();
            List<String[]> dim2Data = cubo.cargarDim2();
            List<String[]> dim3Data = cubo.cargarDim3();

            // Imprimir los datos de Ventas
            System.out.println("Datos de facturación:");
            for (String[] row : factData) {
                System.out.println(String.join(", ", row));
            }

            // Repetir el proceso para los datos de las dimensiones dim1, dim2 y dim3

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}