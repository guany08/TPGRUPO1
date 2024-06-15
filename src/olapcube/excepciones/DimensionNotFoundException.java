package olapcube.excepciones;

public class DimensionNotFoundException extends RuntimeException {
    public DimensionNotFoundException(String message) {
        super(message);
    }
}