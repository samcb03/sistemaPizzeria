package uv.lis.modelo.dominio;

public class Cliente extends Usuario {
    private int calleNumero;
    private int codigoPostal;
    private String colonia;

    public Cliente() {

    }

    public int getCalleNumero() { 
        return calleNumero; 
    }

    public void setCalleNumero(int calleNumero) { 
        this.calleNumero = calleNumero; 
    }

    public String getColonia() { 
        return colonia; 
    }

    public void setColonia(String colonia) { 
        this.colonia = colonia; 
    }

    public int getCodigoPostal() { 
        return codigoPostal; 
    }

    public void setCodigoPostal(int codigoPostal) { 
        this.codigoPostal = codigoPostal; 
    }
}
