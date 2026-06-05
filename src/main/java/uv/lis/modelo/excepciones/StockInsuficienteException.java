package uv.lis.modelo.excepciones;

public class StockInsuficienteException extends PizzeriaException {

    private final String nombreProducto;
    private final int    disponible;
    private final int    solicitado;

    public StockInsuficienteException(String nombreProducto, int disponible, int solicitado) {
        super(String.format(
            "Stock insuficiente para \"%s\": disponible=%d, solicitado=%d.",
            nombreProducto, disponible, solicitado));
        this.nombreProducto = nombreProducto;
        this.disponible     = disponible;
        this.solicitado     = solicitado;
    }

    public String getNombreProducto() { return nombreProducto; }
    public int    getDisponible()     { return disponible;     }
    public int    getSolicitado()     { return solicitado;     }
}