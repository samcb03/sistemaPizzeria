package uv.lis.modelo.dominio;

public class Producto {

    private int idProducto;
    private int disponible;
    private int cantidad;
    private int esPreparado;
    private int esInsumo;
    private String nombre;
    private String descripcion;
    private String restricciones;
    private String foto;
    private double precio;

    public Producto() {

    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(String restricciones) {
        this.restricciones = restricciones;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getEsPreparado() {
        return esPreparado;
    }

    public void setEsPreparado(int esPreparado) {
        this.esPreparado = esPreparado;
    }

    public int getEsInsumo() {
        return esInsumo;
    }

    public void setEsInsumo(int esInsumo) {
        this.esInsumo = esInsumo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
