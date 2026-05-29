package uv.lis.modelo.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private LocalDateTime fechaHoraPedido;
    private int idCliente;
    private String nombreCliente;
    private int idEmpleado;
    private String nombreEmpleado;
    private int idMetodoPago;
    private String nombreMetodo;
    private int idTipoPedido;
    private String nombreTipoPedido;
    private int idTipoEstatus;
    private String nombreEstatus;
    private double total;
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {}

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public LocalDateTime getFechaHoraPedido() { return fechaHoraPedido; }
    public void setFechaHoraPedido(LocalDateTime f) { this.fechaHoraPedido = f; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String n) { this.nombreCliente = n; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String n) { this.nombreEmpleado = n; }
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public String getNombreMetodo() { return nombreMetodo; }
    public void setNombreMetodo(String n) { this.nombreMetodo = n; }
    public int getIdTipoPedido() { return idTipoPedido; }
    public void setIdTipoPedido(int idTipoPedido) { this.idTipoPedido = idTipoPedido; }
    public String getNombreTipoPedido() { return nombreTipoPedido; }
    public void setNombreTipoPedido(String n) { this.nombreTipoPedido = n; }
    public int getIdTipoEstatus() { return idTipoEstatus; }
    public void setIdTipoEstatus(int idTipoEstatus) { this.idTipoEstatus = idTipoEstatus; }
    public String getNombreEstatus() { return nombreEstatus; }
    public void setNombreEstatus(String n) { this.nombreEstatus = n; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
