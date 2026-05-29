package uv.lis.gui.usuarios;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.modelo.dao.impl.ClienteDAO;
import uv.lis.modelo.dominio.Cliente;

public class FormClienteController {

    @FXML private Label     lblTitulo;
    @FXML private TextField txtNombre, txtApPat, txtApMat, txtCiudad;
    @FXML private TextField txtTelefono, txtEmail, txtColonia;
    @FXML private TextField txtCalleNum, txtCP;
    @FXML private Label     lblError;

    private Cliente       cliente;
    private boolean       editando = false;
    private final ClienteDAO dao = new ClienteDAO();

    public void setCliente(Cliente c) {
        this.cliente  = c;
        this.editando = c != null;
        if (editando) {
            lblTitulo.setText("Editar Cliente");
            txtNombre.setText(c.getNombre());
            txtApPat.setText(c.getApellidoPaterno());
            txtApMat.setText(c.getApellidoMaterno());
            txtCiudad.setText(c.getCiudad());
            txtCalleNum.setText(String.valueOf(c.getCalleNumero()));
            txtColonia.setText(c.getColonia());
            txtCP.setText(String.valueOf(c.getCodigoPostal()));
        }
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        if (!validar()) return;
        try {
            Cliente c = editando ? cliente : new Cliente();
            c.setNombre(txtNombre.getText().trim());
            c.setApellidoPaterno(txtApPat.getText().trim());
            c.setApellidoMaterno(txtApMat.getText().trim());
            c.setCiudad(txtCiudad.getText().trim());
            c.setCalleNumero(Integer.parseInt(txtCalleNum.getText().trim()));
            c.setColonia(txtColonia.getText().trim());
            c.setCodigoPostal(Integer.parseInt(txtCP.getText().trim()));

            if (editando) dao.actualizar(c, txtTelefono.getText().trim(), txtEmail.getText().trim());
            else          dao.registrar(c, txtTelefono.getText().trim(), txtEmail.getText().trim());

            Alerta.info("Éxito", "Cliente " + (editando ? "actualizado" : "registrado") + " correctamente.");
            cerrar();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
        }
    }

    @FXML private void onCancelar(ActionEvent event) { cerrar(); }

    private boolean validar() {
        if (txtNombre.getText().isBlank() || txtApPat.getText().isBlank() ||
            txtCiudad.getText().isBlank() || txtColonia.getText().isBlank()) {
            lblError.setText("Todos los campos marcados con * son obligatorios.");
            return false;
        }
        try { Integer.parseInt(txtCalleNum.getText().trim()); } catch (NumberFormatException e) {
            lblError.setText("Calle y número debe ser un valor numérico."); return false;
        }
        try { Integer.parseInt(txtCP.getText().trim()); } catch (NumberFormatException e) {
            lblError.setText("Código postal debe ser numérico."); return false;
        }
        lblError.setText("");
        return true;
    }

    private void cerrar() {
        ((Stage) txtNombre.getScene().getWindow()).close();
    }
}
