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

        // Prueba Roll-Up

        Cubo cubo2 = cubo.rollup("Fechas");

        // Proyecciones
        Proyeccion proyeccion2 = cubo2.proyectar();

        // Mostrar Dimension POS (hecho: default)
        proyeccion2.print("POS");

        // Mostrar Dimensiones POS vs Fechas (hecho: cantidad)
        proyeccion2.seleccionarHecho("costo");
        proyeccion2.seleccionarMedida("suma");
        proyeccion2.print("POS", "Fechas");

        Cubo cubo3 = cubo2.slice("Fechas", 5, "2019 ");

    }
}
