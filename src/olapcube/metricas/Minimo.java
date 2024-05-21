package olapcube.metricas;

import java.util.List;

/**
 * Clase que representa una medida de minimo
 */
public class Minimo extends Medida {

    public Minimo() {
        super("minimo");
    }

    @Override
    public double calcular(List<Double> valores) {
        double minimo = Integer.MAX_VALUE;
        for (Double valor : valores) {
            if (valor < minimo) {
                minimo = valor;
            }
        }
        
        // TODO:
        // no valor, toString da nAn. En estructura nueva clase no valor. new no valor
        // por ahora, MAX_VALUE todos los valores que no sean numeros.
        return minimo;
    }
}
