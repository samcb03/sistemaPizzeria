package uv.lis.modelo.dominio;

public class TipoPedido {
    private int idTipoPedido;
    private String nombreTipo;

    public TipoPedido() {}
    public TipoPedido(int id, String nombre) { this.idTipoPedido = id; this.nombreTipo = nombre; }

    public int getIdTipoPedido() { return idTipoPedido; }
    public void setIdTipoPedido(int id) { this.idTipoPedido = id; }
    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String n) { this.nombreTipo = n; }
    @Override public String toString() { return nombreTipo; }
}
