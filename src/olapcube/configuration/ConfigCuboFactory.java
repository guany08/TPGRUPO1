package olapcube.configuration;

public class ConfigCuboFactory {

    public static ConfigCubo crearConfigCubo(
            String nombreCubo,
            String directorioDatos,
            String archivoVentas,
            String archivoProductos,
            String archivoPOS,
            String archivoFechas,
            Integer columnaProductos,
            Integer columnaPOS,
            Integer columnaFechas,
            Integer columnaFKProductos,
            Integer columnaFKPOS,
            Integer columnaFKFechas,
            Integer[] columnasHechos) {
        return new ConfigCubo(
                nombreCubo,
                ConfigHechos.configCSV(
                        directorioDatos + archivoVentas,
                        new String[] { "cantidad", "valor_unitario", "valor_total", "costo" },
                        columnasHechos),
                new ConfigDimension[] {
                        ConfigDimension.configCSV("Productos", directorioDatos + archivoProductos, 0, columnaProductos,
                                columnaFKProductos),
                        ConfigDimension.configCSV("POS", directorioDatos + archivoPOS, 0, columnaPOS, columnaFKPOS),
                        ConfigDimension.configCSV("Fechas", directorioDatos + archivoFechas, 0, columnaFechas,
                                columnaFKFechas)
                });
    }
}