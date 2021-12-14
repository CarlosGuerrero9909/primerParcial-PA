package Control.Logica;

import Control.Dao.AnimalDAO;
import Modelo.AnimalVO;
import Vista.VtnInsertar;
import Vista.VtnPrincipal;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Properties;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Carlos Guerrero
 * @author Nicolas Díaz
 */
public class Gestor implements ActionListener {

	private VtnPrincipal vtnPrin;
	private VtnInsertar vtnIns;
	private Properties dataProperties;
	private File file;
	private AnimalDAO miAnimalDAO;
	private ArrayList<AnimalVO> listaAnimales;
	private int index;
	private AudioInputStream audioInputStream;
	private Clip clip;

	/**
	 * Metodo constructor de la clase Gestor, encargado de iniciar los objetos y principales
	 * acciones necesarias para el control y gestion de la logica del programa
	 *
	 * @param vtnPrin
	 * @param animal
	 */
	public Gestor(VtnPrincipal vtnPrin, AnimalVO animal) {
		this.vtnPrin = vtnPrin;
		vtnIns = new VtnInsertar(vtnPrin, true);
		index = 0;
		listaAnimales = new ArrayList<>();
		// obtener registros de la base de datos
		obtenerRegistrosBaseDeDatos();
		// si la base de datos no cuenta con registros, pide un archivo properties para agregarlos a la base de datos
		iniciandoProperties();
		// se carga el primer animal en la GUI
		cargarAnimalEnVtnPrincipal(index);
		// se preparan los botones de la interfaz para que pueden escuchar instrucciones
		this.vtnPrin.jBtnAnterior.addActionListener(this);
		this.vtnPrin.jBtnEliminar.addActionListener(this);
		this.vtnPrin.jBtnInsertar.addActionListener(this);
		this.vtnPrin.jBtnModificar.addActionListener(this);
		this.vtnPrin.jBtnPlaySonido.addActionListener(this);
		this.vtnPrin.jBtnSalir.addActionListener(this);
		this.vtnPrin.jBtnSiguiente.addActionListener(this);
		this.vtnPrin.jBtnStopSonido.addActionListener(this);
		this.vtnIns.jBtnAgregar.addActionListener(this);
	}
	
	public void iniciandoProperties(){
		if (listaAnimales.isEmpty()) {
			// inicializacion de los atributos que cargaran el archivo properties
			dataProperties = new Properties();
			dataProperties = cargarProperties();
			// guarda informacion de archivo properties en la base de datos
			guardarPropiedadesEnBaseDeDatos();
			// Ventana emergente de confirmacion
			VtnPrincipal.mostrarJOptionPane(6);
			// obtener registros de la base de datos
			obtenerRegistrosBaseDeDatos();
		}
	}
	
	/**
	 * Metodo encargado de iniciar la ventana principal, dandole una posicion y un titulo
	 */
	public void iniciarVtnPrincipal() {
		//configuracion del titulo de la ventana
		vtnPrin.setTitle("Taxonomia del Reino: Animal");
		// configuracion de la posicion de la ventana
		vtnPrin.setLocationRelativeTo(null);
	}

