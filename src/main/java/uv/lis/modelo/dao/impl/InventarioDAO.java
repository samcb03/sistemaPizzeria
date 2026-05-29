package uv.lis.modelo.dao.impl;

import uv.lis.modelo.conexion.ConexionBD;
import uv.lis.modelo.dao.contratos.IInventarioDAO;
import uv.lis.modelo.dominio.InventarioDetalle;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO implements IInventarioDAO {

    private Connection getConn() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public boolean registrarConteo(int idEmpleado, List<InventarioDetalle> detalles) throws Exception {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            int idInventario;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Inventario (fecha,cantidadInventarioReal,Empleado_idUsuario) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, detalles.stream().mapToInt(InventarioDetalle::getCantidadReal).sum());
                ps.setInt(3, idEmpleado);
                ps.executeUpdate();
                try (ResultSet rk = ps.getGeneratedKeys()) { rk.next(); idInventario = rk.getInt(1); }
            }
            for (InventarioDetalle d : detalles) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO InventarioTieneProducto (Inventario_idInventario,Producto_idProducto,cantidadEnSistema,descripcion) VALUES (?,?,?,?)")) {
                    ps.setInt(1, idInventario);
                    ps.setInt(2, d.getIdProducto());
                    ps.setInt(3, d.getCantidadSistema());
                    ps.setString(4, d.getDescripcion());
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error al registrar conteo: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public List<InventarioDetalle> obtenerUltimoReporte() throws Exception {
        List<InventarioDetalle> lista = new ArrayList<>();
        String sql = "SELECT * FROM v_reporte_inventario " +
                     "WHERE idInventario = (SELECT MAX(idInventario) FROM Inventario)";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InventarioDetalle d = new InventarioDetalle();
                d.setIdInventario(rs.getInt("idInventario"));
                d.setNombreProducto(rs.getString("producto"));
                d.setCantidadSistema(rs.getInt("cantidadEnSistema"));
                d.setCantidadReal(rs.getInt("cantidadReal"));
                d.setDiferencia(rs.getInt("diferencia"));
                d.setResultado(rs.getString("resultado"));
                d.setDescripcion(rs.getString("descripcion"));
                lista.add(d);
            }
        }
        return lista;
    }
}
