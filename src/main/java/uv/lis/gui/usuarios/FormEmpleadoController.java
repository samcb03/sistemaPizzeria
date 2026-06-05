package uv.lis.gui.usuarios;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.modelo.dao.impl.CatalogoDAO;
import uv.lis.modelo.dao.impl.EmpleadoDAO;
import uv.lis.modelo.dominio.Empleado;
import uv.lis.modelo.dominio.Rol;

import java.util.List;

public class FormEmpleadoController {

    @FXML
    private Label lblTitulo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidoPat;
    @FXML
    private TextField txtApellidoMat;
    @FXML
    private TextField txtCiudad;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPass;
    @FXML
    private ComboBox<Rol> cbRol;
    @FXML
    private Label lblError;
    private Empleado empleado;
    private boolean editando = false;
    
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final CatalogoDAO catalogoDAO = new CatalogoDAO();

    @FXML
    public void initialize() {
        try {
            List<Rol> roles = catalogoDAO.obtenerRoles();
            cbRol.setItems(FXCollections.observableArrayList(roles));
            if (!roles.isEmpty()) {
                cbRol.setValue(roles.get(0));
            }
        } catch (Exception e) {
            Alerta.error("Error", "No se cargaron los roles: " + e.getMessage());
        }
    }

    public void setEmpleado(Empleado e) {
        this.empleado = e;
        this.editando = e != null;
        if (editando) {
            lblTitulo.setText("Editar Empleado");
            txtNombre.setText(e.getNombre());
            txtApellidoPat.setText(e.getApellidoPaterno());
            txtApellidoMat.setText(e.getApellidoMaterno());
            txtCiudad.setText(e.getCiudad());
            txtUsername.setText(e.getUsername());
            txtPass.setPromptText("Dejar vacio para no cambiar");
            if (e.getRol() != null) {
                cbRol.setValue(e.getRol());
            }
        }
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        if (!validar()) {
            return;
        }
        try {
            Empleado emp = editando ? empleado : new Empleado();
            emp.setNombre(txtNombre.getText().trim());
            emp.setApellidoPaterno(txtApellidoPat.getText().trim());
            emp.setApellidoMaterno(txtApellidoMat.getText().trim());
            emp.setCiudad(txtCiudad.getText().trim());
            emp.setUsername(txtUsername.getText().trim());
            emp.setRol(cbRol.getValue());
            if (!txtPass.getText().isBlank()) {
                emp.setContrasena(txtPass.getText());
            }

            if (editando) {
                empleadoDAO.actualizar(emp, txtTelefono.getText().trim(), txtEmail.getText().trim());
            } else {
                empleadoDAO.registrar(emp, txtTelefono.getText().trim(), txtEmail.getText().trim());
            }

            Alerta.info("Exito", "Empleado " + (editando ? "actualizado" : "registrado") + " correctamente.");
            cerrar();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
        }
    }

    @FXML
    private void onCancelar(ActionEvent event) {
        cerrar();
    }

    private boolean validar() {
        if (txtNombre.getText().isBlank() || txtApellidoPat.getText().isBlank()
                || txtUsername.getText().isBlank() || cbRol.getValue() == null) {
            lblError.setText("Todos los campos marcados con * son obligatorios.");
            return false;
        }
        if (!editando && txtPass.getText().isBlank()) {
            lblError.setText("La contraseña es obligatoria para un nuevo empleado.");
            return false;
        }
        lblError.setText("");
        return true;
    }

    private void cerrar() {
        ((Stage) txtNombre.getScene().getWindow()).close();
    }
}
