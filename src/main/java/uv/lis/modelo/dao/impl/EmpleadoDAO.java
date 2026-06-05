package uv.lis.modelo.dao.impl;

import uv.lis.modelo.conexion.ConexionBD;
import uv.lis.modelo.dao.contratos.IEmpleadoDAO;
import uv.lis.modelo.dominio.Empleado;
import uv.lis.modelo.dominio.Rol;
import uv.lis.modelo.excepciones.AutenticacionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO implements IEmpleadoDAO {

    private Connection getConn() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public Empleado autenticar(String username, String contrasena) throws Exception {
        String sql = "SELECT e.idEmpleado, u.nombre, u.apellidoPaterno, u.apellidoMaterno, "
                + "u.ciudad, u.estatus, e.username, r.idRol, r.nombreRol "
                + "FROM Empleado e "
                + "INNER JOIN Usuario u ON e.idEmpleado = u.idUsuario "
                + "INNER JOIN Rol r ON e.Rol_idRol = r.idRol "
                + "WHERE e.username = ? AND e.contrasena = SHA2(?, 256)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new AutenticacionException(
                            AutenticacionException.Motivo.CREDENCIALES_INCORRECTAS);
                }
                if (rs.getInt("estatus") == 0) {
                    throw new AutenticacionException(
                            AutenticacionException.Motivo.CUENTA_INACTIVA);
                }
                return mapEmpleado(rs);
            }
        }
    }

    @Override
    public boolean registrar(Empleado emp, String telefono, String email) throws Exception {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            int idUsuario;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Usuario (nombre,apellidoPaterno,apellidoMaterno,ciudad,estatus) VALUES (?,?,?,?,1)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, emp.getNombre());
                ps.setString(2, emp.getApellidoPaterno());
                ps.setString(3, emp.getApellidoMaterno());
                ps.setString(4, emp.getCiudad());
                ps.executeUpdate();
                try (ResultSet rk = ps.getGeneratedKeys()) {
                    rk.next();
                    idUsuario = rk.getInt(1);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Empleado (idEmpleado,username,contrasena,Rol_idRol) VALUES (?,?,SHA2(?,256),?)")) {
                ps.setInt(1, idUsuario);
                ps.setString(2, emp.getUsername());
                ps.setString(3, emp.getContrasena());
                ps.setInt(4, emp.getRol().getIdRol());
                ps.executeUpdate();
            }
            if (telefono != null && !telefono.isBlank()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Telefono VALUES (?,?)")) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, telefono);
                    ps.executeUpdate();
                }
            }
            if (email != null && !email.isBlank()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Email VALUES (?,?)")) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, email);
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error al registrar empleado: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean actualizar(Empleado emp, String telefono, String email) throws Exception {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Usuario SET nombre=?,apellidoPaterno=?,apellidoMaterno=?,ciudad=? WHERE idUsuario=?")) {
                ps.setString(1, emp.getNombre());
                ps.setString(2, emp.getApellidoPaterno());
                ps.setString(3, emp.getApellidoMaterno());
                ps.setString(4, emp.getCiudad());
                ps.setInt(5, emp.getIdUsuario());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Empleado SET username=?,Rol_idRol=? WHERE idEmpleado=?")) {
                ps.setString(1, emp.getUsername());
                ps.setInt(2, emp.getRol().getIdRol());
                ps.setInt(3, emp.getIdUsuario());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Telefono WHERE Usuario_idUsuario=?")) {
                ps.setInt(1, emp.getIdUsuario());
                ps.executeUpdate();
            }
            if (telefono != null && !telefono.isBlank()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Telefono VALUES (?,?)")) {
                    ps.setInt(1, emp.getIdUsuario());
                    ps.setString(2, telefono);
                    ps.executeUpdate();
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Email WHERE Usuario_idUsuario=?")) {
                ps.setInt(1, emp.getIdUsuario());
                ps.executeUpdate();
            }
            if (email != null && !email.isBlank()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Email VALUES (?,?)")) {
                    ps.setInt(1, emp.getIdUsuario());
                    ps.setString(2, email);
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error al actualizar empleado: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean eliminarLogico(int idEmpleado, int idSesion) throws Exception {
        String llamada = "{ CALL sp_eliminar_usuario_logico(?, ?, ?) }";
        try (CallableStatement cs = getConn().prepareCall(llamada)) {
            cs.setInt(1, idEmpleado);
            cs.setInt(2, idSesion);
            cs.registerOutParameter(3, Types.VARCHAR); 
            cs.execute();

            String mensaje = cs.getString(3);
            if (mensaje == null || !mensaje.toLowerCase().contains("correctamente")) {
                throw new Exception(mensaje != null ? mensaje
                        : "No se pudo eliminar el empleado.");
            }
            return true;
        } catch (SQLException e) {
            throw new Exception("Error al eliminar empleado: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Empleado> buscarTodos() throws Exception {
        String sql = "SELECT e.idEmpleado,u.nombre,u.apellidoPaterno,u.apellidoMaterno,"
                + "u.ciudad,u.estatus,e.username,r.idRol,r.nombreRol "
                + "FROM Empleado e INNER JOIN Usuario u ON e.idEmpleado=u.idUsuario "
                + "INNER JOIN Rol r ON e.Rol_idRol=r.idRol ORDER BY u.nombre";
        List<Empleado> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapEmpleado(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Empleado> buscarPorNombre(String nombre) throws Exception {
        String sql = "SELECT e.idEmpleado,u.nombre,u.apellidoPaterno,u.apellidoMaterno,"
                + "u.ciudad,u.estatus,e.username,r.idRol,r.nombreRol "
                + "FROM Empleado e INNER JOIN Usuario u ON e.idEmpleado=u.idUsuario "
                + "INNER JOIN Rol r ON e.Rol_idRol=r.idRol "
                + "WHERE LOWER(CONCAT(u.nombre,' ',u.apellidoPaterno)) LIKE LOWER(?) ORDER BY u.nombre";
        List<Empleado> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapEmpleado(rs));
                }
            }
        }
        return lista;
    }

    private Empleado mapEmpleado(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setIdUsuario(rs.getInt("idEmpleado"));
        e.setNombre(rs.getString("nombre"));
        e.setApellidoPaterno(rs.getString("apellidoPaterno"));
        e.setApellidoMaterno(rs.getString("apellidoMaterno"));
        e.setCiudad(rs.getString("ciudad"));
        e.setEstatus(rs.getInt("estatus"));
        e.setUsername(rs.getString("username"));
        e.setRol(new Rol(rs.getInt("idRol"), rs.getString("nombreRol")));
        return e;
    }
}
