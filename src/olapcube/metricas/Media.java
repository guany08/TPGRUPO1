package olapcube.metricas;

import java.util.List;

/**
 * Clase que representa una medida de media
 */
public class Media extends Medida {

    public Media() {
        super("media");
    }

    @Override
    public double calcular(List<Double> valores) {
        double suma = 0;
        double count = 0;
        double media = 0;

        for (Double valor : valores) {
            suma += valor;
            count += 1;
        }
        if (suma > 0){
            media = suma / count;
        }
        if (suma == 0){
            media = 0;
        }
        
        return media;
    }
}
