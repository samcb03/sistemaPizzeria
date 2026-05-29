package uv.lis.gui.util;

import uv.lis.modelo.dominio.Pedido;
import uv.lis.modelo.dominio.Producto;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * CsvExporter — exportación a CSV para pedidos e inventario.
 */
public class CsvExporter {

    public static void exportarPedidos(List<Pedido> pedidos, String rutaArchivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            pw.println("ID,Cliente,Empleado,Estatus,Tipo,Metodo Pago,Fecha,Total");
            for (Pedido p : pedidos) {
                pw.printf("%d,%s,%s,%s,%s,%s,%s,%.2f%n",
                        p.getIdPedido(),
                        escapeCsv(p.getNombreCliente()),
                        escapeCsv(p.getNombreEmpleado()),
                        escapeCsv(p.getNombreEstatus()),
                        escapeCsv(p.getNombreTipoPedido()),
                        escapeCsv(p.getNombreMetodo()),
                        p.getFechaHoraPedido() != null ? p.getFechaHoraPedido().toString() : "",
                        p.getTotal());
            }
        }
    }

    public static void exportarProductos(List<Producto> productos, String rutaArchivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            pw.println("ID,Nombre,Descripcion,Precio,Cantidad,Disponible");
            for (Producto p : productos) {
                pw.printf("%d,%s,%s,%.2f,%d,%s%n",
                        p.getIdProducto(),
                        escapeCsv(p.getNombre()),
                        escapeCsv(p.getDescripcion()),
                        p.getPrecio(),
                        p.getCantidad(),
                        p.getDisponible() == 1 ? "Activo" : "Inactivo");
            }
        }
    }

    private static String escapeCsv(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n"))
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        return valor;
    }
}
