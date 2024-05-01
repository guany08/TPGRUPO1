import java.io.IOException;
import java.util.List;

import Carga.Cargador;
import Carga.Configurador;

public class Cubo {
    private List<String[]> factData;
    private List<String[]> dim1Data;
    private List<String[]> dim2Data;
    private List<String[]> dim3Data;

    public Cubo(Configurador configurador) throws IOException {
        Cargador cargador = new Cargador(configurador);
        this.factData = cargador.cargarFact();
        this.dim1Data = cargador.cargarDim1();
        this.dim2Data = cargador.cargarDim2();
        this.dim3Data = cargador.cargarDim3();
    }


    public List<String[]> getFactData() {
        return factData;
    }

    public List<String[]> getDim1Data() {
        return dim1Data;
    }

    public List<String[]> getDim2Data() {
        return dim2Data;
    }

    public List<String[]> getDim3Data() {
        return dim3Data;
    }
}