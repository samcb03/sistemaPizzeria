package uv.lis.modelo.dominio;

public class TipoEstatus {
    private int idTipoEstatus;
    private String nombreEstatus;

    public TipoEstatus() {

    }
    public TipoEstatus(int idTipoEstatus, String nombreEstatus) { 
        this.idTipoEstatus = idTipoEstatus; 
        this.nombreEstatus = nombreEstatus; 
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
    
    @Override public String toString() { 
        return nombreEstatus; 
    }
}
