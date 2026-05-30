package uv.lis.modelo.dominio;


public class TipoPedido {
    private int idTipoPedido;
    private String nombreTipo;

    public TipoPedido() {

    }
    public TipoPedido(int idTipoPedido, String nombrePedido) { 
        this.idTipoPedido = idTipoPedido; 
        this.nombreTipo = nombrePedido; 
    }

    public int getIdTipoPedido() { 
        return idTipoPedido; 
    }

    public void setIdTipoPedido(int idTipoPedido) { 
        this.idTipoPedido = idTipoPedido; 
    }

    public String getNombreTipo() { 
        return nombreTipo; 
    }

    public void setNombreTipo(String nombreTipo) { 
        this.nombreTipo = nombreTipo; 
    }

    @Override public String toString() { 
        return nombreTipo; 
    }
}
