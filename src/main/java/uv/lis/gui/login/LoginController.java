package uv.lis.gui.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.gui.util.Sesion;
import uv.lis.modelo.dao.impl.EmpleadoDAO;
import uv.lis.modelo.dominio.Empleado;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Label lblError;
    @FXML
    private Button btnIngresar;

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @FXML
    public void initialize() {
        lblError.setVisible(false);
    }

    @FXML
    private void onIngresar(ActionEvent event) {
        String username = txtUsuario.getText().trim();
        String password = txtContrasena.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Usuario y contraseña son obligatorios.");
            return;
        }

        try {
            Empleado empleado = empleadoDAO.autenticar(username, password);
            if (empleado == null) {
                mostrarError("Usuario o contraseña incorrectos.");
                txtContrasena.clear();
                return;
            }
            Sesion.getInstance().setEmpleadoActual(empleado);
            abrirMenuPrincipal();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error completo en la consola

            Alerta.error("Error de conexión", "No se pudo conectar con la base de datos.\n" + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    private void abrirMenuPrincipal() throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/uv/lis/gui/main/MenuPrincipal.fxml"));

        Stage stage = (Stage) btnIngresar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setTitle("Italia Pizza — Panel de Administración");
    }

}