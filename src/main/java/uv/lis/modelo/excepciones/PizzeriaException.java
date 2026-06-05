package uv.lis.modelo.excepciones;

public class PizzeriaException extends Exception {

    public PizzeriaException(String mensaje) {
        super(mensaje);
    }

    public PizzeriaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}