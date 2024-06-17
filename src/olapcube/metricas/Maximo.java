package olapcube.metricas;

import java.util.List;

/**
 * Clase que representa una medida de maximo
 */
public class Maximo extends Medida {

    public Maximo() {
        super("maximo");
    }

    @Override
    public double calcular(List<Double> valores) {
        double maximo = Double.MIN_VALUE;
        for (Double valor : valores) {
            if (valor >= maximo) {
                maximo = valor;
            }
        }
        if (maximo == Double.MIN_VALUE){
            maximo = 0;
        }
        
        return maximo;
    }
}
