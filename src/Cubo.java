import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import Carga.Cargador;
import Carga.Configurador;

public class Cubo {
    private List<String[]> factData;
    private List<String[]> dim1Data;
    private List<String[]> dim2Data;
    private List<String[]> dim3Data;
    private int[] dimLength = new int[4]; 

    public Cubo(Configurador configurador) throws IOException {
        Cargador cargador = new Cargador(configurador);
        this.factData = cargador.cargarFact();
        dimLength[0] = factData.get(0).length - 1;

        this.dim1Data = cargador.cargarDim1();
        dimLength[1] = dim1Data.get(0).length - 1;

        this.dim2Data = cargador.cargarDim2();
        dimLength[2] = dim2Data.get(0).length - 1;

        this.dim3Data = cargador.cargarDim3();
        dimLength[3] = dim3Data.get(0).length - 1;

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


    public int getDimLength(int i) {
        return dimLength[i];
    }


}