package Control.Logica;

import Control.Dao.AnimalDAO;
import Modelo.AnimalVO;
import Vista.VtnInsertar;
import Vista.VtnPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Carlos Guerrero
 * @author Nicolas Díaz
 */
public class Gestor implements ActionListener {

    private VtnPrincipal vtnPrin;
    private Properties dataProperties;
    private File file;
    private AnimalDAO miAnimalDAO;
    private ArrayList<AnimalVO> listaAnimales;

    /**
     * Metodo constructor de la clase Gestor, encargado de iniciar los objetos y
     * principales acciones necesarias para el control y gestion de la logica
     * del programa
     *
     * @param vtnPrin
     * @param animal
     */
    public Gestor(VtnPrincipal vtnPrin, AnimalVO animal) {
        this.vtnPrin = vtnPrin;
        dataProperties = new Properties();
        dataProperties = cargarProperties();

        // CREAR METODO PARA PASAR PROPERTIES A LA BASE DE DATOS
        // Nombre del metodo
        // obtener registros de la base de datos
        obtenerRegistrosBaseDeDatos();

        // guarda informacion de archivo properties en arraylist
        //guardarPropiedadesEnArrayList();
        this.vtnPrin.jBtnAnterior.addActionListener(this);
        this.vtnPrin.jBtnEliminar.addActionListener(this);
        this.vtnPrin.jBtnInsertar.addActionListener(this);
        this.vtnPrin.jBtnModificar.addActionListener(this);
        this.vtnPrin.jBtnPlaySonido.addActionListener(this);
        this.vtnPrin.jBtnSalir.addActionListener(this);
        this.vtnPrin.jBtnSiguiente.addActionListener(this);
        this.vtnPrin.jBtnStopSonido.addActionListener(this);

    }

    /**
     * Metodo encargado de iniciar la ventana principal, dandole una posicion y
     * un titulo
     */
    public void iniciarVtnPrincipal() {
        vtnPrin.setTitle("Taxonomia del Reino: Animal");
        vtnPrin.setLocationRelativeTo(null);
    }

    /**
     * Se cargan los datos del archivo properties por flujo de datos creando un
     * puente de conexion, luego de haberlo cargado se cierra el archivo ya que
     * no va a ser editado posteriormente.
     *
     * @return
     */
    public Properties cargarProperties() {
        try {
            // buscar el archivo con una ventana de busqueda
            vtnPrin.buscarArchivoProperties(this);

            // se crea un flujo de entrada
            FileInputStream archivo = new FileInputStream(file);

            // se crea el objeto de propiedades
            Properties propiedades = new Properties();

            // carga
            propiedades.load(archivo);

            // cerrar archivo 
            archivo.close();

            if (!propiedades.isEmpty()) { // si no está vacio
                return propiedades;
            }

        } catch (Exception e) {
            // mostrar ventana emergente
            VtnPrincipal.mostrarJOptionPane(0);

            // cerrar el programa
            System.exit(0);
        }
        return null;
    }

    /**
     * Recoge el arraylist con todos los animales registrados en la base de
     * datos
     */
    private void obtenerRegistrosBaseDeDatos() {

        miAnimalDAO = new AnimalDAO();

        // le pide al dao la lista de animales porque es la que hace la consulta
        listaAnimales = miAnimalDAO.listaDeAnimales();

        if (listaAnimales.size() == 0) {
            // mostrar ventana emergente
            VtnPrincipal.mostrarJOptionPane(3);
        }
    }

    /**
     * Metodo para rellenar al arrayList con cada propiedad de cada objeto que
     * está alojado en el archivo properties
     */
    /*public void guardarPropiedadesEnArrayList() {
        for (int i = 1; i <= Integer.parseInt(dataProperties.getProperty("cantidadAnimales")); i++) {

            String filum = dataProperties.getProperty("animal" + i + ".filum");
            String subfilum = dataProperties.getProperty("animal" + i + ".subfilum");
            String clase = dataProperties.getProperty("animal" + i + ".clase");
            String orden = dataProperties.getProperty("animal" + i + ".orden");
            String familia = dataProperties.getProperty("animal" + i + ".familia");
            String genero = dataProperties.getProperty("animal" + i + ".genero");
            String especie = dataProperties.getProperty("animal" + i + ".especie");
            String nombre = dataProperties.getProperty("animal" + i + ".nombre");
            String imagen = dataProperties.getProperty("animal" + i + ".imagen");
            String sonido = dataProperties.getProperty("animal" + i + ".sonido");

            // nuevo objeto
            AnimalVO animal = new AnimalVO(filum, subfilum, clase, orden, familia, genero, especie, nombre, imagen, sonido);

            // se ingresa al arraylist la animal
            animalesArray.add(animal);

        }
    }*/
    /**
     * Metodo sobreescrito de la Clase ActionListener, donde con el parametro se
     * capturara el evento respectivo a cada jBtn para ejecutar las respectivas
     * acciones
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vtnPrin.jBtnInsertar) {
            VtnInsertar vtnIns = new VtnInsertar(vtnPrin, true);
            vtnIns.setTitle("Insertar Nuevo Animal");
            vtnIns.setLocationRelativeTo(null);
            vtnIns.setVisible(true);
        }
        if (e.getSource() == vtnPrin.jBtnAnterior) {

        }
        if (e.getSource() == vtnPrin.jBtnEliminar) {

        }
        if (e.getSource() == vtnPrin.jBtnModificar) {

        }
        if (e.getSource() == vtnPrin.jBtnPlaySonido) {

        }
        if (e.getSource() == vtnPrin.jBtnSiguiente) {

        }
        if (e.getSource() == vtnPrin.jBtnStopSonido) {

        }
        if (e.getSource() == vtnPrin.jBtnSalir) {
            vtnPrin.setVisible(false);
            vtnPrin.dispose();
        }
    }

    //Getters y setters
    public VtnPrincipal getVtnPrin() {
        return vtnPrin;
    }

    public void setVtnPrin(VtnPrincipal vtnPrin) {
        this.vtnPrin = vtnPrin;
    }

    public Properties getDataProperties() {
        return dataProperties;
    }

    public void setDataProperties(Properties dataProperties) {
        this.dataProperties = dataProperties;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
