package uv.lis.modelo.dominio;

public class Cliente extends Usuario {
    private String calleNumero;
    private String colonia;
    private int codigoPostal;

    public Cliente() { }

    public Cliente(String calleNumero, String colonia, int codigoPostal) {
        this.calleNumero = calleNumero;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
    }

    public String getCalleNumero() {
        return calleNumero;
    }

    public void setCalleNumero(String calleNumero) {
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