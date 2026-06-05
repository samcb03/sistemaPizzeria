package uv.lis.modelo.dao.contratos;

import uv.lis.modelo.dominio.Producto;
import java.util.List;

public interface IProductoDAO {
    
    boolean registrar(Producto producto) throws Exception;
    boolean actualizar(Producto producto) throws Exception;
    boolean eliminarLogico(int idProducto) throws Exception;
    List<Producto> buscarTodos() throws Exception;
    List<Producto> buscarPorNombre(String nombre) throws Exception;
    Producto buscarPorId(int idProducto) throws Exception;
    boolean estaEnPedidos(int idProducto) throws Exception;
    List<Producto> buscarDisponibles() throws Exception;
}
