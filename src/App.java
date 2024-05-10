import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Carga.Configurador;
import Carga.Cargador;
import Carga.Celda;

public class App {
    public static void main(String[] args) throws Exception {
        String filePath = System.getProperty("user.dir");
        Configurador config = new Configurador(new String[]{filePath + "/ventas.csv", filePath +"/puntos_venta.csv",filePath + "/productos.csv",filePath + "/fechas.csv"});
        Cubo cubo = new Cubo(config);
        List<String[]> factData = cubo.getFactData();
        List<String[]> dim1Data = cubo.getDim1Data();
        List<String[]> dim2Data = cubo.getDim2Data();
        List<String[]> dim3Data = cubo.getDim3Data();

        // Imprimir los datos de Ventas
        System.out.println("Datos de Ventas:");
        // for (String[] row : factData) {
        //     System.out.println(String.join(", ", row));
        // }

        for (int i = 0; i < 5; i++ ){
            System.out.println(String.join(", ", factData.get(i)));
        }

        for (int i = 0; i < 5; i++ ){
            System.out.println(String.join(", ", dim1Data.get(i)));
        }

        System.out.println(dim1Data.get(1)[cubo.getDimLength(1)]);
        Celda celdita = new Celda("22");
        System.out.println(celdita.getValor());
        System.out.println(celdita.getValorNumerico());
        
    }
}