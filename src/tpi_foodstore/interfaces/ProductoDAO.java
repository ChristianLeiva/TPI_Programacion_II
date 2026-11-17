package tpi_foodstore.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tpi_foodstore.Entities.Producto;
import tpi_foodstore.config.ConexionDB;

/**
 *
 * @author Christian Leiva
 */
public class ProductoDAO implements GenericDAO<Producto> {

    // Alta de Producto
    public Long insertar(Producto producto, Long idCategoria) throws SQLException {
        String sql = "INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, eliminado, created_at, categoria_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, FALSE, CURRENT_TIMESTAMP, ?)";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getDescripcion());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagen());
            ps.setBoolean(6, producto.getDisponible());
            ps.setLong(7, idCategoria);
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar el producto.");
            }
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el producto.");
                }
            }
        }
    }
    
    public List<tpi_foodstore.Entities.Producto> listarTodos() throws SQLException {
    List<tpi_foodstore.Entities.Producto> lista = new ArrayList<>();
    String sql = "SELECT id, nombre, precio, descripcion, stock, imagen, disponible FROM producto WHERE eliminado = FALSE";

    try (Connection conn = tpi_foodstore.config.ConexionDB.obtenerConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            tpi_foodstore.Entities.Producto p = new tpi_foodstore.Entities.Producto();
            p.setId(rs.getLong("id"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio(rs.getDouble("precio"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setStock(rs.getInt("stock"));
            p.setImagen(rs.getString("imagen"));
            p.setDisponible(rs.getBoolean("disponible"));
            
            lista.add(p);
        }
    }
    return lista;
}

    public List<Producto> listarPorCategoria(Long idCategoria) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, descripcion, stock, imagen, disponible, eliminado, created_at " +
                     "FROM producto WHERE categoria_id = ? AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getLong("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setStock(rs.getInt("stock"));
                    p.setImagen(rs.getString("imagen")); 
                    p.setDisponible(rs.getBoolean("disponible"));
                    p.setEliminado(rs.getBoolean("eliminado"));
                    p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    public boolean reasignarCategoria(Long idProducto, Long idNuevaCategoria) throws SQLException {
        String sql = "UPDATE producto SET categoria_id = ? WHERE id = ? AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, idNuevaCategoria);
            ps.setLong(2, idProducto);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Producto> listarActivos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, descripcion, stock, imagen, disponible, eliminado, created_at FROM producto WHERE eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getLong("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStock(rs.getInt("stock"));
                p.setImagen(rs.getString("imagen"));
                p.setDisponible(rs.getBoolean("disponible"));
                p.setEliminado(rs.getBoolean("eliminado"));
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                lista.add(p);
            }
        }
        return lista;
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nombre, precio, descripcion, stock, imagen, disponible, eliminado, created_at FROM producto WHERE id = ? AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getLong("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setStock(rs.getInt("stock"));
                    p.setImagen(rs.getString("imagen"));
                    p.setDisponible(rs.getBoolean("disponible"));
                    p.setEliminado(rs.getBoolean("eliminado"));
                    p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(p);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean modificar(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, descripcion = ?, stock = ?, imagen = ?, disponible = ? WHERE id = ? AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getDescripcion());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagen());
            ps.setBoolean(6, producto.getDisponible());
            ps.setLong(7, producto.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Transacción para descontar stock al registrar un pedido 
    public boolean descontarStockPedido(Connection conn, Long idProducto, int cantidadComprada) throws SQLException {
        String sql = "UPDATE producto SET stock = stock - ? WHERE id = ? AND stock >= ? AND disponible = TRUE AND eliminado = FALSE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidadComprada);
            ps.setLong(2, idProducto);
            ps.setInt(3, cantidadComprada);

            int filasAfectadas = ps.executeUpdate();
            // Retorna true si afectó exactamente a 1 fila (significa que había stock y se descontó bien)
            return filasAfectadas == 1;
        }
    }
    
    @Override
    public boolean bajaLogica(Long id) throws SQLException {
        String sql = "UPDATE producto SET eliminado = TRUE WHERE id = ? AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        }
    }
    
}
