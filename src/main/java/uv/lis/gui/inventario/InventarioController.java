package uv.lis.gui.inventario;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import uv.lis.gui.util.Alerta;
import uv.lis.modelo.dao.impl.InventarioDAO;
import uv.lis.modelo.dominio.InventarioDetalle;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class InventarioController {
    @FXML private TableView<InventarioDetalle> tblInventario;
    @FXML private TableColumn<InventarioDetalle, String> colProducto;
    @FXML private TableColumn<InventarioDetalle, String> colResultado;
    @FXML private TableColumn<InventarioDetalle, Integer> colSistema;
    @FXML private TableColumn<InventarioDetalle, Integer> colReal;
    @FXML private TableColumn<InventarioDetalle, Integer> colDiferencia;
    private final InventarioDAO dao = new InventarioDAO();

    @FXML public void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colSistema.setCellValueFactory(new PropertyValueFactory<>("cantidadSistema"));
        colReal.setCellValueFactory(new PropertyValueFactory<>("cantidadReal"));
        colDiferencia.setCellValueFactory(new PropertyValueFactory<>("diferencia"));
        colResultado.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        colResultado.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                setStyle(switch (item) {
                    case "Faltante"  -> "-fx-text-fill: #C82429; -fx-font-weight: bold;";
                    case "Sobrante"  -> "-fx-text-fill: #F47F23; -fx-font-weight: bold;";
                    default          -> "-fx-text-fill: #16A66E; -fx-font-weight: bold;";
                });
            }
        });
        cargar();
    }

    private void cargar() {
        try { tblInventario.setItems(FXCollections.observableArrayList(dao.obtenerUltimoReporte())); }
        catch (Exception e) { Alerta.error("Error", e.getMessage()); }
    }

    @FXML private void onExportarCSV(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Exportar inventario");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV","*.csv"));
        fc.setInitialFileName("reporte_inventario.csv");
        File archivo = fc.showSaveDialog(tblInventario.getScene().getWindow());
        if (archivo == null) return;
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("Producto,Cant.Sistema,Cant.Real,Diferencia,Resultado");
            for (InventarioDetalle d : tblInventario.getItems()) {
                pw.printf("%s,%d,%d,%d,%s%n",
                    d.getNombreProducto(), d.getCantidadSistema(),
                    d.getCantidadReal(), d.getDiferencia(), d.getResultado());
            }
            Alerta.info("Éxito", "Inventario exportado correctamente.");
        } catch (Exception ex) { Alerta.error("Error al exportar", ex.getMessage()); }
    }
}
