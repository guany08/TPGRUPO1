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
        Configurador config = new Configurador(new String[]{filePath + "/ventas.csv", filePath +"/productos.csv" , filePath + "/puntos_venta.csv" ,filePath + "/fechas.csv"});
        Cubo cubo = new Cubo(config);
        /*
        List<List<Celda>> factData = cubo.getFactData();
        List<List<Celda>> dim1Data = cubo.getDim1Data();
        List<List<Celda>> dim2Data = cubo.getDim2Data();
        List<List<Celda>> dim3Data = cubo.getDim3Data();
        */
        // Imprimir los datos de Ventas
        //System.out.println("Datos de Ventas:");
        // for (String[] row : factData) {
        //     System.out.println(String.join(", ", row));
        // }
        /*
        for (int i = 0; i < 5; i++ ){
            System.out.println(String.join(", ", factData.get(i)));
        }

        for (int i = 0; i < 5; i++ ){
            System.out.println(String.join(", ", dim1Data.get(i)));
        }
        */
        // AHORA IMPRIME LA LISTA DE LISTAS DE CELDAS
        //System.out.println(factData.get(3).get(2).getValorNumerico());
    
        List<List<Celda>> completo = cubo.getCompleto();
        List<Celda> headers = cubo.getHeaders();

        int cont = 0;
        int tamaño = 0;

        for (Celda header : headers){
            System.out.print(header.getValor() + ", ");
        }
        System.out.print("\n");
        for (int i = 0; i < completo.get(0).size(); i++ ){
            System.out.print(completo.get(1).get(i).getValor() + ", ");
        }


        for (List<Celda> row : completo) {
            cont = cont + 1;
            tamaño = row.size();
            /*
            for (Celda celda : row) {
                System.out.print(celda.getValor() + " ");
            }
            */
        }
        System.out.println("\n" + cont);
        System.out.println(tamaño);



    }
}