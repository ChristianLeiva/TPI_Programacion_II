package tpi_foodstore.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tpi_foodstore.Entities.Categoria;
import tpi_foodstore.config.ConexionDB;
import tpi_foodstore.interfaces.GenericDAO;




/**
 *
 * @author Christian Leiva
 */
public class CategoriaDAO implements GenericDAO<Categoria>{
    
    public Long insertar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, descripcion, eliminado, created_at) VALUES (?, ?, FALSE, CURRENT_TIMESTAMP)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se pudo insertar la categoría, ninguna fila afectada.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para la categoría.");
                }
            }
        }
    }
    
    // Validación de unicidad de nombre
    public boolean existeNombreActivo(String nombre) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categoria WHERE LOWER(TRIM(nombre)) = LOWER(TRIM(?)) AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
@Override
    public List<Categoria> listarActivos() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, eliminado, created_at FROM categoria WHERE eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getLong("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                cat.setEliminado(rs.getBoolean("eliminado"));
                cat.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                lista.add(cat);
            }
        }
        return lista;
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nombre, descripcion, eliminado, created_at FROM categoria WHERE id = ? AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setId(rs.getLong("id"));
                    cat.setNombre(rs.getString("nombre"));
                    cat.setDescripcion(rs.getString("descripcion"));
                    cat.setEliminado(rs.getBoolean("eliminado"));
                    cat.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(cat);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean modificar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ? AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setLong(3, categoria.getId());
            
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean bajaLogica(Long id) throws SQLException {
        String sql = "UPDATE categoria SET eliminado = TRUE WHERE id = ? AND eliminado = FALSE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            int filas = ps.executeUpdate();
            return filas == 1; // Valida que exactamente una fila haya sido afectada
        }
    }
    
}
