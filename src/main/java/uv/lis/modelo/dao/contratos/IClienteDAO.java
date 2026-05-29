package uv.lis.modelo.dao.contratos;

import uv.lis.modelo.dominio.Cliente;
import java.util.List;

public interface IClienteDAO {
    boolean registrar(Cliente cliente, String telefono, String email) throws Exception;
    boolean actualizar(Cliente cliente, String telefono, String email) throws Exception;
    boolean eliminarLogico(int idCliente, int idSesion) throws Exception;
    List<Cliente> buscarTodos() throws Exception;
    List<Cliente> buscarPorNombre(String nombre) throws Exception;
    List<Cliente> buscarPorTelefono(String telefono) throws Exception;
    List<Cliente> buscarPorDireccion(String direccion) throws Exception;
    Cliente buscarPorId(int idCliente) throws Exception;
}
