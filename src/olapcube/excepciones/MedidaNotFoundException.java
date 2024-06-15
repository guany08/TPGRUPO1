package olapcube.excepciones;

public class MedidaNotFoundException extends RuntimeException {
    public MedidaNotFoundException(String message) {
        super(message);
    }
}