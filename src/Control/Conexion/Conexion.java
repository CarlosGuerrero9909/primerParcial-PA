package Control.Conexion;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Establece el puente entre el cliente y el servidor
 *
 * @author Carlos Guerrero
 * @author Nicolas DÃ­az
 */
public class Conexion {

    // creamos una conexion
    private static Connection cn = null;
    // definimos el driver (cliente de derby)
    private static Driver driver = new org.apache.derby.jdbc.ClientDriver();
    // forma en la que nos vamos a conectar
    private static String URLBD = "jdbc:derby://localhost:1527/GRANJA";
    // login de la base de datos
    private static String usuario = "granja";
    private static String contrasena = "granja";

    /**
     * Metodo encargado de registrar el driver y establecer la conexion con la
     * DB
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConexion() throws SQLException {
        DriverManager.registerDriver(driver);
        cn = DriverManager.getConnection(URLBD, usuario, contrasena);
        return cn;
    }

    /**
     * Desconecta la DB
     */
    public static void desconectar() {
        cn = null;
    }
}
