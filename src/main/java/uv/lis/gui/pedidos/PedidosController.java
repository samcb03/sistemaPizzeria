package uv.lis.gui.pedidos;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.gui.util.CsvExporter;
import uv.lis.modelo.dao.impl.PedidoDAO;
import uv.lis.modelo.dominio.Pedido;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PedidosController {

    @FXML
    private TableView<Pedido> tblPedidos;
    @FXML
    private TableColumn<Pedido, Integer> colId;
    @FXML
    private TableColumn<Pedido, String> colCliente, colEstatus, colTipo, colMetodo;
    @FXML
    private TableColumn<Pedido, Double> colTotal;
    @FXML
    private TextField txtBuscarCliente;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<String> cbEstatus;

    private final PedidoDAO dao = new PedidoDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("nombreEstatus"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("nombreTipoPedido"));
        colMetodo.setCellValueFactory(new PropertyValueFactory<>("nombreMetodo"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        colEstatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item);
                setStyle(switch (item) {
                    case "En proceso" ->
                        "-fx-text-fill: #F47F23; -fx-font-weight: bold;";
                    case "Entregado" ->
                        "-fx-text-fill: #16A66E; -fx-font-weight: bold;";
                    case "Cancelado" ->
                        "-fx-text-fill: #C82429; -fx-font-weight: bold;";
                    default ->
                        "";
                });
            }
        });

        cbEstatus.setItems(FXCollections.observableArrayList("Todos", "En proceso", "Entregado", "Cancelado"));
        cbEstatus.setValue("Todos");
        cargar();
    }

    private void cargar() {
        try {
            tblPedidos.setItems(FXCollections.observableArrayList(dao.buscarTodos()));
        } catch (Exception e) {
            Alerta.error("Error", e.getMessage());
        }
    }

    @FXML
    private void onBuscar(ActionEvent e) {
        try {
            List<Pedido> res;
            LocalDate fecha = dpFecha.getValue();
            String estatus = cbEstatus.getValue();
            String clienteStr = txtBuscarCliente.getText() == null ? "" : txtBuscarCliente.getText().trim().toLowerCase();

            // Busqueda en Base de datos
            if (fecha != null) {
                res = dao.buscarPorFecha(fecha);
            } else if (estatus != null && !estatus.equals("Todos")) {
                res = dao.buscarPorEstatus(estatus);
            } else {
                res = dao.buscarTodos();
            }

            // Filtro por nombre del cliente
            if (!clienteStr.isEmpty()) {
                res = res.stream()
                        .filter(p -> p.getNombreCliente() != null
                        && p.getNombreCliente().toLowerCase().contains(clienteStr))
                        .collect(Collectors.toList());
            }

            tblPedidos.setItems(FXCollections.observableArrayList(res));

        } catch (Exception ex) {
            Alerta.error("Error", ex.getMessage());
        }
    }

    @FXML
    private void onLimpiar(ActionEvent e) {
        dpFecha.setValue(null);
        cbEstatus.setValue("Todos");
        txtBuscarCliente.setText(""); // Ahora también limpia la caja de texto
        cargar();
    }

    @FXML
    private void onNuevoPedido(ActionEvent e) {
        abrirDetalle(null);
    }

    @FXML
    private void onVerDetalle(ActionEvent e) {
        Pedido sel = tblPedidos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerta.advertencia("Seleccion", "Selecciona un pedido.");
            return;
        }
        abrirDetalle(sel);
    }

    @FXML
    private void onCambiarEstatus(ActionEvent e) {
        Pedido sel = tblPedidos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerta.advertencia("Seleccion", "Selecciona un pedido.");
            return;
        }
        abrirCambioEstatus(sel);
    }

    @FXML
    private void onExportarCSV(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Exportar pedidos");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fc.setInitialFileName("pedidos.csv");
        File archivo = fc.showSaveDialog(tblPedidos.getScene().getWindow());
        if (archivo == null) {
            return;
        }
        try {
            CsvExporter.exportarPedidos(tblPedidos.getItems(), archivo.getAbsolutePath());
            Alerta.info("Exito", "Pedidos exportados correctamente.");
        } catch (Exception ex) {
            Alerta.error("Error al exportar", ex.getMessage());
        }
    }

    private void abrirDetalle(Pedido pedido) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/uv/lis/gui/pedidos/DetallePedido.fxml"));
            Parent root = loader.load();
            DetallePedidoController ctrl = loader.getController();
            ctrl.setPedido(pedido);
            Stage dlg = new Stage();
            dlg.initModality(Modality.APPLICATION_MODAL);
            dlg.setTitle(pedido == null ? "Nuevo Pedido" : "Detalle Pedido #" + pedido.getIdPedido());
            dlg.setScene(new Scene(root));
            dlg.setResizable(false);
            dlg.showAndWait();
            cargar();
        } catch (Exception ex) {
            Alerta.error("Error", ex.getMessage());
        }
    }

    private void abrirCambioEstatus(Pedido pedido) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/uv/lis/gui/pedidos/CambiarEstatus.fxml"));
            Parent root = loader.load();
            CambiarEstatusController ctrl = loader.getController();
            ctrl.setPedido(pedido);
            Stage dlg = new Stage();
            dlg.initModality(Modality.APPLICATION_MODAL);
            dlg.setTitle("Cambiar Estatus — Pedido #" + pedido.getIdPedido());
            dlg.setScene(new Scene(root));
            dlg.setResizable(false);
            dlg.showAndWait();
            cargar();
        } catch (Exception ex) {
            Alerta.error("Error", ex.getMessage());
        }
    }
}
