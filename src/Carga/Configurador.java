package Carga;

public class Configurador {
    private String fact;
    private String dim1;
    private String dim2;
    private String dim3;

    public Configurador(String[] configuracion) {
        this.fact = configuracion[0];
        this.dim1 = configuracion[1];
        this.dim2 = configuracion[2];
        this.dim3 = configuracion[3];
    }

    public String getFact() {
        return fact;
    }

    public String getDim1() {
        return dim1;
    }

    public String getDim2() {
        return dim2;
    }

    public String getDim3() {
        return dim3;
    }
}