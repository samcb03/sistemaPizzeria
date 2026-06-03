package uv.lis.modelo.dominio;

public class InventarioDetalle {

    private int idInventario;
    private int idProducto;
    private int cantidadSistema;
    private int cantidadReal;
    private int diferencia;
    private String nombreProducto;
    private String resultado;
    private String descripcion;

    public InventarioDetalle() {

    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidadSistema() {
        return cantidadSistema;
    }

    public void setCantidadSistema(int cantidadSistema) {
        this.cantidadSistema = cantidadSistema;
    }

    public int getCantidadReal() {
        return cantidadReal;
    }

    public void setCantidadReal(int cantidadReal) {
        this.cantidadReal = cantidadReal;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
