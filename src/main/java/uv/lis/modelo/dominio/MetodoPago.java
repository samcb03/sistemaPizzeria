package uv.lis.modelo.dominio;

public class MetodoPago {
    private int idMetodo;
    private String metodo;

    public MetodoPago() {

    }

    public MetodoPago(int idMetodo, String metodo) { 
        this.idMetodo = idMetodo; 
        this.metodo = metodo; 
    }

    public int getIdMetodo() { 
        return idMetodo; 
    }

    public void setIdMetodo(int idMetodo) { 
        this.idMetodo = idMetodo; 
    }

    public String getMetodo() { 
        return metodo; 
    }

    public void setMetodo(String metodo) { 
        this.metodo = metodo; 
    }

    @Override public String toString() { 
        return metodo; 
    }
}
