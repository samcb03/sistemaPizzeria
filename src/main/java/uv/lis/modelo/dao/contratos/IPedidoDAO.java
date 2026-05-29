package uv.lis.modelo.dao.contratos;

import uv.lis.modelo.dominio.BitacoraEstatus;
import uv.lis.modelo.dominio.DetallePedido;
import uv.lis.modelo.dominio.Pedido;
import java.time.LocalDate;
import java.util.List;

public interface IPedidoDAO {
    int crear(Pedido pedido) throws Exception;
    boolean actualizarDetalle(int idPedido, List<DetallePedido> detalles) throws Exception;
    boolean cambiarEstatus(int idPedido, String nuevoEstatus, int idEmpleado) throws Exception;
    List<Pedido> buscarTodos() throws Exception;
    List<Pedido> buscarPorCliente(int idCliente) throws Exception;
    List<Pedido> buscarPorFecha(LocalDate fecha) throws Exception;
    List<Pedido> buscarPorEstatus(String estatus) throws Exception;
    Pedido buscarPorId(int idPedido) throws Exception;
    List<DetallePedido> obtenerDetalle(int idPedido) throws Exception;
    List<BitacoraEstatus> obtenerBitacora(int idPedido) throws Exception;
}
