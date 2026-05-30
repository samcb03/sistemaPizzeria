package uv.lis.modelo.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private int idEmpleado;
    private int idMetodoPago;
    private int idTipoPedido;
    private int idTipoEstatus;
    private String nombreEmpleado;
    private String nombreMetodo;
    private String nombreTipoPedido;
    private String nombreEstatus;
    private String nombreCliente;
    private double total;
    private LocalDateTime fechaHoraPedido;
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {}

    public int getIdPedido() { 
        return idPedido; 
    }

    public void setIdPedido(int idPedido) { 
        this.idPedido = idPedido; 
    }

    public LocalDateTime getFechaHoraPedido() { 
        return fechaHoraPedido; 
    }

    public void setFechaHoraPedido(LocalDateTime fechaHoraPedido) { 
        this.fechaHoraPedido = fechaHoraPedido; 
    }
    public int getIdCliente() { 
        return idCliente; 
    }

    public void setIdCliente(int idCliente) { 
        this.idCliente = idCliente; 
    }

    public String getNombreCliente() { 
        return nombreCliente; 
    }

    public void setNombreCliente(String nombreCliente) { 
        this.nombreCliente = nombreCliente; 
    }

    public int getIdEmpleado() { 
        return idEmpleado; 
    }

    public void setIdEmpleado(int idEmpleado) { 
        this.idEmpleado = idEmpleado; 
    }

    public String getNombreEmpleado() { 
        return nombreEmpleado; 
    }
    public void setNombreEmpleado(String nombreEmpleado) { 
        this.nombreEmpleado = nombreEmpleado; 
    }

    public int getIdMetodoPago() { 
        return idMetodoPago; 
    }

    public void setIdMetodoPago(int idMetodoPago) { 
        this.idMetodoPago = idMetodoPago; 
    }

    public String getNombreMetodo() { 
        return nombreMetodo; 
    }

    public void setNombreMetodo(String nombreMetodo) { 
        this.nombreMetodo = nombreMetodo; 
    }

    public int getIdTipoPedido() { 
        return idTipoPedido; 
    }

    public void setIdTipoPedido(int idTipoPedido) { 
        this.idTipoPedido = idTipoPedido; 
    }
    public String getNombreTipoPedido() { 
        return nombreTipoPedido; 
    }

    public void setNombreTipoPedido(String nombreTipoPedido) { 
        this.nombreTipoPedido = nombreTipoPedido; 
    }

    public int getIdTipoEstatus() { 
        return idTipoEstatus; 
    }

    public void setIdTipoEstatus(int idTipoEstatus) { 
        this.idTipoEstatus = idTipoEstatus; 
    }

    public String getNombreEstatus() { 
        return nombreEstatus; 
    }

    public void setNombreEstatus(String nombreEstatus) { 
        this.nombreEstatus = nombreEstatus; 
    }

    public double getTotal() { 
        return total; 
    }

    public void setTotal(double total) { 
        this.total = total; 
    }

    public List<DetallePedido> getDetalles() { 
        return detalles; 
    }

    public void setDetalles(List<DetallePedido> detalles) { 
        this.detalles = detalles; 
    }
}
