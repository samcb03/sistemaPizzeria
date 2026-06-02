package uv.lis.modelo.dao.impl;

import uv.lis.modelo.conexion.ConexionBD;
import uv.lis.modelo.dao.contratos.IClienteDAO;
import uv.lis.modelo.dominio.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {

    private Connection getConn() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean registrar(Cliente c, String telefono, String email) throws Exception {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            int idUsuario;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Usuario (nombre,apellidoPaterno,apellidoMaterno,ciudad,estatus) VALUES (?,?,?,?,1)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getNombre());
                ps.setString(2, c.getApellidoPaterno());
                ps.setString(3, c.getApellidoMaterno());
                ps.setString(4, c.getCiudad());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); idUsuario = rs.getInt(1); }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Cliente (idCliente,calleNumero,colonia,codigoPostal) VALUES (?,?,?,?)")) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, c.getCalleNumero());
                ps.setString(3, c.getColonia());
                ps.setInt(4, c.getCodigoPostal());
                ps.executeUpdate();
            }
            if (telefono != null && !telefono.isBlank()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Telefono VALUES (?,?)")) {
                    ps.setInt(1, idUsuario); ps.setString(2, telefono); ps.executeUpdate();
                }
            }
            if (email != null && !email.isBlank()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Email VALUES (?,?)")) {
                    ps.setInt(1, idUsuario); ps.setString(2, email); ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error al registrar cliente: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean actualizar(Cliente c, String telefono, String email) throws Exception {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Usuario SET nombre=?,apellidoPaterno=?,apellidoMaterno=?,ciudad=? WHERE idUsuario=?")) {
                ps.setString(1,c.getNombre()); ps.setString(2,c.getApellidoPaterno());
                ps.setString(3,c.getApellidoMaterno()); ps.setString(4,c.getCiudad());
                ps.setInt(5,c.getIdUsuario()); ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Cliente SET calleNumero=?,colonia=?,codigoPostal=? WHERE idCliente=?")) {
                ps.setInt(1,c.getCalleNumero()); ps.setString(2,c.getColonia());
                ps.setInt(3,c.getCodigoPostal()); ps.setInt(4,c.getIdUsuario()); ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Telefono WHERE Usuario_idUsuario=?")) {
                ps.setInt(1,c.getIdUsuario()); ps.executeUpdate();
            }
            if (telefono != null && !telefono.isBlank()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Telefono VALUES (?,?)")) {
                    ps.setInt(1,c.getIdUsuario()); ps.setString(2,telefono); ps.executeUpdate();
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Email WHERE Usuario_idUsuario=?")) {
                ps.setInt(1,c.getIdUsuario()); ps.executeUpdate();
            }
            if (email != null && !email.isBlank()) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Email VALUES (?,?)")) {
                    ps.setInt(1,c.getIdUsuario()); ps.setString(2,email); ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error al actualizar cliente: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean eliminarLogico(int idCliente, int idSesion) throws Exception {
        // Las reglas de negocio (cliente con pedidos, no autoeliminacion) y la
        // baja logica se centralizan en sp_eliminar_usuario_logico.
        String llamada = "{ CALL sp_eliminar_usuario_logico(?, ?, ?) }";
        try (CallableStatement cs = getConn().prepareCall(llamada)) {
            cs.setInt(1, idCliente);
            cs.setInt(2, idSesion);
            cs.registerOutParameter(3, Types.VARCHAR);          // OUT p_mensaje
            cs.execute();

            String mensaje = cs.getString(3);
            if (mensaje == null || !mensaje.toLowerCase().contains("correctamente"))
                throw new Exception(mensaje != null ? mensaje
                        : "No se pudo eliminar el cliente.");
            return true;
        } catch (SQLException e) {
            throw new Exception("Error al eliminar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> buscarTodos() throws Exception {
        return ejecutarConsulta("SELECT c.idCliente,u.nombre,u.apellidoPaterno,u.apellidoMaterno," +
                "u.ciudad,u.estatus,c.calleNumero,c.colonia,c.codigoPostal " +
                "FROM Cliente c INNER JOIN Usuario u ON c.idCliente=u.idUsuario ORDER BY u.nombre", null);
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) throws Exception {
        return ejecutarConsulta("SELECT c.idCliente,u.nombre,u.apellidoPaterno,u.apellidoMaterno," +
                "u.ciudad,u.estatus,c.calleNumero,c.colonia,c.codigoPostal " +
                "FROM Cliente c INNER JOIN Usuario u ON c.idCliente=u.idUsuario " +
                "WHERE LOWER(CONCAT(u.nombre,' ',u.apellidoPaterno)) LIKE LOWER(?) ORDER BY u.nombre",
                "%" + nombre + "%");
    }

    @Override
    public List<Cliente> buscarPorTelefono(String telefono) throws Exception {
        return ejecutarConsulta("SELECT c.idCliente,u.nombre,u.apellidoPaterno,u.apellidoMaterno," +
                "u.ciudad,u.estatus,c.calleNumero,c.colonia,c.codigoPostal " +
                "FROM Cliente c INNER JOIN Usuario u ON c.idCliente=u.idUsuario " +
                "INNER JOIN Telefono t ON t.Usuario_idUsuario=u.idUsuario " +
                "WHERE t.telefono LIKE ? ORDER BY u.nombre", "%" + telefono + "%");
    }

    @Override
    public List<Cliente> buscarPorDireccion(String direccion) throws Exception {
        return ejecutarConsulta("SELECT c.idCliente,u.nombre,u.apellidoPaterno,u.apellidoMaterno," +
                "u.ciudad,u.estatus,c.calleNumero,c.colonia,c.codigoPostal " +
                "FROM Cliente c INNER JOIN Usuario u ON c.idCliente=u.idUsuario " +
                "WHERE LOWER(c.colonia) LIKE LOWER(?) ORDER BY u.nombre",
                "%" + direccion + "%");
    }

    @Override
    public Cliente buscarPorId(int idCliente) throws Exception {
        List<Cliente> r = ejecutarConsulta("SELECT c.idCliente,u.nombre,u.apellidoPaterno,u.apellidoMaterno," +
                "u.ciudad,u.estatus,c.calleNumero,c.colonia,c.codigoPostal " +
                "FROM Cliente c INNER JOIN Usuario u ON c.idCliente=u.idUsuario WHERE c.idCliente=?",
                String.valueOf(idCliente));
        return r.isEmpty() ? null : r.get(0);
    }

    private List<Cliente> ejecutarConsulta(String sql, String param) throws Exception {
        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            if (param != null) {
                try { ps.setInt(1, Integer.parseInt(param)); }
                catch (NumberFormatException e) { ps.setString(1, param); }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdUsuario(rs.getInt("idCliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellidoPaterno(rs.getString("apellidoPaterno"));
                    c.setApellidoMaterno(rs.getString("apellidoMaterno"));
                    c.setCiudad(rs.getString("ciudad"));
                    c.setEstatus(rs.getInt("estatus"));
                    c.setCalleNumero(rs.getInt("calleNumero"));
                    c.setColonia(rs.getString("colonia"));
                    c.setCodigoPostal(rs.getInt("codigoPostal"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }
}