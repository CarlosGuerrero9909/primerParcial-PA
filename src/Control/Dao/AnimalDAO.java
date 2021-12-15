package Control.Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Control.Conexion.*;
import Modelo.AnimalVO;
import Vista.VtnPrincipal;
import java.util.ArrayList;

/**
 * Sabe manejar y entender las instrucciones SQL, es la que se comunica con la
 * base de datos a traves de la clase conexion enviandole sql
 *
 * @author Carlos Guerrero
 * @author Nicolas DÃ­az
 */
public class AnimalDAO {

    private Connection con; // conexion
    private Statement st; // donde se guardara las instrucciones sql
    private ResultSet rs; // donde se van a guardar las respuestas

    /**
     * constructor donde declaramos nulos los atributos, ya que aun no se hbran
     * hecho instrucciones sql
     */
    public AnimalDAO() {
        con = null;
        st = null;
        rs = null;
    }

    /**
     * Insertando datos a la base de datos recordemos que a la clase DAO le
     * llegan objetos
     *
     * @param mascota
     */
    public void insertarRegistro(AnimalVO animalRegistro) {
        try {
            // pide la conexion (Importante)
            con = Conexion.getConexion();
            // cree una estructura para poder transportar sql 
            st = con.createStatement();
            // se escribe el comando en sql para registrar cada mascota que ingrese al metodo
            String insercion = "INSERT INTO Animales VALUES('" + animalRegistro.getFilum() + "','" + animalRegistro.getSubfilum() + "','" + animalRegistro.getClase() + "','" + animalRegistro.getOrden() + "','" + animalRegistro.getFamilia() + "','" + animalRegistro.getGenero() + "','" + animalRegistro.getEspecie() + "','" + animalRegistro.getNombre() + "','" + animalRegistro.getImagen() + "','" + animalRegistro.getSonido() + "')";
            // se realiza una insercionm por lo que es Update
            st.executeUpdate(insercion);
            // cierra el statement
            st.close();
            // al terminar cierra la conexion (Importante)
            Conexion.desconectar();
        } catch (SQLException ex) {
            // mostrar ventana emergente
            VtnPrincipal.mostrarJOptionPane(4);
        }
    }

    /**
     * Modifica el campo de algun registro en la DB
     *
     * @param codigo
     * @return
     */
    public boolean modificarRegistro(String nombreModificar, String clase, String especie, String familia, String filum,
            String genero, String orden, String subFilum) {
        //Instruccion en sql
        String consulta1 = "update Animales set clase ='" + clase + "' where nombre='" + nombreModificar + "'";
        String consulta2 = "update Animales set especie ='" + especie + "' where nombre='" + nombreModificar + "'";
        String consulta3 = "update Animales set familia = '" + familia + "' where nombre='" + nombreModificar + "'";
        String consulta4 = "update Animales set filum = '" + filum + "' where nombre='" + nombreModificar + "'";
        String consulta5 = "update Animales set genero ='" + genero + "' where nombre='" + nombreModificar + "'";
        String consulta6 = "update Animales set orden ='" + orden + "' where nombre='" + nombreModificar + "'";
        String consulta7 = "update Animales set subfilum ='" + subFilum + "' where nombre='" + nombreModificar + "'";
        //String consulta = "update Animales set edad=" + 15 + " where id='" + nombreModificar + "'";
        try {
            con = Conexion.getConexion();// pedimos una conexion
            st = con.createStatement(); // crea una consulta
            st.executeUpdate(consulta1);//Eliminar , agregar o modificar va con update
            st.executeUpdate(consulta2);
            st.executeUpdate(consulta3);
            st.executeUpdate(consulta4);
            st.executeUpdate(consulta5);
            st.executeUpdate(consulta6);
            st.executeUpdate(consulta7);
            st.close(); // cierra el stamenten porque ya se realizo la consulta
            Conexion.desconectar(); // desconecta la DB
            return true;
        } catch (SQLException ex) {
            VtnPrincipal.mostrarJOptionPane(9);
        }
        return false;
    }

    /**
     * Eliminar
     *
     * @param codigo
     * @return
     */
    public boolean eliminarRegistro(String nombreEliminar) {
        // Instruccion en sql
        String consulta = "DELETE FROM Animales where nombre='" + nombreEliminar + "'";
        try {
            con = Conexion.getConexion();// pedimos una conexion
            st = con.createStatement(); // crea una consulta
            st.executeUpdate(consulta); // Eliminar , agregar o modificar va con update
            st.close(); // cierra el stamenten porque ya se realizo la consulta
            Conexion.desconectar(); // desconecta la DB
            return true;
        } catch (SQLException ex) {
            VtnPrincipal.mostrarJOptionPane(7);
        }
        return false;
    }

    /**
     * Metodo encargado de crear un arraylist de animales de acuerdo a la
     * consulta en la DB
     *
     * @return
     */
    public ArrayList<AnimalVO> recuperarListaDeAnimalesDeBaseDeDatos() { // como son varias y no sabemos cuentas se crea un arraylist
        ArrayList<AnimalVO> misAnimales = new ArrayList<AnimalVO>();
        String consulta = "SELECT * FROM Animales"; // todos los registros

        try {
            con = Conexion.getConexion(); // pedimos una conexion
            st = con.createStatement(); // crea una consulta
            rs = st.executeQuery(consulta); // guarda lo que retorna la consultta

            // mientras haya (next) respuestas
            while (rs.next()) { // while porque son varios
                // se crea un animal nuevo
                AnimalVO animal = new AnimalVO();

                // pedimos datos
                animal.setFilum(rs.getString("filum"));
                animal.setSubfilum(rs.getString("subfilum"));
                animal.setClase(rs.getString("clase"));
                animal.setOrden(rs.getString("orden"));
                animal.setFamilia(rs.getString("familia"));
                animal.setGenero(rs.getString("genero"));
                animal.setEspecie(rs.getString("especie"));
                animal.setNombre(rs.getString("nombre"));
                animal.setImagen(rs.getString("imagen"));
                animal.setSonido(rs.getString("sonido"));

                // añade una nueva animal al arraylist
                misAnimales.add(animal);
            }

            // cierra el stamenten porque ya se realizo la consulta
            st.close();
            Conexion.desconectar(); // desconecta la DB
        } catch (SQLException ex) {
            // mostrar ventana emergente
            VtnPrincipal.mostrarJOptionPane(1);
            // cerrar el programa
            System.exit(0);
        }

        // retorna el arraylist
        return misAnimales;
    }
}