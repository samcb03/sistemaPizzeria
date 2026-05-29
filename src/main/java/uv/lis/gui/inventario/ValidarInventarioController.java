package uv.lis.gui.inventario;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uv.lis.gui.util.Alerta;
import uv.lis.gui.util.Sesion;
import uv.lis.modelo.dao.impl.InventarioDAO;
import uv.lis.modelo.dao.impl.ProductoDAO;
import uv.lis.modelo.dominio.InventarioDetalle;
import uv.lis.modelo.dominio.Producto;

import java.util.ArrayList;
import java.util.List;

public class ValidarInventarioController {

    @FXML private TableView<InventarioDetalle>           tblValidar;
    @FXML private TableColumn<InventarioDetalle,String>  colProducto;
    @FXML private TableColumn<InventarioDetalle,Integer> colSistema;

    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final ProductoDAO   productoDAO   = new ProductoDAO();
    private final List<InventarioDetalle> detalles = new ArrayList<>();

    @FXML public void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colSistema.setCellValueFactory(new PropertyValueFactory<>("cantidadSistema"));

        // Columna editable para cantidad real
        TableColumn<InventarioDetalle,Integer> colReal = new TableColumn<>("Cant. Real (capturar)");
        colReal.setPrefWidth(160);
        colReal.setCellValueFactory(new PropertyValueFactory<>("cantidadReal"));
        colReal.setCellFactory(col -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>(0, 9999, 0);
            { spinner.setEditable(true);
              spinner.valueProperty().addListener((obs, oldV, newV) -> {
                  if (getIndex() >= 0 && getIndex() < detalles.size())
                      detalles.get(getIndex()).setCantidadReal(newV);
              });
            }
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : spinner);
                if (!empty && item != null) spinner.getValueFactory().setValue(item);
            }
        });
        tblValidar.getColumns().add(colReal);
        cargarProductos();
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = productoDAO.buscarTodos();
            detalles.clear();
            for (Producto p : productos) {
                InventarioDetalle d = new InventarioDetalle();
                d.setIdProducto(p.getIdProducto());
                d.setNombreProducto(p.getNombre());
                d.setCantidadSistema(p.getCantidad());
                d.setCantidadReal(0);
                detalles.add(d);
            }
            tblValidar.setItems(FXCollections.observableArrayList(detalles));
        } catch (Exception e) { Alerta.error("Error", e.getMessage()); }
    }

    @FXML private void onRegistrar(ActionEvent e) {
        if (!Alerta.confirmar("Confirmar", "¿Registrar este conteo de inventario?")) return;
        try {
            int idEmp = Sesion.getInstance().getEmpleadoActual().getIdUsuario();
            inventarioDAO.registrarConteo(idEmp, detalles);
            Alerta.info("Éxito", "Conteo de inventario registrado correctamente.");
        } catch (Exception ex) { Alerta.error("Error", ex.getMessage()); }
    }
}
