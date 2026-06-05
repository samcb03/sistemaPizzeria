package uv.lis.modelo.excepciones;

public class ValidacionException extends PizzeriaException {

    private final String campo;

    public ValidacionException(String mensaje) {
        super(mensaje);
        this.campo = null;
    }

    public ValidacionException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }

    @Override
    public String getMessage() {
        if (campo != null && !campo.isBlank()) {
            return "[" + campo + "] " + super.getMessage();
        }
        return super.getMessage();
    }
}