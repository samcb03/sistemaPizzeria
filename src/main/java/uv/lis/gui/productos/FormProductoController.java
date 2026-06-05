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
        String pre  = txtPrecio.getText()   == null ? "" : txtPrecio.getText().trim();
        String cant = txtCantidad.getText() == null ? "" : txtCantidad.getText().trim();

        if (txtNombre.getText() == null || txtNombre.getText().isBlank()) {
            lblError.setText("El nombre del producto es obligatorio."); return false;
        }
        if (pre.isBlank()) {
            lblError.setText("El precio es obligatorio."); return false;
        }
        double precio;
        try {
            precio = Double.parseDouble(pre);
        } catch (NumberFormatException ex) {
            lblError.setText("El precio debe ser un número."); return false;
        }
        if (precio < 0) {
            lblError.setText("El precio no puede ser negativo."); return false;
        }
        if (precio > 99999) {
            lblError.setText("El precio no puede superar 99999."); return false;
        }

        if (cant.isBlank()) {
            lblError.setText("La cantidad es obligatoria."); return false;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(cant);
        } catch (NumberFormatException ex) {
            lblError.setText("La cantidad debe ser un número entero."); return false;
        }
        if (cantidad < 0) {
            lblError.setText("La cantidad no puede ser negativa."); return false;
        }
        if (cantidad > 99999) {
            lblError.setText("La cantidad no puede superar 99999."); return false;
        }

        lblError.setText("");
        return true;
    }
}
