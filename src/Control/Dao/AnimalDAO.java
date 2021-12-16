package Control.Dao;

// importaciones necesarias para el funcionamiento de todos los recursos usados en la clase
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Control.Conexion.*;
import Modelo.AnimalVO;
import Vista.VtnPrincipal;
import java.util.ArrayList;

/**
 * Esta clase sabe manejar y entender las instrucciones SQL, es la que se
 * comunica con la base de datos a traves de la clase conexion, a la cual esta
 * clase le envia peticiones y ordenes en sql
 *
 * @author Carlos Guerrero
 * @author Nicolas Di­az
 */
public class AnimalDAO {

    // conexion
    private Connection con;
    // donde se guardara las instrucciones sql
    private Statement st;
    // donde se van a guardar las respuestas
    private ResultSet rs;

    /**
     * constructor donde declaramos nulos los atributos, ya que aun no se habran
     * hecho instrucciones sql
     */
    public AnimalDAO() {
        con = null;
        st = null;
        rs = null;
    }

    /**
     * Este metodo recibe objetos y estos mismos los inserta a la base de datos
     * en forma de registros con instrucciones en sql
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
            String insercion = "INSERT INTO Animales VALUES('" + animalRegistro.getFilum() + "','" + animalRegistro.getSubfilum()
                    + "','" + animalRegistro.getClase() + "','" + animalRegistro.getOrden() + "','" + animalRegistro.getFamilia()
                    + "','" + animalRegistro.getGenero() + "','" + animalRegistro.getEspecie() + "','" + animalRegistro.getNombre()
                    + "','" + animalRegistro.getImagen() + "','" + animalRegistro.getSonido() + "')";
            // se realiza una insercionm por lo que es Update
            st.executeUpdate(insercion);
            // cierra el statement
            st.close();
            // al terminar cierra la conexion (Importante)
            Conexion.desconectar();
        } catch (SQLException ex) {
            // mostrar ventana emergente que comunica que "No se pudo relizar el registro(s) de el(los) animal(es)."
            VtnPrincipal.mostrarJOptionPane(4);
        }
    }

    /**
     * Metodo encargado de modificar y actualisar los campos de algun registro
     * en la DB, llegandole objetos y traduciendo esto a instrucciones sql
     *
     * @param codigo
     * @return
     */
    public boolean modificarRegistro(String nombreModificar, String clase, String especie, String familia, String filum,
            String genero, String orden, String subFilum) {
        //Instruccion en sql para modificar cada uno de los campos de un registro con los objetos que llegaron
        String consulta1 = "update Animales set clase ='" + clase + "' where nombre='" + nombreModificar + "'";
        String consulta2 = "update Animales set especie ='" + especie + "' where nombre='" + nombreModificar + "'";
        String consulta3 = "update Animales set familia = '" + familia + "' where nombre='" + nombreModificar + "'";
        String consulta4 = "update Animales set filum = '" + filum + "' where nombre='" + nombreModificar + "'";
        String consulta5 = "update Animales set genero ='" + genero + "' where nombre='" + nombreModificar + "'";
        String consulta6 = "update Animales set orden ='" + orden + "' where nombre='" + nombreModificar + "'";
        String consulta7 = "update Animales set subfilum ='" + subFilum + "' where nombre='" + nombreModificar + "'";
        try {
            // pedimos una conexion
            con = Conexion.getConexion();
            // crea una consulta
            st = con.createStatement();
            // enviamos la instruccion para que actualice "update" la base de datos con la instruccion que ingresamos arriba
            st.executeUpdate(consulta1);
            st.executeUpdate(consulta2);
            st.executeUpdate(consulta3);
            st.executeUpdate(consulta4);
            st.executeUpdate(consulta5);
            st.executeUpdate(consulta6);
            st.executeUpdate(consulta7);
            // cierra el stamenten porque ya se realizo la consulta
            st.close();
            // desconecta la DB
            Conexion.desconectar();
            return true;
        } catch (SQLException ex) {
            // ventana emergente en caso de errros que comunica que "No se pudo modificar el animal"
            VtnPrincipal.mostrarJOptionPane(9);
        }
        return false;
    }

    /**
     * Este metodo se encarga de eliminar registros completos de la base de
     * datos segun el nombre que le llegue, esto lo traduce a instruccion sql y
     * envia la instruccion a la conexion
     *
     * @param codigo
     * @return
     */
    public boolean eliminarRegistro(String nombreEliminar) {
        // Instruccion en sql
        String consulta = "DELETE FROM Animales where nombre='" + nombreEliminar + "'";
        try {
            // pedimos una conexion
            con = Conexion.getConexion();
            // crea una consulta
            st = con.createStatement();
            // Eliminar , agregar o modificar va con update
            st.executeUpdate(consulta);
            // cierra el stamenten porque ya se realizo la consulta
            st.close();
            // desconecta la DB
            Conexion.desconectar();
            return true;
        } catch (SQLException ex) {
            // ventana emergente en caso de errros que comunica que "No se pudo eliminar el animal"
            VtnPrincipal.mostrarJOptionPane(7);
        }
        return false;
    }

    /**
     * Metodo encargado de crear y retornar un arraylist de animales con todos
     * los registros alojados en la base de datos, creando una conexion y
     * recoperando todos los registros
     *
     * @return
     */
    public ArrayList<AnimalVO> recuperarListaDeAnimalesDeBaseDeDatos() {
        // instanciacion del arraylist en el que se aljaran todos los registros de la base de datos. Se usa un arraylist porque se desconoce exactamente cuantos registros existen
        ArrayList<AnimalVO> misAnimales = new ArrayList<AnimalVO>();
        // Instruccion en sql que pide todos los registros de la base de datos
        String consulta = "SELECT * FROM Animales";

        try {
            // pedimos una conexion
            con = Conexion.getConexion();
            // crea una consulta
            st = con.createStatement();
            // guarda lo que retorna la consultta
            rs = st.executeQuery(consulta);
            // mientras haya mas registros en la BD sigue ejecutando el bloque de codifo
            while (rs.next()) {
                // se instancia un animalVO nuevo
                AnimalVO animal = new AnimalVO();
                // pedimos datos del registro y se lo asignamos a cada variable de nuestro animal instanciado anteriomente
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
                // añade el nuevo animal al arraylist
                misAnimales.add(animal);
            }
            // cierra el stamenten porque ya se realizo la consulta
            st.close();
            // desconecta la DB
            Conexion.desconectar();
        } catch (SQLException ex) {
            // ventana emergente en caso de errros que comunica que "No se pudo realizar la consulta de la Base de Datos."
            VtnPrincipal.mostrarJOptionPane(1);
            // en caso en que  haya un error al cargar la base de datos cerrara el programa
            System.exit(0);
        }
        // retorna el arraylist con todos los  registros encontrados
        return misAnimales;
    }
}
