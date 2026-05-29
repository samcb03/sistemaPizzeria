package uv.lis.modelo.dominio;

public class TipoEstatus {
    private int idTipoEstatus;
    private String nombreEstatus;

    public TipoEstatus() {}
    public TipoEstatus(int id, String nombre) { this.idTipoEstatus = id; this.nombreEstatus = nombre; }

    public int getIdTipoEstatus() { return idTipoEstatus; }
    public void setIdTipoEstatus(int id) { this.idTipoEstatus = id; }
    public String getNombreEstatus() { return nombreEstatus; }
    public void setNombreEstatus(String n) { this.nombreEstatus = n; }
    @Override public String toString() { return nombreEstatus; }
}
