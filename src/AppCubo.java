import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import olapcube.Proyeccion;
import olapcube.configuration.ConfigCubo;
import olapcube.configuration.ConfigCuboFactory;
import olapcube.estructura.Cubo;

public class AppCubo {

    public static void main(String[] args) {
        ConfigCubo config = ConfigCuboFactory.crearConfigCubo(
                "Cubo de ventas",
                "src/olapcube/datasets-olap/",
                "ventas.csv",
                "productos.csv",
                "puntos_venta.csv",
                "fechas.csv",
                3, // Columna de valor para Productos
                5, // Columna de valor para POS
                4, // Columna de valor para Fechas
                0, // FK Productos
                1, // FK POS
                2, // FK Fechas
                new Integer[] { 3, 4, 5, 6 } // Columnas de hechos de ventas
        );

        Cubo cubo = Cubo.crearFromConfig(config);
        System.out.println("Cubo creado: " + cubo);

        // Proyección inicial
        System.out.println("PROYECCION 1D");
        Proyeccion proyeccion = cubo.proyectar();
        proyeccion.print("POS");

        // Proyección 2D antes del Drill Down
        System.out.println("PROYECCION 2D");
        proyeccion.seleccionarHecho("valor_total");
        proyeccion.seleccionarMedida("suma");
        proyeccion.print("POS", "Fechas");

        // Roll Up por Fechas
        Cubo cuboRollUpFechas = cubo.rollup("Fechas");
        Proyeccion proyeccionRollUpFechas = cuboRollUpFechas.proyectar();
        System.out.println("PROYECCION 2D DESPUES DEL ROLL UP POR FECHAS");
        proyeccionRollUpFechas.seleccionarHecho("costo");
        proyeccionRollUpFechas.seleccionarMedida("suma");
        proyeccionRollUpFechas.print("POS", "Fechas");

        // Drill Down por POS
        Cubo cuboDrillDownPOS = cuboRollUpFechas.drilldown("POS");
        Proyeccion proyeccionDrillDownPOS = cuboDrillDownPOS.proyectar();
        System.out.println("PROYECCION 2D DESPUES DEL DRILL DOWN POR POS");
        proyeccionDrillDownPOS.seleccionarHecho("costo");
        proyeccionDrillDownPOS.seleccionarMedida("suma");
        proyeccionDrillDownPOS.print("POS", "Fechas");

        // Slice por Fechas
        Cubo cuboSlice = cuboDrillDownPOS.slice("Fechas", "2019");
        Proyeccion proyeccionSlice = cuboSlice.proyectar();
        System.out.println("PROYECCION 1D DESPUES DEL SLICE POR FECHAS 2019");
        proyeccionSlice.seleccionarHecho("costo");
        proyeccionSlice.seleccionarMedida("suma");
        proyeccionSlice.print("POS");
        System.out.println("PROYECCION 2D DESPUES DEL SLICE POR FECHAS 2019");
        proyeccionSlice.print("POS", "Productos");

        // Dice por POS y Fechas
        Map<String, Set<String>> condiciones = new HashMap<>();
        condiciones.put("POS", Set.of("Canada", "France"));
        condiciones.put("Fechas", Set.of("2018", "2019"));

        Cubo cuboDice = cuboSlice.dice(condiciones);
        Proyeccion proyeccionDice = cuboDice.proyectar();
        System.out.println("PROYECCION 1D DESPUES DEL DICE POR PAÍSES FRANCIA, CANADA Y FECHAS 2018, 2019");
        proyeccionDice.seleccionarHecho("costo");
        proyeccionDice.seleccionarMedida("suma");
        proyeccionDice.print("POS");
        System.out.println("PROYECCION 2D DESPUES DEL DICE POR PAÍSES FRANCIA, CANADA Y FECHAS 2018, 2019");
        proyeccionDice.print("Productos", "POS");
        System.out.println("CAMBIO DE EJES");
        proyeccionDice.print("POS", "Productos");
    }
}
