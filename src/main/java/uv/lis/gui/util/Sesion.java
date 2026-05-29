package uv.lis.gui.util;

import uv.lis.modelo.dominio.Empleado;

/**
 * Sesion — singleton que guarda el empleado autenticado.
 */
public class Sesion {
    private static Sesion instance;
    private Empleado empleadoActual;

    private Sesion() {}

    public static Sesion getInstance() {
        if (instance == null) instance = new Sesion();
        return instance;
    }

    public Empleado getEmpleadoActual() { return empleadoActual; }
    public void setEmpleadoActual(Empleado e) { this.empleadoActual = e; }

    public boolean esAdministrador() {
        return empleadoActual != null &&
               "Administrador".equals(empleadoActual.getRol().getNombreRol());
    }

    public void cerrar() { empleadoActual = null; }
}
