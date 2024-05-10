package Carga;

public class Celda {
    private String valor;
    
    public Celda(String valor){
        this.valor = valor;
    }
    public String getValor(){
        return valor;
    }
    public double getValorNumerico(){
        return Double.parseDouble(valor);
    }
}
