package uv.lis.modelo.dominio;

public class MetodoPago {
    private int idMetodo;
    private String metodo;

    public MetodoPago() {}
    public MetodoPago(int id, String metodo) { this.idMetodo = id; this.metodo = metodo; }

    public int getIdMetodo() { return idMetodo; }
    public void setIdMetodo(int id) { this.idMetodo = id; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    @Override public String toString() { return metodo; }
}
