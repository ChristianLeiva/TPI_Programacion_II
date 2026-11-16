
package tpi_foodstore.service;

import java.sql.Connection;
import java.sql.SQLException;
import tpi_foodstore.Entities.DetallePedido;
import tpi_foodstore.Entities.Pedido;
import tpi_foodstore.config.ConexionDB;


import tpi_foodstore.interfaces.ProductoDAO;
/**
 *
 * @author Christian Leiva
 */
public class PedidoService {

    private ProductoDAO productoDAO;

    public PedidoService() {
        this.productoDAO = new ProductoDAO();
    }
    
    /**
     * Procesa un pedido: descuenta el stock en la base de datos de forma transaccional 
     * y valida que todo sea correcto antes de guardarlo en el sistema.
     */
    
    public void procesarPedido(Pedido pedido) throws Exception {
        try {
            // Recorremos los detalles del pedido para descontar el stock uno por uno
            for (tpi_foodstore.Entities.DetallePedido detalle : pedido.getDetalles()) {
                Long idProducto = detalle.getProducto().getId();
                int cantidad = detalle.getCantidad();

                // Llamamos al DAO que creamos para descontar el stock
                boolean exito = productoDAO.descontarStockPedido(tpi_foodstore.config.ConexionDB.obtenerConexion(), idProducto, cantidad);

                if (!exito) {
                    throw new Exception("No hay stock suficiente para el producto ID: " + idProducto);
                }
            }
            System.out.println("Stock descontado con exito en la base de datos!");
        } catch (Exception e) {
            throw new Exception("Error al procesar el pedido: " + e.getMessage());
        }
    }

    
}
