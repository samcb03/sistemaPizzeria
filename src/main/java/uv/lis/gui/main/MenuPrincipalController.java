<<<<<<< HEAD
package uv.lis.gui.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.gui.util.Sesion;

public class MenuPrincipalController {

    @FXML private BorderPane rootPane;
    @FXML private Label lblUsuario;
    @FXML private Label lblRol;
    @FXML private MenuItem miUsuarios;
    @FXML private MenuItem miCerrarSesion;

    @FXML
    public void initialize() {
        var emp = Sesion.getInstance().getEmpleadoActual();
        if (emp != null) {
            lblUsuario.setText(emp.getNombreCompleto());
            lblRol.setText(emp.getRol().getNombreRol());
        }
        // Si es cajero, ocultar administración
        if (!Sesion.getInstance().esAdministrador()) {
            miUsuarios.setDisable(true);
        }
        // Cargar dashboard por defecto
        cargarVista("/uv/lis/gui/pedidos/Pedidos.fxml");
    }

    @FXML private void onUsuarios()  { cargarVista("/uv/lis/gui/usuarios/Usuarios.fxml"); }
    @FXML private void onProductos() { cargarVista("/uv/lis/gui/productos/Productos.fxml"); }
    @FXML private void onPedidos()   { cargarVista("/uv/lis/gui/pedidos/Pedidos.fxml"); }
    @FXML private void onInventario(){ cargarVista("/uv/lis/gui/inventario/Inventario.fxml"); }
    @FXML private void onValidarInv(){ cargarVista("/uv/lis/gui/inventario/ValidarInventario.fxml"); }

    @FXML
    private void onAcercaDe() {
        Alerta.info("Acerca de Italia Pizza",
            "Sistema de Administración Italia Pizza v1.0\n\n" +
            "Equipo de Desarrollo:\n[Nombres del equipo]\n\n" +
            "Ingeniería de Software • 4° Semestre\n" +
            "Facultad de Estadística e Informática\n" +
            "Universidad Veracruzana • 2025");
    }

    @FXML
    private void onCerrarSesion() {
        if (Alerta.confirmar("Cerrar Sesión", "¿Deseas cerrar la sesión actual?")) {
            Sesion.getInstance().cerrar();
            try {
                Parent root = FXMLLoader.load(
                    getClass().getResource("/uv/lis/gui/login/Login.fxml"));
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setMaximized(false);
                stage.setResizable(false);
                stage.setWidth(900);
                stage.setHeight(580);
                stage.centerOnScreen();
            } catch (Exception e) {
                Alerta.error("Error", "No se pudo volver al login: " + e.getMessage());
            }
        }
    }

    private void cargarVista(String fxmlPath) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(fxmlPath));
            rootPane.setCenter(vista);
        } catch (Exception e) {
            Alerta.error("Error de carga", "No se pudo cargar la vista:\n" + e.getMessage());
        }
    }
}
=======
package uv.lis.gui.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.gui.util.Sesion;

public class MenuPrincipalController {

    @FXML private BorderPane rootPane;
    @FXML private Label lblUsuario;
    @FXML private Label lblRol;
    @FXML private MenuItem miUsuarios;
    @FXML private MenuItem miCerrarSesion;

    @FXML
    public void initialize() {
        var emp = Sesion.getInstance().getEmpleadoActual();
        if (emp != null) {
            lblUsuario.setText(emp.getNombreCompleto());
            lblRol.setText(emp.getRol().getNombreRol());
        }
        // Si es cajero, ocultar administración
        if (!Sesion.getInstance().esAdministrador()) {
            miUsuarios.setDisable(true);
        }
        // Cargar dashboard por defecto
        cargarVista("/uv/lis/gui/pedidos/Pedidos.fxml");
    }

    @FXML private void onUsuarios()  { cargarVista("/uv/lis/gui/usuarios/Usuarios.fxml"); }
    @FXML private void onProductos() { cargarVista("/uv/lis/gui/productos/Productos.fxml"); }
    @FXML private void onPedidos()   { cargarVista("/uv/lis/gui/pedidos/Pedidos.fxml"); }
    @FXML private void onInventario(){ cargarVista("/uv/lis/gui/inventario/Inventario.fxml"); }
    @FXML private void onValidarInv(){ cargarVista("/uv/lis/gui/inventario/ValidarInventario.fxml"); }

    @FXML
    private void onAcercaDe() {
        Alerta.info("Acerca de Italia Pizza",
            "Sistema de Administración Italia Pizza v1.0\n\n" +
            "Equipo de Desarrollo:\n[Nombres del equipo]\n\n" +
            "Ingeniería de Software • 4° Semestre\n" +
            "Facultad de Estadística e Informática\n" +
            "Universidad Veracruzana • 2025");
    }

    @FXML
    private void onCerrarSesion() {
        if (Alerta.confirmar("Cerrar Sesión", "¿Deseas cerrar la sesión actual?")) {
            Sesion.getInstance().cerrar();
            try {
                Parent root = FXMLLoader.load(
                    getClass().getResource("/uv/lis/gui/login/Login.fxml"));
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setMaximized(false);
                stage.setResizable(false);
                stage.setWidth(900);
                stage.setHeight(580);
                stage.centerOnScreen();
            } catch (Exception e) {
                Alerta.error("Error", "No se pudo volver al login: " + e.getMessage());
            }
        }
    }

    private void cargarVista(String fxmlPath) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(fxmlPath));
            rootPane.setCenter(vista);
        } catch (Exception e) {
            Alerta.error("Error de carga", "No se pudo cargar la vista:\n" + e.getMessage());
        }
    }
}
>>>>>>> 2e7f050b85b1ba7b27058142506c4ae1b5821036
