package uv.lis.modelo.dao.impl;

import uv.lis.modelo.conexion.ConexionBD;
import uv.lis.modelo.dao.contratos.IProductoDAO;
import uv.lis.modelo.dominio.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IProductoDAO {

    private Connection getConn() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean registrar(Producto p) throws Exception {
        String sql = "INSERT INTO Producto (nombre,descripcion,restricciones,disponible,precio,cantidad,foto,esPreparado,esInsumo) " +
                     "VALUES (?,?,?,1,?,?,?,?,?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getRestricciones());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getCantidad());
            ps.setString(6, p.getFoto());
            ps.setInt(7, p.getEsPreparado());
            ps.setInt(8, p.getEsInsumo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("Error al registrar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Producto p) throws Exception {
        String sql = "UPDATE Producto SET nombre=?,descripcion=?,restricciones=?,precio=?,cantidad=?,foto=?,esPreparado=?,esInsumo=? " +
                     "WHERE idProducto=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getRestricciones());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getCantidad());
            ps.setString(6, p.getFoto());
            ps.setInt(7, p.getEsPreparado());
            ps.setInt(8, p.getEsInsumo());
            ps.setInt(9, p.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new Exception("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarLogico(int idProducto) throws Exception {
        if (estaEnPedidos(idProducto))
            throw new Exception("El producto ya ha sido utilizado en pedidos y no puede eliminarse.");
        try (PreparedStatement ps = getConn().prepareStatement(
                "UPDATE Producto SET disponible = 0 WHERE idProducto = ?")) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean estaEnPedidos(int idProducto) throws Exception {
        try (PreparedStatement ps = getConn().prepareStatement(
                "SELECT fn_producto_en_pedidos(?) AS usado")) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("usado") == 1;
            }
        }
    }

    @Override
    public List<Producto> buscarTodos() throws Exception {
        return consulta("SELECT * FROM Producto ORDER BY nombre", null);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) throws Exception {
        return consulta("SELECT * FROM Producto WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY nombre",
                "%" + nombre + "%");
    }

    @Override
    public Producto buscarPorId(int idProducto) throws Exception {
        List<Producto> r = consulta("SELECT * FROM Producto WHERE idProducto=?",
                String.valueOf(idProducto));
        return r.isEmpty() ? null : r.get(0);
    }

    private List<Producto> consulta(String sql, String param) throws Exception {
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            if (param != null) {
                try { ps.setInt(1, Integer.parseInt(param)); }
                catch (NumberFormatException e) { ps.setString(1, param); }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapProducto(rs));
            }
        }
        return lista;
    }

    private Producto mapProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("idProducto"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setRestricciones(rs.getString("restricciones"));
        p.setDisponible(rs.getInt("disponible"));
        p.setPrecio(rs.getDouble("precio"));
        p.setCantidad(rs.getInt("cantidad"));
        p.setFoto(rs.getString("foto"));
        p.setEsPreparado(rs.getInt("esPreparado"));
        p.setEsInsumo(rs.getInt("esInsumo"));
        return p;
    }
}
