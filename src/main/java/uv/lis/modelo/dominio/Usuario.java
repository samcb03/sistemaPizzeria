package uv.lis.modelo.dominio;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String ciudad;
    private int estatus;   // 1=Activo, 0=Inactivo
    private String telefono;
    private String email;
    private Rol rol;

    public Usuario() { }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String ap) { this.apellidoPaterno = ap; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String am) { this.apellidoMaterno = am; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public int getEstatus() { return estatus; }
    public void setEstatus(int estatus) { this.estatus = estatus; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
    @Override public String toString() { return getNombreCompleto(); }
}
