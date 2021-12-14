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
 * @author Nicolas Díaz
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
     * recordemos que a la clase DAO le llegan objetos
     *
     * @param mascota
     */
    public void insertarDatos(AnimalVO animalregistro) {
        try {
            // pide la conexion (Importante)
            con = Conexion.getConexion();
            // cree una estructura para poder transportar sql 
            st = con.createStatement();
            // se escribe el comando en sql para registrar cada mascota que ingrese al metodo
            String insercion = "INSERT INTO Animales VALUES('" + animalregistro.getFilum() + "','" + animalregistro.getSubfilum() + "','" + animalregistro.getClase() + "','" + animalregistro.getOrden() + "','" + animalregistro.getFamilia() + "','" + animalregistro.getGenero() + "','" + animalregistro.getEspecie() + "','" + animalregistro.getNombre() + "','" + animalregistro.getImagen() + "','" + animalregistro.getSonido() + "')";
            // se realiza una insercionm por lo que es Update
            st.executeUpdate(insercion);
            // Ventana emergente de confirmacion
            VtnPrincipal.mostrarJOptionPane(6);
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

                // añade una nueva amscota al arraylist
                misAnimales.add(animal);
            }

            // cierra el stamenten porque ya se realizo la consulta
            st.close();
            Conexion.desconectar(); // desconecta la DB
        } catch (SQLException ex) {
            // mostrar ventana emergente
            VtnPrincipal.mostrarJOptionPane(1);
        }

        // retorna el arraylist
        return misAnimales;
    }
}
