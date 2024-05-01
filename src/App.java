import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Carga.Configurador;
import Carga.Cubo;

public class App {
    public static void main(String[] args) throws Exception {
        Configurador config = new Configurador(new String[]{"../ventas.csv", "../puntos_venta.csv", "../productos.csv", "../fechas.csv"});
        Cubo cubo = new Cubo(config);
        try {
            List<String[]> factData = cubo.cargarFact();
            List<String[]> dim1Data = cubo.cargarDim1();
            List<String[]> dim2Data = cubo.cargarDim2();
            List<String[]> dim3Data = cubo.cargarDim3();

            // Aquí puedes procesar los datos según sea necesario
            // Por ejemplo, imprimirlos
            System.out.println("Datos de facturación:");
            for (String[] row : factData) {
                System.out.println(String.join(", ", row));
            }
            // Repite el proceso para los datos de las dimensiones dim1, dim2 y dim3

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}