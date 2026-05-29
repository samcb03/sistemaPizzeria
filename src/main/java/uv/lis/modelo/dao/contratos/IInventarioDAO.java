package uv.lis.modelo.dao.contratos;

import uv.lis.modelo.dominio.InventarioDetalle;
import java.util.List;

public interface IInventarioDAO {
    boolean registrarConteo(int idEmpleado, List<InventarioDetalle> detalles) throws Exception;
    List<InventarioDetalle> obtenerUltimoReporte() throws Exception;
}
