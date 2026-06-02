package uv.lis.gui.usuarios;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.gui.util.Sesion;
import uv.lis.modelo.dao.impl.ClienteDAO;
import uv.lis.modelo.dao.impl.EmpleadoDAO;
import uv.lis.modelo.dominio.Cliente;
import uv.lis.modelo.dominio.Empleado;

import java.util.ArrayList;
import java.util.List;

//NOTA
public class UsuariosController {

    // ── Tabla Clientes ──
    @FXML private TableView<Cliente>             tblClientes;
    @FXML private TableColumn<Cliente,Integer>   colCIdUsuario;
    @FXML private TableColumn<Cliente,String>    colCNombre, colCCiudad;
    @FXML private TableColumn<Cliente,Integer>   colCEstatus;

    // ── Tabla Empleados ──
    @FXML private TableView<Empleado>             tblEmpleados;
    @FXML private TableColumn<Empleado,Integer>  colEIdUsuario;
    @FXML private TableColumn<Empleado,String>   colENombre, colEUsername, colERol;
    @FXML private TableColumn<Empleado,Integer>  colEEstatus;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbBuscarPor;
    @FXML private TabPane tabPane;

    private final ClienteDAO  clienteDAO  = new ClienteDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @FXML
    public void initialize() {
        configurarTablaClientes();
        configurarTablaEmpleados();
        cbBuscarPor.setItems(FXCollections.observableArrayList("Nombre","Teléfono","Dirección"));
        cbBuscarPor.setValue("Nombre");
        cargarClientes();
        cargarEmpleados();
    }

    private void configurarTablaClientes() {
        colCIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colCNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colCEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        colCEstatus.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item == 1 ? "Activo" : "Inactivo");
                    setStyle(item == 1 ? "-fx-text-fill: #16A66E;" : "-fx-text-fill: #C82429;");
                }
            }
        });
    }

    private void configurarTablaEmpleados() {
        colEIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colENombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colEUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colERol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setText(null); return; }
                Empleado emp = getTableView().getItems().get(getIndex());
                setText(emp.getRol() != null ? emp.getRol().getNombreRol() : "");
            }
        });
        colEEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        colEEstatus.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item == 1 ? "Activo" : "Inactivo");
                    setStyle(item == 1 ? "-fx-text-fill: #16A66E;" : "-fx-text-fill: #C82429;");
                }
            }
        });
    }

    private void cargarClientes() {
        try {
            tblClientes.setItems(FXCollections.observableArrayList(clienteDAO.buscarTodos()));
        } catch (Exception e) {
            Alerta.error("Error", "No se pudieron cargar los clientes: " + e.getMessage());
        }
    }

    private void cargarEmpleados() {
        try {
            tblEmpleados.setItems(FXCollections.observableArrayList(empleadoDAO.buscarTodos()));
        } catch (Exception e) {
            Alerta.error("Error", "No se pudieron cargar los empleados: " + e.getMessage());
        }
    }

    @FXML
    private void onBuscar(ActionEvent event) {
        String texto = txtBuscar.getText().trim();
        String criterio = cbBuscarPor.getValue();
        boolean esClientes = tabPane.getSelectionModel().getSelectedIndex() == 0;
        try {
            if (esClientes) {
                List<Cliente> res = switch (criterio) {
                    case "Teléfono"   -> clienteDAO.buscarPorTelefono(texto);
                    case "Dirección"  -> clienteDAO.buscarPorDireccion(texto);
                    default           -> texto.isEmpty() ? clienteDAO.buscarTodos() : clienteDAO.buscarPorNombre(texto);
                };
                tblClientes.setItems(FXCollections.observableArrayList(res));
            } else {
                List<Empleado> res = texto.isEmpty()
                    ? empleadoDAO.buscarTodos()
                    : empleadoDAO.buscarPorNombre(texto);
                tblEmpleados.setItems(FXCollections.observableArrayList(res));
            }
        } catch (Exception e) {
            Alerta.error("Error", e.getMessage());
        }
    }

    @FXML
    private void onNuevoCliente(ActionEvent event) {
        abrirFormulario(null, true);
    }

    @FXML
    private void onNuevoEmpleado(ActionEvent event) {
        abrirFormulario(null, false);
    }

    @FXML
    private void onEditarCliente(ActionEvent event) {
        Cliente sel = tblClientes.getSelectionModel().getSelectedItem();
        if (sel == null) { Alerta.advertencia("Selección", "Selecciona un cliente para editar."); return; }
        abrirFormulario(sel, true);
    }

    @FXML
    private void onEditarEmpleado(ActionEvent event) {
        Empleado sel = tblEmpleados.getSelectionModel().getSelectedItem();
        if (sel == null) { Alerta.advertencia("Selección", "Selecciona un empleado para editar."); return; }
        abrirFormulario(sel, false);
    }

    @FXML
    private void onEliminarCliente(ActionEvent event) {
        Cliente sel = tblClientes.getSelectionModel().getSelectedItem();
        if (sel == null) { Alerta.advertencia("Selección", "Selecciona un cliente para eliminar."); return; }
        if (!Alerta.confirmar("Confirmar Eliminación",
                "¿Deseas desactivar al cliente \"" + sel.getNombreCompleto() + "\"?\n" +
                "La cuenta quedará inactiva pero sus datos se conservarán.")) return;
        try {
            clienteDAO.eliminarLogico(sel.getIdUsuario(), Sesion.getInstance().getEmpleadoActual().getIdUsuario());
            Alerta.info("Éxito", "Cliente desactivado correctamente.");
            cargarClientes();
        } catch (Exception e) {
            Alerta.error("No se pudo eliminar", e.getMessage());
        }
    }

    @FXML
    private void onEliminarEmpleado(ActionEvent event) {
        Empleado sel = tblEmpleados.getSelectionModel().getSelectedItem();
        if (sel == null) { Alerta.advertencia("Selección", "Selecciona un empleado para eliminar."); return; }
        if (!Alerta.confirmar("Confirmar Eliminación",
                "¿Deseas desactivar al empleado \"" + sel.getNombreCompleto() + "\"?")) return;
        try {
            empleadoDAO.eliminarLogico(sel.getIdUsuario(), Sesion.getInstance().getEmpleadoActual().getIdUsuario());
            Alerta.info("Éxito", "Empleado desactivado correctamente.");
            cargarEmpleados();
        } catch (Exception e) {
            Alerta.error("No se pudo eliminar", e.getMessage());
        }
    }

    private void abrirFormulario(Object usuario, boolean esCliente) {
        try {
            String fxml = esCliente
                ? "/uv/lis/gui/usuarios/FormCliente.fxml"
                : "/uv/lis/gui/usuarios/FormEmpleado.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            if (esCliente) {
                FormClienteController ctrl = loader.getController();
                ctrl.setCliente((Cliente) usuario);
            } else {
                FormEmpleadoController ctrl = loader.getController();
                ctrl.setEmpleado((Empleado) usuario);
            }
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(esCliente
                ? (usuario == null ? "Nuevo Cliente" : "Editar Cliente")
                : (usuario == null ? "Nuevo Empleado" : "Editar Empleado"));
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();
            if (esCliente) cargarClientes(); else cargarEmpleados();
        } catch (Exception e) {
            Alerta.error("Error", e.getMessage());
        }
    }
}