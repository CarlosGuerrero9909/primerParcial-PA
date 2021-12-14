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
		index = 0;
		listaAnimales = new ArrayList<AnimalVO>();
		// inicializacion de los atributos que cargaran el archivo properties
		dataProperties = new Properties();
		dataProperties = cargarProperties();
		// guarda informacion de archivo properties en arraylist
		guardarPropiedadesEnArrayList();
		// se carga el primer animal en la GUI
		cargarAnimalEnVtnPrincipal(index);

		// CREAR METODO PARA PASAR PROPERTIES A LA BASE DE DATOS
		// Nombre del metodo
		// obtener registros de la base de datos
		//obtenerRegistrosBaseDeDatos();
			
		// se preparan los botones de la interfaz para que pueden escuchar instrucciones
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
	 * Metodo encargado de iniciar la ventana principal, dandole una posicion y un titulo
	 */
	public void iniciarVtnPrincipal() {
		vtnPrin.setTitle("Taxonomia del Reino: Animal");
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
	 * Recoge el arraylist con todos los animales registrados en la base de datos
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
	 * Metodo para rellenar al arrayList con cada propiedad de cada objeto que está alojado en el
	 * archivo properties
	 */
	public void guardarPropiedadesEnArrayList() {
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
			// se ingresa al arraylist el animal
			listaAnimales.add(animal);
		}
	}

	/**
	 *Metodo encargado de cargar la informacion del animal en la GUI segun el indice que recibe como parametro, este indice es un 
	 * atributo de la clase utilizado para controlar el animal de la lista de animales que se mostrara segun sea la opturacion de
	 * los botones anterior y siguiente
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

	/**
	 *Metodo encargado de reproducir el sonido del animal segun el indice que recibe como parametro, este indice es un 
	 * atributo de la clase utilizado para controlar el animal de la lista de animales que se mostrara segun sea la opturacion de
	 * los botones anterior y siguiente
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
	 * Metodo sobreescrito de la Clase ActionListener, donde con el parametro se capturara el evento
	 * respectivo a cada jBtn para ejecutar las respectivas acciones
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
		if (e.getSource() == vtnPrin.jBtnEliminar) {

		}
		if (e.getSource() == vtnPrin.jBtnModificar) {

		}
		if (e.getSource() == vtnPrin.jBtnPlaySonido) {
			reproducirSonidoAnimal(index);
		}
		if (e.getSource() == vtnPrin.jBtnSiguiente) {
			/*se hace el control del indice utilizado con el boton siguiente para que este no sobre pase el limite 
			del tamaño del arraylist*/
			if (index < listaAnimales.size()-1) {
				index++;
				cargarAnimalEnVtnPrincipal(index);
				try {
					clip.stop();
				} catch (NullPointerException ex) { }
			}else{
				VtnPrincipal.mostrarJOptionPane(3);
			}
		}
		if (e.getSource() == vtnPrin.jBtnAnterior) {
			/*se hace el control del indice utilizado con el boton anterior  para que este no sobre pase el limites del 
			arraylist*/
			if (index > 0) {
				index--;
				cargarAnimalEnVtnPrincipal(index);
				try {
					clip.stop();
				} catch (NullPointerException ex) { }
			}else{
				VtnPrincipal.mostrarJOptionPane(3);
			}
		}
		if (e.getSource() == vtnPrin.jBtnStopSonido) {
			clip.stop();
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
