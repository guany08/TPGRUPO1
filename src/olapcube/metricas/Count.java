package olapcube.metricas;

import java.util.List;

/**
 * Clase que representa una medida de count
 */
public class Count extends Medida {

    public Count() {
        super("count");
    }

    @Override
    public double calcular(List<Double> valores) {
        double count = 0;

        for (Double valor : valores) {
            count += 1;
        }
        
        return count;
    }
}
