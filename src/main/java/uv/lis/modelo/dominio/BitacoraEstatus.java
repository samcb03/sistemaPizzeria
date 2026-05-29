package uv.lis.modelo.dominio;

import java.time.LocalDateTime;

public class BitacoraEstatus {
    private int idPedido;
    private String estatus;
    private LocalDateTime fechaHora;
    private String nombreCliente;

    public BitacoraEstatus() {}

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime f) { this.fechaHora = f; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String n) { this.nombreCliente = n; }
}
