package Carga;

public class Configurador {
    private String fact;
    private String dim1;
    private String dim2;
    private String dim3;
    private String dimKeys[] = new String[3];

    public Configurador(String[] configuracion) {
        this.fact = configuracion[0];
        this.dim1 = configuracion[1];
        this.dim2 = configuracion[2];
        this.dim3 = configuracion[3];
        this.dimKeys[0] = configuracion[4];
        this.dimKeys[1] = configuracion[5];
        this.dimKeys[2] = configuracion[6];
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

    public String[] getKeys() {
        return dimKeys;
    }
}