package uv.lis.modelo.dominio;

public class DetallePedido {
    private int idProducto;
    private int idPedido;
    private String nombreProducto;
    private double precioUnitario;
    private int cantidadProductos;
    private double subtotal;

    public DetallePedido() {}

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String n) { this.nombreProducto = n; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double p) { this.precioUnitario = p; }
    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int c) { this.cantidadProductos = c; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double s) { this.subtotal = s; }
}
