package uv.lis.gui.pedidos;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import uv.lis.gui.util.Alerta;
import uv.lis.gui.util.Sesion;
import uv.lis.modelo.dao.impl.*;
import uv.lis.modelo.dominio.*;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.VBox;

public class DetallePedidoController {

    @FXML private Label lblPedidoId, lblCliente, lblEstatus, lblTotal;
    @FXML private TableView<DetallePedido>             tblDetalle;
    @FXML private TableColumn<DetallePedido,String>    colProducto;
    @FXML private TableColumn<DetallePedido,Integer>   colCantidad;
    @FXML private TableColumn<DetallePedido,Double>    colPrecio, colSubtotal;
    @FXML private TableView<BitacoraEstatus>           tblBitacora;
    @FXML private TableColumn<BitacoraEstatus,String>  colBEstatus, colBFecha;
    @FXML private ComboBox<Producto>                   cbProductoAgregar;
    @FXML private Spinner<Integer>                     spnCantidad;
    @FXML private ComboBox<Cliente>                    cbCliente;
    @FXML private ComboBox<MetodoPago>                 cbMetodoPago;
    @FXML private ComboBox<TipoPedido>                 cbTipoPedido;
    @FXML private VBox                                 pnlNuevoPedido;
    @FXML private VBox                                 pnlDetalle;

    private Pedido pedido;
    private boolean esNuevo;
    private final PedidoDAO    pedidoDAO    = new PedidoDAO();
    private final ProductoDAO  productoDAO  = new ProductoDAO();
    private final ClienteDAO   clienteDAO   = new ClienteDAO();
    private final CatalogoDAO  catalogoDAO  = new CatalogoDAO();
    private final List<DetallePedido> detallesTemp = new ArrayList<>();

    @FXML
    public void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadProductos"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colBEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        colBFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        spnCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,99,1));
    }

    public void setPedido(Pedido p) {
        this.pedido = p;
        this.esNuevo = (p == null);
        if (esNuevo) {
            pnlNuevoPedido.setVisible(true);
            pnlDetalle.setVisible(false);
            cargarCatalogosNuevo();
        } else {
            pnlNuevoPedido.setVisible(false);
            pnlDetalle.setVisible(true);
            lblPedidoId.setText("Pedido #" + p.getIdPedido());
            lblCliente.setText(p.getNombreCliente());
            lblEstatus.setText(p.getNombreEstatus());
            cargarDetalle();
            cargarBitacora();
            cargarProductosDisponibles();
        }
    }

    private void cargarCatalogosNuevo() {
        try {
            cbCliente.setItems(FXCollections.observableArrayList(clienteDAO.buscarTodos()));
            cbMetodoPago.setItems(FXCollections.observableArrayList(catalogoDAO.obtenerMetodosPago()));
            cbTipoPedido.setItems(FXCollections.observableArrayList(catalogoDAO.obtenerTiposPedido()));
            cargarProductosDisponibles();
        } catch (Exception e) { Alerta.error("Error", e.getMessage()); }
    }

    private void cargarProductosDisponibles() {
        try { cbProductoAgregar.setItems(FXCollections.observableArrayList(productoDAO.buscarTodos())); }
        catch (Exception e) { Alerta.error("Error", e.getMessage()); }
    }

    private void cargarDetalle() {
        try {
            List<DetallePedido> det = pedidoDAO.obtenerDetalle(pedido.getIdPedido());
            detallesTemp.clear(); detallesTemp.addAll(det);
            tblDetalle.setItems(FXCollections.observableArrayList(det));
            actualizarTotal();
        } catch (Exception e) { Alerta.error("Error", e.getMessage()); }
    }

    private void cargarBitacora() {
        try { tblBitacora.setItems(FXCollections.observableArrayList(pedidoDAO.obtenerBitacora(pedido.getIdPedido()))); }
        catch (Exception e) { Alerta.error("Error", e.getMessage()); }
    }

    @FXML private void onAgregarProducto(ActionEvent e) {
        Producto prod = cbProductoAgregar.getValue();
        if (prod == null) return;
        int cant = spnCantidad.getValue();
        // Verificar si ya está en la lista
        detallesTemp.stream()
            .filter(d -> d.getIdProducto() == prod.getIdProducto())
            .findFirst()
            .ifPresentOrElse(
                d -> d.setCantidadProductos(d.getCantidadProductos() + cant),
                () -> {
                    DetallePedido d = new DetallePedido();
                    d.setIdProducto(prod.getIdProducto());
                    d.setNombreProducto(prod.getNombre());
                    d.setPrecioUnitario(prod.getPrecio());
                    d.setCantidadProductos(cant);
                    d.setSubtotal(prod.getPrecio() * cant);
                    detallesTemp.add(d);
                });
        tblDetalle.setItems(FXCollections.observableArrayList(detallesTemp));
        actualizarTotal();
    }

    @FXML private void onQuitarProducto(ActionEvent e) {
        DetallePedido sel = tblDetalle.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        detallesTemp.remove(sel);
        tblDetalle.setItems(FXCollections.observableArrayList(detallesTemp));
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = detallesTemp.stream().mapToDouble(d -> d.getPrecioUnitario() * d.getCantidadProductos()).sum();
        if (lblTotal != null) lblTotal.setText(String.format("Total: $%.2f", total));
    }

@FXML private void onGuardar(ActionEvent e) {
        if (detallesTemp.isEmpty()) { 
            Alerta.advertencia("Vacío", "Agrega al menos un producto."); 
            return; 
        }
        
        try {
            if (esNuevo) {
                // 1. Validar que los campos obligatorios estén seleccionados
                if (cbCliente.getValue() == null || cbMetodoPago.getValue() == null || cbTipoPedido.getValue() == null) {
                    Alerta.advertencia("Datos incompletos", "Selecciona cliente, método de pago y tipo."); 
                    return;
                }
                
                // 2. Extraer los IDs de los objetos seleccionados
                int idCliente = cbCliente.getValue().getIdUsuario();
                int idEmpleado = Sesion.getInstance().getEmpleadoActual().getIdUsuario(); // Asegúrate que este sea el getter correcto
                int idMetodo = cbMetodoPago.getValue().getIdMetodo();
                int idTipoPedido = cbTipoPedido.getValue().getIdTipoPedido();
                
                // 3. Llamar al Procedimiento Almacenado en lugar de usar pedidoDAO.crear()
                int idNuevo = pedidoDAO.registrarPedido(idCliente, idEmpleado, idTipoPedido, idMetodo);
                
                // 4. Validar si falló la creación en BD
                if (idNuevo == -1) {
                    Alerta.error("Error", "No se pudo registrar el encabezado del pedido.");
                    return;
                }
                
                // 5. Insertar todos los productos del carrito (detalles)
                pedidoDAO.actualizarDetalle(idNuevo, detallesTemp);
                
                Alerta.info("Éxito", "Pedido #" + idNuevo + " creado correctamente.");
                
            } else {
                // Lógica intacta para cuando solo se edita un pedido existente
                pedidoDAO.actualizarDetalle(pedido.getIdPedido(), detallesTemp);
                Alerta.info("Éxito", "Pedido actualizado correctamente.");
            }
            
            // Cerrar la ventana al terminar
            ((Stage) tblDetalle.getScene().getWindow()).close();
            
        } catch (Exception ex) { 
            Alerta.error("Error", ex.getMessage()); 
        }
    }
    @FXML private void onCancelar(ActionEvent e) { 
        ((Stage) tblDetalle.getScene().getWindow()).close(); 
    }
}