package uv.lis.modelo.dao.contratos;

import uv.lis.modelo.dominio.Empleado;
import java.util.List;

public interface IEmpleadoDAO {
    Empleado autenticar(String username, String contrasena) throws Exception;
    boolean registrar(Empleado empleado, String telefono, String email) throws Exception;
    boolean actualizar(Empleado empleado, String telefono, String email) throws Exception;
    boolean eliminarLogico(int idEmpleado, int idSesion) throws Exception;
    List<Empleado> buscarTodos() throws Exception;
    List<Empleado> buscarPorNombre(String nombre) throws Exception;
}
