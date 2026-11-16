package tpi_foodstore.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import tpi_foodstore.enumerations.Estado;
import tpi_foodstore.enumerations.FormaPago;
import tpi_foodstore.interfaces.Calculable;

/**
 *
 * @author Christian Leiva
 */
public class Pedido extends Base implements Calculable{
    
    private LocalDate fecha;
    private Estado estado;
    private double total;
    private FormaPago formaPago;
    
     private List<DetallePedido> detalles;

    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    public Pedido(Long id, tpi_foodstore.enumerations.FormaPago formaPago) {
        super();
        this.id = id;
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.formaPago = formaPago;
        this.detalles = new ArrayList<>();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
    
    public List<DetallePedido> getDetalles() {
    return detalles;
}
    
    public void addDetallePedido(int cantidad, Double subtotal, Producto producto) {
        DetallePedido detalle = new DetallePedido();
        detalle.setId(System.currentTimeMillis() + (long)(Math.random() * 1000));
        detalle.setCantidad(cantidad);
        detalle.setSubtotal(subtotal);
        detalle.setProducto(producto);
        detalle.setEliminado(false);
        
        this.detalles.add(detalle);
        calcularTotal(); // Recalcula el total automáticamente
    }
    
    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado() && detalle.getProducto().equals(producto)) {
                return detalle;
            }
        }
        return null;
    }
    
    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalle.setEliminado(true);
            calcularTotal();
        }
    }

    @Override
    public String toString() {
        return "Pedido [id=" + id + ", fecha=" + fecha + ", estado=" + estado + ", formaPago=" + formaPago + ", total=" + total + "]";
    }  

    @Override
    public void calcularTotal() {
           double suma = 0.0;
           for (DetallePedido detalle : detalles) {
               if (!detalle.isEliminado()) {
                   suma += detalle.getSubtotal();
               }
           }
           this.total = suma;
       }
}
