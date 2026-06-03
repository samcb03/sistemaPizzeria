package uv.lis.gui.productos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.modelo.dao.impl.ProductoDAO;
import uv.lis.modelo.dominio.Producto;

public class FormProductoController {

    @FXML private Label lblTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtRestricciones;
    @FXML private TextField txtPrecio, txtCantidad;
    @FXML private CheckBox  chkPreparado;
    @FXML private Label lblError;

    private Producto producto;
    private boolean editando = false;
    private final ProductoDAO dao = new ProductoDAO();

    public void setProducto(Producto p) {
        this.producto = p; this.editando = p != null;
        if (editando) {
            lblTitulo.setText("Editar Producto");
            txtNombre.setText(p.getNombre());
            txtDescripcion.setText(p.getDescripcion());
            txtRestricciones.setText(p.getRestricciones());
            txtPrecio.setText(String.valueOf(p.getPrecio()));
            txtCantidad.setText(String.valueOf(p.getCantidad()));
            chkPreparado.setSelected(p.getEsPreparado() == 1);
        }
    }

    @FXML private void onGuardar(ActionEvent e) {
        if (!validar()) 
            return;
        try {
            Producto p = editando ? producto : new Producto();
            p.setNombre(txtNombre.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setRestricciones(txtRestricciones.getText().trim());
            p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            p.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
            p.setEsPreparado(chkPreparado.isSelected() ? 1 : 0);

            if (editando) dao.actualizar(p);
            else dao.registrar(p);

            Alerta.info("Éxito", "Producto " + (editando ? "actualizado" : "registrado") + " correctamente.");
            ((Stage) txtNombre.getScene().getWindow()).close();
        } catch (Exception ex) { lblError.setText(ex.getMessage()); }
    }

    @FXML private void onCancelar(ActionEvent e) { 
        ((Stage) txtNombre.getScene().getWindow()).close(); 
    }

    private boolean validar() {
        if (txtNombre.getText().isBlank() || txtPrecio.getText().isBlank()) {
            lblError.setText("Nombre y precio son obligatorios."); return false;
        }
        try { Double.parseDouble(txtPrecio.getText().trim()); } catch (NumberFormatException ex) {
            lblError.setText("El precio debe ser un número."); return false;
        }
        try { Integer.parseInt(txtCantidad.getText().trim()); } catch (NumberFormatException ex) {
            lblError.setText("La cantidad debe ser un número entero."); return false;
        }
        lblError.setText(""); return true;
    }
}
