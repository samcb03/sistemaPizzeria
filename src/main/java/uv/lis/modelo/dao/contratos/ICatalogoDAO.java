package uv.lis.modelo.dao.contratos;

import uv.lis.modelo.dominio.*;
import java.util.List;

public interface ICatalogoDAO {

    List<Rol> obtenerRoles() throws Exception;
    List<MetodoPago> obtenerMetodosPago() throws Exception;
    List<TipoPedido> obtenerTiposPedido() throws Exception;
    List<TipoEstatus> obtenerTiposEstatus() throws Exception;
    
}
