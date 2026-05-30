package uv.lis.modelo.dominio;

import java.time.LocalDateTime;

public class BitacoraEstatus {
    private String nombreCliente;
    private String estatus;
    private int idPedido;
    private LocalDateTime fechaHora;

    public BitacoraEstatus() {

    }

    public int getIdPedido() { 
        return idPedido; 
    }

    public void setIdPedido(int idPedido) { 
        this.idPedido = idPedido; 
    }

    public String getEstatus() { 
        return estatus; 
    }

    public void setEstatus(String estatus) { 
        this.estatus = estatus; 
    }

    public LocalDateTime getFechaHora() { 
        return fechaHora; 
    }

    public void setFechaHora(LocalDateTime fechaHora) { 
        this.fechaHora = fechaHora; 
    }

    public String getNombreCliente() { 
        return nombreCliente; 
    }

    public void setNombreCliente(String nombreCliente) { 
        this.nombreCliente = nombreCliente; 
    }
}
