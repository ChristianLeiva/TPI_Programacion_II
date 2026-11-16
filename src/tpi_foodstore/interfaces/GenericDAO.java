package tpi_foodstore.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Christian Leiva
 */
public interface GenericDAO<T> {
    List<T> listarActivos() throws SQLException;
    Optional<T> buscarPorId(Long id) throws SQLException;
    boolean modificar(T entidad) throws SQLException;
    boolean bajaLogica(Long id) throws SQLException;    
    
}
