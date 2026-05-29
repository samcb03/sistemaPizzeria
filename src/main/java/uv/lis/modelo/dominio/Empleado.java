package uv.lis.modelo.dominio;

public class Empleado extends Usuario {
    private String username;
    private String contrasena;
    private Rol rol;

    public Empleado() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
