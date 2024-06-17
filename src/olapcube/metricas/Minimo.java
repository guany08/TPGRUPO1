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
        double minimo = Double.MAX_VALUE;
        for (Double valor : valores) {
            if (valor < minimo) {
                minimo = valor;
            }
        }
        if (minimo == Double.MAX_VALUE){
            minimo = 0;
        }
        return minimo;
    }
}
