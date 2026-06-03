package uv.lis.modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DB = "italia_pizza";
    private static final String USUARIO = "admin_pizza";
    private static final String PASSWORD = "pizza_ita23";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB;

    private static ConexionBD instancia;
    private Connection conexion;

    private ConexionBD() {
    }

    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("user", USUARIO);
            props.setProperty("password", PASSWORD);
            props.setProperty("useSSL", "false");
            props.setProperty("allowPublicKeyRetrieval", "true");
            props.setProperty("serverTimezone", "UTC");
            props.setProperty("useUnicode", "true");
            props.setProperty("characterEncoding", "UTF-8");
            conexion = DriverManager.getConnection(URL, props);
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (instancia != null && instancia.conexion != null) {
            try {
                instancia.conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar: " + e.getMessage());
            } finally {
                instancia.conexion = null;
            }
        }
    }
}