	/**
	 * Se cargan los datos del archivo properties por flujo de datos creando un puente de conexion,
	 * luego de haberlo cargado se cierra el archivo ya que no va a ser editado posteriormente.
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
	 * Metodo para registar cada animal alojado en el archivo properties en la base de datos por sql
	 * enviandolo al DAO
	 */
	public void guardarPropiedadesEnBaseDeDatos() {
		// crea una instancia de animal DAO para enviar animales del properties y que este los registre
		miAnimalDAO = new AnimalDAO();

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
			AnimalVO animalRegistro = new AnimalVO(filum, subfilum, clase, orden, familia, genero, especie, nombre, imagen, sonido);
			// se envia el animal al DAO para ser registrada en la base de datos
			miAnimalDAO.insertarRegistro(animalRegistro);
		}
	}

	/**
	 * Recoge el arraylist con todos los animales registrados en la base de datos anteriormente
	 */
	private void obtenerRegistrosBaseDeDatos() {
		// creacion de una nueva instancia animal Dao para su implementacion al recuperar una arraylist con todos los registros de la base de datos
		miAnimalDAO = new AnimalDAO();
		// le pide al dao la lista de animales porque es la que hace la consulta
		listaAnimales = miAnimalDAO.recuperarListaDeAnimalesDeBaseDeDatos();
		// si el tamaño del arraylist es de 0 da una ventana emergente comunicando que no existen registros por lo que pedira que se seleccione una archivo properties
		if (listaAnimales.size() == 0) {
			// mostrar ventana emergente
			VtnPrincipal.mostrarJOptionPane(2);
		}
	}

	/**
	 * Metodo encargado de cargar la informacion del animal en la GUI segun el indice que recibe
	 * como parametro, este indice es un atributo de la clase utilizado para controlar el animal de
	 * la lista de animales que se mostrara segun sea la opturacion de los botones anterior y
	 * siguiente
	 *
	 * @param index
	 */
	public void cargarAnimalEnVtnPrincipal(int index) {
		// asignando imagenes
		ImageIcon imagen = new ImageIcon(getClass().getResource(listaAnimales.get(index).getImagen()));
		Icon icon = new ImageIcon(imagen.getImage().getScaledInstance(vtnPrin.jBtnImagen.getWidth(), vtnPrin.jBtnImagen.getHeight(), Image.SCALE_DEFAULT));
		vtnPrin.jBtnImagen.setIcon(icon);

		// asignando informacion
		vtnPrin.jTfClase.setText(listaAnimales.get(index).getClase());
		vtnPrin.jTfEspecie.setText(listaAnimales.get(index).getEspecie());
		vtnPrin.jTfFamilia.setText(listaAnimales.get(index).getFamilia());
		vtnPrin.jTfFilum.setText(listaAnimales.get(index).getFilum());
		vtnPrin.jTfGenero.setText(listaAnimales.get(index).getGenero());
		vtnPrin.jTfNombre.setText(listaAnimales.get(index).getNombre());
		vtnPrin.jTfOrden.setText(listaAnimales.get(index).getOrden());
		vtnPrin.jTfSubFilum.setText(listaAnimales.get(index).getSubfilum());
	}

	public void eliminarAnimal(int index) {
		String nombreEliminar = listaAnimales.get(index).getNombre();
		if (miAnimalDAO.eliminarRegistro(nombreEliminar)) {
			VtnPrincipal.mostrarJOptionPane(8);
		} else {
			VtnPrincipal.mostrarJOptionPane(7);
		}
	}

	/**
	 * Metodo sobreescrito de la Clase ActionListener, donde con el parametro se capturara el evento
	 * respectivo a cada jBtn para ejecutar las respectivas acciones
	 *
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// configuracion del boton insertar de la ventana principal
		if (e.getSource() == vtnPrin.jBtnInsertar) {
			//configura el titulo de la ventana
			vtnIns.setTitle("Insertar Nuevo Animal");
			// configura la posicion de aparicion en pantalla de la ventana 
			vtnIns.setLocationRelativeTo(null);
			// muestra la ventana
			vtnIns.setVisible(true);
		}
		// configuracion del boton agregar de la ventana insertar
		if (e.getSource() == vtnIns.jBtnAgregar) {
			// manejo error si no se ingresa datos para ingresar al animal
			if ((vtnIns.jTfFilum.getText().length() == 0) || (vtnIns.jTfSubFilum.getText().length() == 0) || (vtnIns.jTfClase.getText().length() == 0) || (vtnIns.jTfOrden.getText().length() == 0) || (vtnIns.jTfFamilia.getText().length() == 0) || (vtnIns.jTfGenero.getText().length() == 0) || (vtnIns.jTfEspecie.getText().length() == 0) || (vtnIns.jTfNombre.getText().length() == 0) || (vtnIns.jTfDireImg.getText().length() == 0) || (vtnIns.jTfDireImg.getText().length() == 0)) {
				VtnPrincipal.mostrarJOptionPane(5);
			} else {
				// ejecucion de metodo para agregar la persona al arraylist
				agregarAnimalABaseDeDatos(vtnIns.jTfFilum.getText(), vtnIns.jTfSubFilum.getText(), vtnIns.jTfClase.getText(), vtnIns.jTfOrden.getText(), vtnIns.jTfFamilia.getText(), vtnIns.jTfGenero.getText(), vtnIns.jTfEspecie.getText(), vtnIns.jTfNombre.getText(), vtnIns.jTfDireImg.getText(), vtnIns.jTfDireSon.getText());
				// Ventana emergente de confirmacion
				VtnPrincipal.mostrarJOptionPane(6);
				// obtener registros de la base de datos para actualizar con el animal nuevo
				obtenerRegistrosBaseDeDatos();
				// limpia casillas de la ventana de insertar
				vtnIns.limpiarCasillas();
				// oculta la ventana de insertar
				vtnIns.setVisible(false);
			}
		}
		// configuracion del boton eliminar de la ventana principal
		if (e.getSource() == vtnPrin.jBtnEliminar) {
			eliminarAnimal(index);
			obtenerRegistrosBaseDeDatos();
			iniciandoProperties();
			index = 0;
			cargarAnimalEnVtnPrincipal(index);
		}
		// configuracion del boton modificar de la ventana principal
		if (e.getSource() == vtnPrin.jBtnModificar) {

		}
		// configuracion del boton play sonido de la ventana principal
		if (e.getSource() == vtnPrin.jBtnPlaySonido) {
			reproducirSonidoAnimal(index);
		}
		// configuracion del boton stop sonido de la ventana principal
		if (e.getSource() == vtnPrin.jBtnStopSonido) {
			clip.stop();
		}
		// configuracion del boton siguiente de la ventana principal
		if (e.getSource() == vtnPrin.jBtnSiguiente) {
			/*se hace el control del indice utilizado con el boton siguiente para que este no sobre pase el limite 
			del tamaño del arraylist*/
			if (index < listaAnimales.size() - 1) {
				index++;
				cargarAnimalEnVtnPrincipal(index);
				try {
					clip.stop();
				} catch (NullPointerException ex) {
				}
			} else {
				VtnPrincipal.mostrarJOptionPane(3);
			}
		}
		// configuracion del boton anterior de la ventana principal
		if (e.getSource() == vtnPrin.jBtnAnterior) {
			/*se hace el control del indice utilizado con el boton anterior  para que este no sobre pase el limites del 
			arraylist*/
			if (index > 0) {
				index--;
				cargarAnimalEnVtnPrincipal(index);
				try {
					clip.stop();
				} catch (NullPointerException ex) {
				}
			} else {
				VtnPrincipal.mostrarJOptionPane(3);
			}
		}
		// configuracion del boton salir de la ventana principal
		if (e.getSource() == vtnPrin.jBtnSalir) {
			vtnPrin.setVisible(false);
			vtnPrin.dispose();
		}
	}

	/**
	 * Metodo encargado de reproducir el sonido del animal segun el indice que recibe como
	 * parametro, este indice es un atributo de la clase utilizado para controlar el animal de la
	 * lista de animales que se mostrara segun sea la opturacion de los botones anterior y siguiente
	 *
	 * @param index
	 */
	public void reproducirSonidoAnimal(int index) {
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(FileSystems.getDefault().getPath("").toAbsolutePath() + listaAnimales.get(index).getSonido()));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * metodo que crea y agrega al animal a la base de datos
	 *
	 * @param filum
	 * @param subfilum
	 * @param clase
	 * @param orden
	 * @param familia
	 * @param genero
	 * @param especie
	 * @param nombre
	 * @param direImg
	 * @param direSon
	 */
	public void agregarAnimalABaseDeDatos(String filum, String subfilum, String clase, String orden, String familia, String genero, String especie, String nombre, String direImg, String direSon) {
		// crea una instancia del dao
		miAnimalDAO = new AnimalDAO();
		//crea una persona con los datos ingresado
		AnimalVO animalRegistrar = new AnimalVO(filum, subfilum, clase, orden, familia, genero, especie, nombre, direImg, direSon);
		// animal al dao para que este los ingrese a la base de datos
		miAnimalDAO.insertarRegistro(animalRegistrar);

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
