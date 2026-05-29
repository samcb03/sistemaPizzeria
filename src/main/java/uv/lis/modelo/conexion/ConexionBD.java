package uv.lis.modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "URLbase";
    private static final String USUARIO = "usuariobd";
    private static final String CONTRASENA = "sucontraseñayasi";

    private ConexionBD() { } 

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}