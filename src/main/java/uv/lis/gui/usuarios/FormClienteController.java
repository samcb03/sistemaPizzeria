package uv.lis.gui.usuarios;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.modelo.dao.impl.ClienteDAO;
import uv.lis.modelo.dominio.Cliente;
import uv.lis.modelo.excepciones.ValidacionException;

public class FormClienteController {

    @FXML private Label     lblTitulo;
    @FXML private TextField txtNombre, txtApellidoPat, txtApellidoMat, txtCiudad;
    @FXML private TextField txtTelefono, txtEmail, txtColonia;
    @FXML private TextField txtCalleNum, txtCP;
    @FXML private Label     lblError;

    private Cliente  cliente;
    private boolean  editando = false;
    private final ClienteDAO dao = new ClienteDAO();
    
    @FXML
    private void initialize() {
        StringBuilder faltantes = new StringBuilder();
        if (txtNombre      == null) faltantes.append("txtNombre ");
        if (txtApellidoPat == null) faltantes.append("txtApellidoPat ");
        if (txtApellidoMat == null) faltantes.append("txtApellidoMat ");
        if (txtCiudad      == null) faltantes.append("txtCiudad ");
        if (txtTelefono    == null) faltantes.append("txtTelefono ");
        if (txtEmail       == null) faltantes.append("txtEmail ");
        if (txtColonia     == null) faltantes.append("txtColonia ");
        if (txtCalleNum    == null) faltantes.append("txtCalleNum ");
        if (txtCP          == null) faltantes.append("txtCP ");
        if (faltantes.length() > 0)
            throw new IllegalStateException(
                "fx:id no inyectados (revisa el FXML): " + faltantes.toString().trim());
    }

    public void setCliente(Cliente cliente) {
        this.cliente  = cliente;
        this.editando = cliente != null;
        if (editando) {
            lblTitulo.setText("Editar Cliente");
            txtNombre.setText(cliente.getNombre());
            txtApellidoPat.setText(cliente.getApellidoPaterno());
            txtApellidoMat.setText(cliente.getApellidoMaterno());
            txtCiudad.setText(cliente.getCiudad());
            txtCalleNum.setText(String.valueOf(cliente.getCalleNumero()));
            txtColonia.setText(cliente.getColonia());
            txtCP.setText(String.valueOf(cliente.getCodigoPostal()));
            txtTelefono.setText(cliente.getTelefono() != null ? cliente.getTelefono() : "");
            txtEmail.setText(cliente.getEmail()       != null ? cliente.getEmail()    : "");
        }
    }

    @FXML
    private void onGuardar(ActionEvent event) {
        try {
            validar();
            Cliente c = editando ? cliente : new Cliente();
            c.setNombre(txtNombre.getText().trim());
            c.setApellidoPaterno(txtApellidoPat.getText().trim());
            c.setApellidoMaterno(txtApellidoMat.getText().trim());
            c.setCiudad(txtCiudad.getText().trim());
            c.setCalleNumero(Integer.parseInt(txtCalleNum.getText().trim()));
            c.setColonia(txtColonia.getText().trim());
            c.setCodigoPostal(Integer.parseInt(txtCP.getText().trim()));

            if (editando) dao.actualizar(c, txtTelefono.getText().trim(), txtEmail.getText().trim());
            else          dao.registrar(c, txtTelefono.getText().trim(), txtEmail.getText().trim());

            Alerta.info("Éxito", "Cliente " + (editando ? "actualizado" : "registrado") + " correctamente.");
            cerrar();
        } catch (ValidacionException ve) {
            lblError.setText(ve.getMessage());
            ve.printStackTrace();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelar(ActionEvent event) { cerrar(); }

    private void validar() throws ValidacionException {
        if (txtNombre.getText().isBlank())
            throw new ValidacionException("Nombre", "El nombre es obligatorio.");
        if (txtApellidoPat.getText().isBlank())
            throw new ValidacionException("Apellido Paterno", "El apellido paterno es obligatorio.");
        if (txtApellidoMat.getText().isBlank())
            throw new ValidacionException("Apellido Materno", "El apellido materno es obligatorio.");
        if (txtCiudad.getText().isBlank())
            throw new ValidacionException("Ciudad", "La ciudad es obligatoria.");
        if (txtColonia.getText().isBlank())
            throw new ValidacionException("Colonia", "La colonia es obligatoria.");
        try {
            Integer.parseInt(txtCalleNum.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Calle y Numero", "Calle y numero debe ser un valor numerico.");
        }
        try {
            Integer.parseInt(txtCP.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Codigo Postal", "Codigo postal debe ser numerico.");
        }
    }

    private void cerrar() {
        ((Stage) txtNombre.getScene().getWindow()).close();
    }
}