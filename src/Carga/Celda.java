package Carga;

public class Celda {
    private String valor;
    private boolean operable;
    
    public Celda(String valor, boolean operable){
        this.valor = valor;
        this.operable = operable;
    }
    public String getValor(){
        return valor;
    }
    public double getValorNumerico(){
        return Double.parseDouble(valor);
    }
}
