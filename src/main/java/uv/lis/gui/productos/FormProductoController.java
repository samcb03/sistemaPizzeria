package uv.lis.gui.productos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.modelo.dao.impl.ProductoDAO;
import uv.lis.modelo.dominio.Producto;

public class FormProductoController {

    @FXML
    private Label lblTitulo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtRestricciones;
    @FXML
    private TextField txtPrecio, txtCantidad;
    @FXML
    private CheckBox chkPreparado;
    @FXML
    private Label lblError;

    private Producto producto;
    private boolean editando = false;
    private final ProductoDAO dao = new ProductoDAO();

    public void setProducto(Producto p) {
        this.producto = p;
        this.editando = p != null;
        if (editando) {
            lblTitulo.setText("Editar Producto");

            txtNombre.setText(p.getNombre() == null ? "" : p.getNombre());
            txtDescripcion.setText(p.getDescripcion() == null ? "" : p.getDescripcion());
            txtRestricciones.setText(p.getRestricciones() == null ? "" : p.getRestricciones());
            txtPrecio.setText(String.valueOf(p.getPrecio()));
            txtCantidad.setText(String.valueOf(p.getCantidad()));
            chkPreparado.setSelected(p.getEsPreparado() == 1);
        }
    }

    @FXML
    private void onGuardar(ActionEvent e) {
        if (!validar()) {
            return;
        }

        try {
            Producto p = editando ? producto : new Producto();

            String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
            String desc = txtDescripcion.getText() == null ? "" : txtDescripcion.getText().trim();
            String rest = txtRestricciones.getText() == null ? "" : txtRestricciones.getText().trim();

            p.setNombre(nombre);
            p.setDescripcion(desc);
            p.setRestricciones(rest);

            p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            p.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
            p.setEsPreparado(chkPreparado.isSelected() ? 1 : 0);

            if (editando) {
                dao.actualizar(p);
            } else {
                dao.registrar(p);
            }

            Alerta.info("Exito", "Producto " + (editando ? "actualizado" : "registrado") + " correctamente.");
            ((Stage) txtNombre.getScene().getWindow()).close();

        } catch (Exception ex) {
            lblError.setText(ex.getMessage());
        }
    }

    @FXML
    private void onCancelar(ActionEvent e) {
        ((Stage) txtNombre.getScene().getWindow()).close();
    }

    private boolean validar() {
        
        String nom = txtNombre.getText() == null ? "" : txtNombre.getText();
        String pre = txtPrecio.getText() == null ? "" : txtPrecio.getText();
        String cant = txtCantidad.getText() == null ? "" : txtCantidad.getText();

        if (nom.isBlank() || pre.isBlank()) {
            lblError.setText("Nombre y precio son obligatorios.");
            return false;
        }
        try {
            Double.parseDouble(pre.trim());
        } catch (NumberFormatException ex) {
            lblError.setText("El precio debe ser un numero.");
            return false;
        }
        try {
            Integer.parseInt(cant.trim());
        } catch (NumberFormatException ex) {
            lblError.setText("La cantidad debe ser un numero entero.");
            return false;
        }

        lblError.setText("");
        return true;
    }
}
