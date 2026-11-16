
package tpi_foodstore;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import tpi_foodstore.config.ConexionDB;

/**
 *
 * @author Christian Leiva
 */
public class TestConexion {
    public static void main(String[] args) {
        System.out.println("Probando conexion a la base de datos...");

        try (Connection conn = ConexionDB.obtenerConexion()) {
            if (conn != null) {
                System.out.println("CONEXION EXITOSA!");
                
                // Extraemos metadata para ver qué motor y versión estamos usando
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Base de datos: " + meta.getDatabaseProductName());
                System.out.println("Version: " + meta.getDatabaseProductVersion());
                System.out.println("URL en uso: " + ConexionDB.getUrl());
            } else {
                System.out.println("La conexión devolvió null.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR DE CONEXIÓN:");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Código de error: " + e.getErrorCode());
            System.err.println("SQLState: " + e.getSQLState());
        }
    }
}