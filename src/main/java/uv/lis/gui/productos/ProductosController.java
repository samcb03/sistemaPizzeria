package uv.lis.gui.productos;

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
import uv.lis.modelo.dao.impl.ProductoDAO;
import uv.lis.modelo.dominio.Producto;
// Importamos la excepción personalizada
import uv.lis.modelo.excepciones.ValidacionException; 

import java.io.File;
import java.util.List;

public class ProductosController {

    @FXML private TableView<Producto> tblProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre, colDescripcion;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colCantidad, colDisponible;
    @FXML private TextField txtBuscar;

    private final ProductoDAO dao = new ProductoDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        colDisponible.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                if (item == 0) {
                    setText("Inactivo");
                    setStyle("-fx-text-fill: #C82429;");
                } else if (getTableView().getItems().get(getIndex()).getCantidad() <= 0) {
                    setText("Sin stock");
                    setStyle("-fx-text-fill: #F47F23;");
                } else {
                    setText("Disponible");
                    setStyle("-fx-text-fill: #16A66E;");
                }
            }
        });
        cargar();
    }

    private void cargar() {
        try {
            tblProductos.setItems(FXCollections.observableArrayList(dao.buscarTodos()));
        } catch (Exception e) {
            Alerta.error("Error", e.getMessage());
        }
    }

    @FXML
    private void onBuscar(ActionEvent e) {
        String txt = txtBuscar.getText().trim();
        try {
            List<Producto> res = txt.isEmpty() ? dao.buscarTodos() : dao.buscarPorNombre(txt);
            tblProductos.setItems(FXCollections.observableArrayList(res));
        } catch (Exception ex) {
            Alerta.error("Error", ex.getMessage());
        }
    }

    @FXML
    private void onNuevo(ActionEvent e) {
        abrirForm(null);
    }

    @FXML
    private void onEditar(ActionEvent e) {
        Producto sel = tblProductos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerta.advertencia("Seleccion", "Selecciona un producto para editar.");
            return;
        }
        abrirForm(sel);
    }

    @FXML
    private void onEliminar(ActionEvent e) {
        Producto sel = tblProductos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerta.advertencia("Seleccion", "Selecciona un producto.");
            return;
        }
        if (!Alerta.confirmar("Eliminar Producto", "¿Deseas desactivar el producto \"" + sel.getNombre() + "\"?")) {
            return;
        }
        try {
            dao.eliminarLogico(sel.getIdProducto());
            Alerta.info("Exito", "Producto desactivado.");
            cargar();
        } catch (ValidacionException ve) {
            Alerta.error("Error de Regla de Negocio", ve.getMessage());
        } catch (Exception ex) {
            Alerta.error("Error", ex.getMessage());
        }
    }

    @FXML
    private void onExportarCSV(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar inventario CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fc.setInitialFileName("inventario_productos.csv");
        File archivo = fc.showSaveDialog(tblProductos.getScene().getWindow());
        if (archivo == null) return;

        try {
            CsvExporter.exportarProductos(tblProductos.getItems(), archivo.getAbsolutePath());
            Alerta.info("Exito", "Inventario exportado a:\n" + archivo.getAbsolutePath());
        } catch (Exception ex) {
            Alerta.error("Error al exportar", ex.getMessage());
        }
    }

    private void abrirForm(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uv/lis/gui/productos/FormProducto.fxml"));
            Parent root = loader.load();
            FormProductoController ctrl = loader.getController();
            ctrl.setProducto(producto);
            Stage dlg = new Stage();
            dlg.initModality(Modality.APPLICATION_MODAL);
            dlg.setTitle(producto == null ? "Nuevo Producto" : "Editar Producto");
            dlg.setScene(new Scene(root));
            dlg.setResizable(false);
            dlg.showAndWait();
            cargar();
        } catch (Exception ex) {
            Alerta.error("Error", ex.getMessage());
        }
    }
}