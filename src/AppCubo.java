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

        // Mostrar Dimensiones POS vs Fechas (hecho: cantidad)
        proyeccion.seleccionarHecho("costo");
        proyeccion.seleccionarMedida("suma");
        proyeccion.print("POS", "Fechas");

        // Drill down Fecha

        Cubo cubo2 = cubo.drilldown("Fechas");

        // Proyecciones
        Proyeccion proyeccion2 = cubo2.proyectar();

        // Mostrar Dimension POS (hecho: default)
        proyeccion2.print("POS");

        // Mostrar Dimensiones POS vs Fechas (hecho: cantidad)
        proyeccion2.seleccionarHecho("costo");
        proyeccion2.seleccionarMedida("suma");
        proyeccion2.print("POS", "Fechas");
        
        // Roll Up de fecha
        Cubo cubo3 = cubo2.rollup("Fechas");
        Proyeccion proyeccion3 = cubo3.proyectar();
        proyeccion3.seleccionarHecho("costo");
        proyeccion3.seleccionarMedida("suma");
        proyeccion3.print("POS", "Fechas");
        
        // Slice!
        Cubo cuboslice = cubo3.slice("Fechas", "2019");
        Proyeccion proyeccionslice = cuboslice.proyectar();

        // Mostrar Dimensiones POS vs Productos (hecho: valor total)
        proyeccionslice.seleccionarHecho("valor_total");
        proyeccionslice.seleccionarMedida("suma");
        proyeccionslice.print("POS", "Productos");

        proyeccion3.seleccionarHecho("valor_total");
        proyeccion3.seleccionarMedida("suma");
        proyeccion3.print("POS", "Productos");

        // Dice!

        Cubo cubodice = cubo2.dice("POS", new String[]{"Canada","France"}, "Fechas", new String[]{"2018","2019"});

        Proyeccion proyeccionsdice = cubodice.proyectar();

        // Esto no funciona bien.
        proyeccionsdice.print("POS");

        // Mostrar Dimensiones POS vs Fechas (hecho: cantidad)
        proyeccionsdice.seleccionarHecho("costo");
        proyeccionsdice.seleccionarMedida("suma");
        proyeccionsdice.print("POS", "Fechas");

    }
}
