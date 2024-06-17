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
                4, // Columna de valor para POS
                5, // Columna de valor para Fechas
                0, // FK Productos
                1, // FK POS
                2, // FK Fechas
                new Integer[] { 3, 4, 5, 6 } // Columnas de hechos de ventas, changed to int[]
        );

        Cubo cubo = Cubo.crearFromConfig(config);
        System.out.println("Cubo creado: " + cubo);

        // Proyecciones
        System.out.println(("PROYECCION 1D"));
        Proyeccion proyeccion = cubo.proyectar();

        // Mostrar Dimension POS (hecho: default)
        proyeccion.print("POS");
        System.out.println(("PROYECCION 2D"));
        // Mostrar Dimensiones POS vs Fechas (hecho: costo)
        proyeccion.seleccionarHecho("costo");
        proyeccion.seleccionarMedida("suma");
        proyeccion.print("POS", "Fechas");

        // Slice!
        Cubo cuboslice = cubo.slice("Fechas", "2019");

        Proyeccion proyeccionslice = cuboslice.proyectar();
        System.out.println(("PROYECCION 2D DESPUES DEL SLICE POR FECHAS 2019"));
        proyeccionslice.seleccionarHecho("costo");
        proyeccionslice.seleccionarMedida("suma");
        proyeccionslice.print("POS");
        proyeccionslice.print("POS", "Productos");

        // Dice!
        Map<String, Set<String>> condiciones = new HashMap<>();
        condiciones.put("POS", Set.of("Canada", "France"));
        condiciones.put("Fechas", Set.of("2018", "2019"));

        Cubo cubodice = cubo.dice(condiciones);

        Proyeccion proyecciondice = cubodice.proyectar();
        System.out.println(("PROYECCION 2D DESPUES DEL DICE POR PAÍSES FRANCIA, CANADA Y FECHAS 2018, 2019"));
        proyecciondice.seleccionarHecho("costo");
        proyecciondice.seleccionarMedida("suma");
        proyecciondice.print("POS");
        proyecciondice.print("POS", "Productos");
    }
}
