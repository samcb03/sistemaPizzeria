package uv.lis.gui.pedidos;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.gui.util.Sesion;
import uv.lis.modelo.dao.impl.PedidoDAO;
import uv.lis.modelo.dominio.Pedido;

public class CambiarEstatusController {

    @FXML private Label             lblPedidoInfo;
    @FXML private ComboBox<String>  cbNuevoEstatus;
    @FXML private Label             lblError;

    private Pedido          pedido;
    private final PedidoDAO dao = new PedidoDAO();

    @FXML public void initialize() {
        cbNuevoEstatus.setItems(FXCollections.observableArrayList("En proceso","Entregado","Cancelado"));
    }

    public void setPedido(Pedido p) {
        this.pedido = p;
        lblPedidoInfo.setText("Pedido #" + p.getIdPedido() + "  —  " + p.getNombreCliente() +
                              "\nEstatus actual: " + p.getNombreEstatus());
        cbNuevoEstatus.setValue(p.getNombreEstatus());
    }

    @FXML private void onGuardar(ActionEvent e) {
        String estatusElegido = cbNuevoEstatus.getValue();
        
        if (estatusElegido == null) { 
            lblError.setText("Selecciona un estatus."); 
            return; 
        }
        
        if (estatusElegido.equals(pedido.getNombreEstatus())) {
            lblError.setText("El pedido ya se encuentra en ese estatus.");
            return;
        }

        if (!Alerta.confirmar("Confirmar", "¿Cambiar estatus a \"" + estatusElegido + "\"?")) {
            return; 
        }
        
        try {
            int idEmpleado = Sesion.getInstance().getEmpleadoActual().getIdUsuario();
            dao.cambiarEstatus(pedido.getIdPedido(), estatusElegido, idEmpleado);

            Alerta.info("Éxito", "Estatus actualizado a \"" + estatusElegido + "\" correctamente.");
            ((Stage) cbNuevoEstatus.getScene().getWindow()).close();
            
        } catch (Exception ex) { 
            lblError.setText(ex.getMessage()); 
        }
    }

    @FXML private void onCancelar(ActionEvent e) { 
        ((Stage) cbNuevoEstatus.getScene().getWindow()).close(); 
    }
}