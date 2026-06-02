package uv.lis.modelo.dominio;

public class Usuario {
    private int idUsuario;
    private int estatus;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String ciudad;
<<<<<<< HEAD
    private int estatus;  
=======
>>>>>>> 2e7f050b85b1ba7b27058142506c4ae1b5821036
    private String telefono;
    private String email;
    private Rol rol;

<<<<<<< HEAD
    public Usuario() { }
=======
    public Usuario() {
>>>>>>> 2e7f050b85b1ba7b27058142506c4ae1b5821036

    }

    public int getIdUsuario() { 
        return idUsuario; 
    }

    public void setIdUsuario(int idUsuario) { 
        this.idUsuario = idUsuario; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getApellidoPaterno() { 
        return apellidoPaterno; 
    }
    
    public void setApellidoPaterno(String ap) { 
        this.apellidoPaterno = ap; 
    }

    public String getApellidoMaterno() { 
        return apellidoMaterno; 
    }

    public void setApellidoMaterno(String am) { 
        this.apellidoMaterno = am; 
    }

    public String getCiudad() { 
        return ciudad; 
    }

    public void setCiudad(String ciudad) { 
        this.ciudad = ciudad; 
    }

    public int getEstatus() { 
        return estatus; 
    }

    public void setEstatus(int estatus) { 
        this.estatus = estatus; 
    }

    public String getTelefono() { 
        return telefono; 
    }

    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }

    @Override public String toString() { 
        return getNombreCompleto(); 
    }
}
