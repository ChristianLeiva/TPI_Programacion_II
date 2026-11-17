package tpi_foodstore.Entities;

/**
 *
 * @author Christian Leiva
 */
public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;
    private Producto producto;
    
    public DetallePedido() {
        super();
    }
    
    public DetallePedido(Long id, int cantidad, Double subtotal, Producto producto) {
        super();
        this.id = id;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    @Override
    public String toString() {
        return "Detalle de Pedido [id=" + id + ", cantidad=" + cantidad + ", subtotal=" + subtotal + ", producto=" + (producto != null ? producto.getNombre() : "null") + "]";
    }
}
