package uv.lis.modelo.excepciones;

public class AutenticacionException extends PizzeriaException {

    public enum Motivo {
        CREDENCIALES_INCORRECTAS,
        CUENTA_INACTIVA,
        ACCESO_DENEGADO
    }

    private final Motivo motivo;

    public AutenticacionException(Motivo motivo) {
        super(mensajePara(motivo));
        this.motivo = motivo;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    private static String mensajePara(Motivo m) {
        return switch (m) {
            case CREDENCIALES_INCORRECTAS -> "Usuario o contraseña incorrectos.";
            case CUENTA_INACTIVA          -> "Tu cuenta está inactiva. Contacta al administrador.";
            case ACCESO_DENEGADO          -> "No tienes permiso para acceder a esta sección.";
        };
    }
}