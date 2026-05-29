package uv.lis.modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConexionBD — Singleton de conexión a MySQL.
 * Italia Pizza | FEI-UV 2025
 */
public class ConexionBD {

    private static final String URL      = "jdbc:mysql://localhost:3306/italia_pizza"
                                         + "?useSSL=false&serverTimezone=America/Mexico_City"
                                         + "&allowPublicKeyRetrieval=true";
    private static final String USUARIO  = "admin_pizza";
    private static final String PASSWORD = "pizza_ita23";

    private static ConexionBD instancia;
    private Connection conexion;

    private ConexionBD() {}

    public static ConexionBD getInstancia() {
        if (instancia == null) instancia = new ConexionBD();
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed())
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        return conexion;
    }

    public static void cerrarConexion() {
        if (instancia != null && instancia.conexion != null) {
            try { instancia.conexion.close(); }
            catch (SQLException e) { System.err.println("Error al cerrar: " + e.getMessage()); }
            finally { instancia.conexion = null; }
        }
    }
}
