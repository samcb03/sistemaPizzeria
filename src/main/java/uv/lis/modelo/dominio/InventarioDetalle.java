package uv.lis.modelo.dominio;

public class InventarioDetalle {
    private int idInventario;
    private int idProducto;
    private String nombreProducto;
    private int cantidadSistema;
    private int cantidadReal;
    private int diferencia;
    private String resultado;
    private String descripcion;

    public InventarioDetalle() {}

    public int getIdInventario() { return idInventario; }
    public void setIdInventario(int id) { this.idInventario = id; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int id) { this.idProducto = id; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String n) { this.nombreProducto = n; }
    public int getCantidadSistema() { return cantidadSistema; }
    public void setCantidadSistema(int c) { this.cantidadSistema = c; }
    public int getCantidadReal() { return cantidadReal; }
    public void setCantidadReal(int c) { this.cantidadReal = c; }
    public int getDiferencia() { return diferencia; }
    public void setDiferencia(int d) { this.diferencia = d; }
    public String getResultado() { return resultado; }
    public void setResultado(String r) { this.resultado = r; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String d) { this.descripcion = d; }
}
