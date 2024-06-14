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
                new Integer[] { 3, 4, 5, 6 } // Columnas de hechos de ventas
        );

        Cubo cubo = Cubo.crearFromConfig(config);
        System.out.println("Cubo creado: " + cubo);

        // Proyecciones
        Proyeccion proyeccion = cubo.proyectar();

        // Mostrar Dimension POS (hecho: default)
        proyeccion.print("POS");

        // Mostrar Dimensiones POS vs Fechas (hecho: costo)
        proyeccion.seleccionarHecho("costo");
        proyeccion.seleccionarMedida("suma");
        proyeccion.print("POS", "Fechas");

        // Drill Down por Fechas

        Cubo cubo2 = cubo.drilldown("Fechas");

        // Proyecciones
        Proyeccion proyeccion2 = cubo2.proyectar();

        // Mostrar Dimensiones POS vs Fechas (hecho: costo)
        proyeccion2.seleccionarHecho("costo");
        proyeccion2.seleccionarMedida("suma");
        proyeccion2.print("POS", "Fechas");

        // Rollup por Fechas

        Cubo cubo3 = cubo2.rollup("Fechas");

        // Proyecciones
        Proyeccion proyeccion3 = cubo3.proyectar();

        // Mostrar Dimensiones POS vs Fechas (hecho: costo)
        proyeccion3.seleccionarHecho("costo");
        proyeccion3.seleccionarMedida("suma");
        proyeccion3.print("POS", "Fechas");

        // Slice!

        Cubo cuboslice = cubo3.slice("Fechas", "2019");

        Proyeccion proyeccionslice = cuboslice.proyectar();

        // Mostrar Dimensiones POS vs Productos (hecho: costo) ACA MUESTRA LO MISMO QUE EL CUBO ENTERO
        proyeccionslice.seleccionarHecho("costo");
        proyeccionslice.seleccionarMedida("suma");
        proyeccionslice.print("POS", "Productos");

        // Dice! NO SUMA BIEN LOS VALORES EN 1D MUESTRA UN VALOR y EN 2D LA SUMA NO ES IGUAL AL VALOR De 1D

        Cubo cubodice = cubo2.dice("POS", new String[]{"Canada","France"}, "Fechas", new String[]{"2018","2019"});

        Proyeccion proyeccionsdice = cubodice.proyectar();


        // Mostrar Dimensiones POS vs Fechas (hecho: valor total)
        proyeccionsdice.seleccionarHecho("valor_total");
        proyeccionsdice.seleccionarMedida("suma");
        proyeccionsdice.print("POS");
        proyeccionsdice.print("POS", "Fechas");

    }
}
