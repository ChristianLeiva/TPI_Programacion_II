package tpi_foodstore.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Christian Leiva
 */
public class ConexionDB {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    static {
        cargarConfiguracion();
    }

    private static void cargarConfiguracion() {
        try (InputStream is = ConexionDB.class.getResourceAsStream("/META-INF/persistence.xml")) {
            if (is == null) {
                throw new RuntimeException("No se pudo encontrar el archivo persistence.xml en el classpath.");
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList properties = doc.getElementsByTagName("property");
            for (int i = 0; i < properties.getLength(); i++) {
                Element prop = (Element) properties.item(i);
                String name = prop.getAttribute("name");
                String value = prop.getAttribute("value");

                switch (name) {
                    case "jakarta.persistence.jdbc.driver":
                        driver = value;
                        break;
                    case "jakarta.persistence.jdbc.url":
                        url = value;
                        break;
                    case "jakarta.persistence.jdbc.user":
                        user = value;
                        break;
                    case "jakarta.persistence.jdbc.password":
                        password = value;
                        break;
                }
            }

            // Cargar el driver JDBC
            Class.forName(driver);

        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la configuración de persistencia: " + e.getMessage(), e);
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
    public static String getUrl() {
        return url;
    }
    
}
