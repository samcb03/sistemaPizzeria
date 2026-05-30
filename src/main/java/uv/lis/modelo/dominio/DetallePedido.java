package uv.lis.modelo.dominio;

public class DetallePedido {
    private int idProducto;
    private int idPedido;
    private int cantidadProductos;
    private double subtotal;
    private double precioUnitario;
    private String nombreProducto;

    public DetallePedido() {

    }

    public int getIdProducto() { 
        return idProducto; 
    }

    public void setIdProducto(int idProducto) { 
        this.idProducto = idProducto; 
    }

    public int getIdPedido() { 
        return idPedido; 
    }

    public void setIdPedido(int idPedido) { 
        this.idPedido = idPedido; 
    }
    
    public String getNombreProducto() { 
        return nombreProducto; 
    }

    public void setNombreProducto(String nombreProducto) { 
        this.nombreProducto = nombreProducto; 
    }

    public double getPrecioUnitario() { 
        return precioUnitario; 
    }

    public void setPrecioUnitario(double precioUnitario) { 
        this.precioUnitario = precioUnitario; 
    }

    public int getCantidadProductos() { 
        return cantidadProductos; 
    }

    public void setCantidadProductos(int cantidadProductos) { 
        this.cantidadProductos = cantidadProductos; 
    }

    public double getSubtotal() { 
        return subtotal; 
    }

    public void setSubtotal(double subtotal) { 
        this.subtotal = subtotal; 
    }
}
