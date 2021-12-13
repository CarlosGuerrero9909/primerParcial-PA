package Control.Conexion;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Establece el puente entre el cliente y el servidor
 *
 * @author Carlos Guerrero
 * @author Nicolas Díaz
 */
public class Conexion {

	private static Connection cn = null;
	private static Driver driver = new org.apache.derby.jdbc.ClientDriver();
	private static String URLBD = "jdbc:derby://localhost:1527/GRANJA";
	private static String usuario = "granja";
	private static String contrasena = "granja";

	public static Connection getConexion() throws SQLException {
		DriverManager.registerDriver(driver);
		cn = DriverManager.getConnection(URLBD, usuario, contrasena);
		return cn;
	}

	public static void desconectar() {
		cn = null;
	}
}
