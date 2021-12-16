package Control.Conexion;

// importaciones necesarias para el funcionamiento del programa
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta clase esta encargada de establecer el puente entre el cliente y el servidor por medio sql.
 * Contine toda la informacion para el ingreso a la base de datos, por lo que si se desea pedir o
 * enviar datos, se hace con esta clase
 *
 * @author Carlos Guerrero
 * @author Nicolas Di­az
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
	 * Metodo encargado de registrar el driver y establecer la conexion con la BD
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
	 * Metodo encargado de desconectar la conexion con la base de datos
	 */
	public static void desconectar() {
		cn = null;
	}
}
