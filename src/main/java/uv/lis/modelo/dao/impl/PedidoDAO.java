package uv.lis.modelo.dao.impl;

import uv.lis.modelo.conexion.ConexionBD;
import uv.lis.modelo.dao.contratos.IPedidoDAO;
import uv.lis.modelo.dominio.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements IPedidoDAO {

    private Connection getConn() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public int crear(Pedido pedido) throws Exception {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            int idEstatus;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT idTipoEstatus FROM TipoEstatus WHERE nombreEstatus='En proceso' LIMIT 1"); ResultSet rs = ps.executeQuery()) {
                rs.next();
                idEstatus = rs.getInt(1);
            }
            String sqlP = "INSERT INTO Pedido (fechaHoraPedido,estatusActual,Cliente_idCliente,"
                    + "Empleado_idEmpleado,MetodoPago_idMetodo,TipoPedido_idTipoPedido,TipoEstatus_idTipoEstatus) "
                    + "VALUES (NOW(),?,?,?,?,?,?)";
            int idPedido;
            try (PreparedStatement ps = conn.prepareStatement(sqlP, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idEstatus);
                ps.setInt(2, pedido.getIdCliente());
                ps.setInt(3, pedido.getIdEmpleado());
                ps.setInt(4, pedido.getIdMetodoPago());
                ps.setInt(5, pedido.getIdTipoPedido());
                ps.setInt(6, idEstatus);
                ps.executeUpdate();
                try (ResultSet rk = ps.getGeneratedKeys()) {
                    rk.next();
                    idPedido = rk.getInt(1);
                }
            }
            for (DetallePedido d : pedido.getDetalles()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO DetallePedido (Producto_idProducto,Pedido_idPedido,cantidadProductos,subtotal) VALUES (?,?,?,?)")) {
                    ps.setInt(1, d.getIdProducto());
                    ps.setInt(2, idPedido);
                    ps.setInt(3, d.getCantidadProductos());
                    ps.setDouble(4, d.getSubtotal());
                    ps.executeUpdate();
                }
            }
            int idBitacora;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT idEstatus FROM BitacoraEstatus WHERE nombreEstatus='Pedido creado' LIMIT 1"); ResultSet rs = ps.executeQuery()) {
                rs.next();
                idBitacora = rs.getInt(1);
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO PedidoTieneBitacoraEstatus (Pedido_idPedido,BitacoraEstatus_idEstatus,Empleado_idEmpleado,fechaHora) VALUES (?,?,?,NOW())")) {
                ps.setInt(1, idPedido);
                ps.setInt(2, idBitacora);
                ps.setInt(3, pedido.getIdEmpleado());
                ps.executeUpdate();
            }
            conn.commit();
            return idPedido;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error al crear pedido: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean actualizarDetalle(int idPedido, List<DetallePedido> detalles) throws Exception {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM DetallePedido WHERE Pedido_idPedido=?")) {
                ps.setInt(1, idPedido);
                ps.executeUpdate();
            }
            for (DetallePedido d : detalles) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO DetallePedido (Producto_idProducto,Pedido_idPedido,cantidadProductos,subtotal) VALUES (?,?,?,?)")) {
                    ps.setInt(1, d.getIdProducto());
                    ps.setInt(2, idPedido);
                    ps.setInt(3, d.getCantidadProductos());
                    ps.setDouble(4, d.getSubtotal());
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw new Exception("Error al actualizar detalle: " + e.getMessage(), e);
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean cambiarEstatus(int idPedido, String nuevoEstatus, int idEmpleado) throws Exception {
        String llamada = "{ CALL sp_cambiar_estatus_pedido(?, ?, ?, ?) }";
        try (CallableStatement cs = getConn().prepareCall(llamada)) {
            cs.setInt(1, idPedido);
            cs.setString(2, nuevoEstatus);
            cs.setInt(3, idEmpleado);
            cs.registerOutParameter(4, Types.VARCHAR);
            cs.execute();

            String mensaje = cs.getString(4);
            String m = (mensaje == null) ? "" : mensaje.toLowerCase();
            if (m.isEmpty() || m.contains("error") || m.contains("no encontrado")) {
                throw new Exception(mensaje != null ? mensaje
                        : "No se pudo cambiar el estatus del pedido.");
            }
            return true;
        } catch (SQLException e) {
            throw new Exception("Error al cambiar estatus: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pedido> buscarTodos() throws Exception {
        return ejecutarBusqueda("SELECT * FROM v_pedidos_completos ORDER BY fechaHoraPedido DESC", null, null);
    }

    @Override
    public List<Pedido> buscarPorCliente(int idCliente) throws Exception {
        String sql = "SELECT p.idPedido,p.fechaHoraPedido,fn_nombre_completo(c.idCliente) AS nombreCliente,"
                + "te.nombreEstatus,tp.nombreTipo,mp.metodo,fn_nombre_completo(e.idEmpleado) AS nombreEmpleado,"
                + "fn_total_pedido(p.idPedido) AS totalPedido "
                + "FROM Pedido p INNER JOIN Cliente c ON p.Cliente_idCliente=c.idCliente "
                + "INNER JOIN TipoEstatus te ON p.TipoEstatus_idTipoEstatus=te.idTipoEstatus "
                + "INNER JOIN TipoPedido tp ON p.TipoPedido_idTipoPedido=tp.idTipoPedido "
                + "INNER JOIN MetodoPago mp ON p.MetodoPago_idMetodo=mp.idMetodo "
                + "INNER JOIN Empleado e ON p.Empleado_idEmpleado=e.idEmpleado "
                + "WHERE p.Cliente_idCliente=? ORDER BY p.fechaHoraPedido DESC";
        return ejecutarBusqueda(sql, idCliente, null);
    }

    @Override
    public List<Pedido> buscarPorFecha(LocalDate fecha) throws Exception {
        String sql = "SELECT * FROM v_pedidos_completos WHERE DATE(fechaHoraPedido)=? ORDER BY fechaHoraPedido DESC";
        return ejecutarBusqueda(sql, null, fecha.toString());
    }

    @Override
    public List<Pedido> buscarPorEstatus(String estatus) throws Exception {
        String sql = "SELECT * FROM v_pedidos_completos WHERE estatusActual=? ORDER BY fechaHoraPedido DESC";
        return ejecutarBusqueda(sql, null, estatus);
    }

    @Override
    public Pedido buscarPorId(int idPedido) throws Exception {
        String sql = "SELECT * FROM v_pedidos_completos WHERE idPedido=?";
        List<Pedido> r = ejecutarBusqueda(sql, idPedido, null);
        return r.isEmpty() ? null : r.get(0);
    }

    @Override
    public List<DetallePedido> obtenerDetalle(int idPedido) throws Exception {
        List<DetallePedido> lista = new ArrayList<>();
        String sql = "SELECT dp.Producto_idProducto,dp.Pedido_idPedido,pr.nombre,"
                + "pr.precio,dp.cantidadProductos,dp.subtotal "
                + "FROM DetallePedido dp INNER JOIN Producto pr ON dp.Producto_idProducto=pr.idProducto "
                + "WHERE dp.Pedido_idPedido=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setIdProducto(rs.getInt("Producto_idProducto"));
                    d.setIdPedido(rs.getInt("Pedido_idPedido"));
                    d.setNombreProducto(rs.getString("nombre"));
                    d.setPrecioUnitario(rs.getDouble("precio"));
                    d.setCantidadProductos(rs.getInt("cantidadProductos"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    lista.add(d);
                }
            }
        }
        return lista;
    }

    @Override
    public List<BitacoraEstatus> obtenerBitacora(int idPedido) throws Exception {
        List<BitacoraEstatus> lista = new ArrayList<>();
        String sql = "SELECT * FROM v_bitacora_pedidos WHERE idPedido=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BitacoraEstatus b = new BitacoraEstatus();
                    b.setIdPedido(rs.getInt("idPedido"));
                    b.setEstatus(rs.getString("estatus"));
                    b.setFechaHora(rs.getTimestamp("fechaHora").toLocalDateTime());
                    b.setNombreCliente(rs.getString("nombreCliente"));
                    b.setNombreEmpleado(rs.getString("nombreEmpleado"));
                    lista.add(b);
                }
            }
        }
        return lista;
    }

    @Override
    public List<Pedido> reportePedidos(Integer idCliente, LocalDate fecha, String estatus) throws Exception {

        List<Pedido> lista = new ArrayList<>();
        String llamada = "{ CALL sp_reporte_pedidos(?, ?, ?) }";
        try (CallableStatement cs = getConn().prepareCall(llamada)) {
            if (idCliente != null) {
                cs.setInt(1, idCliente);
            } else {
                cs.setNull(1, Types.INTEGER);
            }
            if (fecha != null) {
                cs.setDate(2, Date.valueOf(fecha));
            } else {
                cs.setNull(2, Types.DATE);
            }
            if (estatus != null) {
                cs.setString(3, estatus);
            } else {
                cs.setNull(3, Types.VARCHAR);
            }
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapPedidoReporte(rs));
                }
            }
        }
        return lista;
    }

    private Pedido mapPedidoReporte(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("idPedido"));
        p.setFechaHoraPedido(rs.getTimestamp("fechaHoraPedido").toLocalDateTime());
        p.setNombreCliente(rs.getString("cliente"));
        p.setNombreEmpleado(rs.getString("empleado"));
        p.setNombreEstatus(rs.getString("estatus"));
        p.setNombreTipoPedido(rs.getString("tipoPedido"));
        p.setNombreMetodo(rs.getString("metodoPago"));
        p.setTotal(rs.getDouble("total"));
        return p;
    }

    private List<Pedido> ejecutarBusqueda(String sql, Integer intParam, String strParam) throws Exception {
        List<Pedido> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            if (intParam != null) {
                ps.setInt(1, intParam);
            } else if (strParam != null) {
                ps.setString(1, strParam);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setIdPedido(rs.getInt("idPedido"));
                    p.setFechaHoraPedido(rs.getTimestamp("fechaHoraPedido").toLocalDateTime());
                    p.setNombreCliente(rs.getString("nombreCliente"));
                    p.setNombreEstatus(rs.getString("estatusActual") != null
                            ? rs.getString("estatusActual") : rs.getString("nombreEstatus"));
                    p.setNombreTipoPedido(rs.getString("tipoPedido") != null
                            ? rs.getString("tipoPedido") : rs.getString("nombreTipoPedido"));
                    p.setNombreMetodo(rs.getString("metodoPago") != null
                            ? rs.getString("metodoPago") : rs.getString("metodo"));
                    p.setNombreEmpleado(rs.getString("empleadoAsignado") != null
                            ? rs.getString("empleadoAsignado") : rs.getString("nombreEmpleado"));
                    try {
                        p.setTotal(rs.getDouble("totalPedido"));
                    } catch (SQLException ignored) {
                    }
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
