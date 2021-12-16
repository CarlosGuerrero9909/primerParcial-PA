package Control.Logica;

// importaciones necesarias para el funcionamiento de todos los recursos usados en la clase
import Control.Dao.AnimalDAO;
import Modelo.AnimalVO;
import Vista.VtnInsertar;
import Vista.VtnPrincipal;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
 * Esta clase se envarga de toda la logica del programa, haciendole peticiones a las demas clases y
 * enviando datos, sin esta clase el programa no podria funcionar correctamente
 *
 * @author Carlos Guerrero
 * @author Nicolas Di­az
 */
public class Gestor implements ActionListener {

	// atributo de nuestra ventana principal
	private VtnPrincipal vtnPrin;
	// atributo de ventana ingresar que se mostrara al presionar el boton ingresar nuevo animal
	private VtnInsertar vtnIns;
	// atributo en el que se alojara el archivo properties que buscaremos posteriormente para registrar todoslo registros que contenga
	private Properties dataProperties;
	// atributo en el que se guardara el archivo properties que se elija al ejecutar la ventana de busqueda
	private File file;
	// objeto de la clase AnimalDAO con el cual pediremos conexion a la base de datos para recuperar, ingresar, modificar y eliminar registros de la base de datos
	private AnimalDAO miAnimalDAO;
	// arraylist en el que se guardaran todos los registros alojados en nuetra base de datos
	private ArrayList<AnimalVO> listaAnimales;
	// atributo con la que se controlara el animal que se esta mostransdo en la interfaz, ventan principal
	private int index;
	// atributo usada para la reproduccion de sonido
	private AudioInputStream audioInputStream;
	// atributo usada para la reproduccion de sonido
	private Clip clip;
	// atributo en el que se guardara el archivo aleatorio que se creara el la carpeta del proyecto con toda la informacion de la base de datos
	private RandomAccessFile archivo;
	// atributos usadas para creacion del archivo aleatorio, clave contrine el numero de registro
	private int clave;
	// atributo que contiene la cantidad de bytes que tiene 
	private long tamReg;
	// atributo en el que se guardara el archivo aleatorio que se elija al ejecutar la ventana de busqueda
	private File fileAleatorio;

	/**
	 * Metodo constructor de la clase Gestor, encargado de iniciar los objetos y principales
	 * acciones necesarias para el control y gestion de la logica del programa. Para la
	 * instanciacion de este le debe llegar una ventana principal y un animal
	 *
	 * @param vtnPrin
	 * @param animal
	 */
	public Gestor(VtnPrincipal vtnPrin, AnimalVO animal) {
		this.vtnPrin = vtnPrin;
		vtnIns = new VtnInsertar(vtnPrin, true);
		// indice para control de animal mostrado en ventana principal
		index = 0;
		// instanciacion de arraylist donde se guardaran todos los registros de la base de datos
		listaAnimales = new ArrayList<>();
		// obtener registros de la base de datos
		obtenerRegistrosBaseDeDatos();
		// si la base de datos no cuenta con registros, pide un archivo properties para agregarlos a la base de datos
		iniciandoProperties();
		// se carga el primer animal en la GUI
		cargarAnimalEnVtnPrincipal(index);
		// se preparan los botones de la interfaz para que pueda escuchar instrucciones
		this.vtnPrin.jBtnAnterior.addActionListener(this);
		this.vtnPrin.jBtnEliminar.addActionListener(this);
		this.vtnPrin.jBtnInsertar.addActionListener(this);
		this.vtnPrin.jBtnModificar.addActionListener(this);
		this.vtnPrin.jBtnPlaySonido.addActionListener(this);
		this.vtnPrin.jBtnSalir.addActionListener(this);
		this.vtnPrin.jBtnSiguiente.addActionListener(this);
		this.vtnPrin.jBtnStopSonido.addActionListener(this);
		this.vtnPrin.jBtnTermMod.addActionListener(this);
		this.vtnIns.jBtnAgregar.addActionListener(this);

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
	 * Recoge el arraylist con todos los animales registrados en la base de datos anteriormente,
	 * luego de esto guarda un archivo aleatorio con toda la informacion
	 */
	private void obtenerRegistrosBaseDeDatos() {
		// creacion de una nueva instancia animal Dao para su implementacion al recuperar una arraylist con todos los registros de la base de datos
		miAnimalDAO = new AnimalDAO();
		// le pide al dao la lista de animales porque es la que hace la consulta
		listaAnimales = miAnimalDAO.recuperarListaDeAnimalesDeBaseDeDatos();
		// si el tamaño del arraylist es de 0 da una ventana emergente comunicando que no existen registros por lo que pedira que se seleccione una archivo properties
		if (listaAnimales.isEmpty()) {
			// mostrar ventana emergente
			VtnPrincipal.mostrarJOptionPane(2);
		}
	}

	/**
	 * Carga el archivo properties con una ventana de busqueda donde se alojan los animales que se
	 * desean ingresar a la base de datos, al ingresar a la base de datos la informacion se guarda
	 * en un archivo tipo aleatorio
	 */
	public void iniciandoProperties() {
		if (listaAnimales.isEmpty()) {
			// inicializacion de los atributos que cargaran el archivo properties
			dataProperties = new Properties();
			// se le asocia el retorno de cargarProperties el cual abre una ventana de busqueda para elegir un archivo properties
			dataProperties = cargarProperties();
			// guarda informacion de archivo properties en la base de datos
			guardarPropiedadesEnBaseDeDatos();
			// Ventana emergente de confirmacion que comunica que "Se inserto registro(s) satisfactoriamente a la base de datos."
			VtnPrincipal.mostrarJOptionPane(6);
			// obtener registros de la base de datos en un arraylist
			obtenerRegistrosBaseDeDatos();
		}
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
			if (!propiedades.isEmpty()) { // si no esta vacio
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
		// bucle que recorre cada objeto del archivo properties para obtener sus propiedades 
		// y posteriormente insertarlo en la base de datos
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

	/**
	 * Metodo encargado de realizar la eliminacion de un animal invocando al dao para que este
	 * elimine el registro en la DB
	 *
	 * @param index
	 */
	public void eliminarAnimal(int index) {
		String nombreEliminar = listaAnimales.get(index).getNombre();
		if (miAnimalDAO.eliminarRegistro(nombreEliminar)) {
			VtnPrincipal.mostrarJOptionPane(8);
		} else {
			VtnPrincipal.mostrarJOptionPane(7);
		}
	}

	/**
	 * Metodo encargado de desbloquear los texfield para la modificacion de la taxonomia del animal
	 */
	private void desbloquearTextField() {
		//se habilita el botos terminar edicion y los textfield
		vtnPrin.jBtnTermMod.setVisible(true);
		vtnPrin.jTfClase.setEditable(true);
		vtnPrin.jTfEspecie.setEditable(true);
		vtnPrin.jTfFamilia.setEditable(true);
		vtnPrin.jTfFilum.setEditable(true);
		vtnPrin.jTfGenero.setEditable(true);
		vtnPrin.jTfOrden.setEditable(true);
		vtnPrin.jTfSubFilum.setEditable(true);
	}

	/**
	 * Metodo encargado de bloquear los texfield una vez terminada la modificacion de la taxonomia
	 * del animal
	 */
	private void bloquearTextField() {
		vtnPrin.jTfClase.setEditable(false);
		vtnPrin.jTfEspecie.setEditable(false);
		vtnPrin.jTfFamilia.setEditable(false);
		vtnPrin.jTfFilum.setEditable(false);
		vtnPrin.jTfGenero.setEditable(false);
		vtnPrin.jTfOrden.setEditable(false);
		vtnPrin.jTfSubFilum.setEditable(false);

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
			if ((vtnIns.jTfFilum.getText().length() == 0) || (vtnIns.jTfSubFilum.getText().length() == 0) || (vtnIns.jTfClase.getText().length() == 0) || 
					(vtnIns.jTfOrden.getText().length() == 0) || (vtnIns.jTfFamilia.getText().length() == 0) || (vtnIns.jTfGenero.getText().length() == 0) || (vtnIns.jTfEspecie.getText().length() == 0) || 
					(vtnIns.jTfNombre.getText().length() == 0) || (vtnIns.jTfDireImg.getText().length() == 0) || (vtnIns.jTfDireImg.getText().length() == 0)) {
				VtnPrincipal.mostrarJOptionPane(5);
			} else {
				// ejecucion de metodo para agregar la persona al arraylist
				agregarAnimalABaseDeDatos(vtnIns.jTfFilum.getText(), vtnIns.jTfSubFilum.getText(), vtnIns.jTfClase.getText(), vtnIns.jTfOrden.getText(), vtnIns.jTfFamilia.getText(), 
						vtnIns.jTfGenero.getText(), vtnIns.jTfEspecie.getText(), vtnIns.jTfNombre.getText(), "/data/" + vtnIns.jTfDireImg.getText(), "/src/data/" + vtnIns.jTfDireSon.getText());
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
			desbloquearTextField();
		}
		// configuracion del boton terminar modificacion de la ventana principal
		if (e.getSource() == vtnPrin.jBtnTermMod) {
			bloquearTextField();
			vtnPrin.jBtnTermMod.setVisible(false);
			if (miAnimalDAO.modificarRegistro(vtnPrin.jTfNombre.getText(), vtnPrin.jTfClase.getText(), vtnPrin.jTfEspecie.getText(), vtnPrin.jTfFamilia.getText(),
					vtnPrin.jTfFilum.getText(), vtnPrin.jTfGenero.getText(), vtnPrin.jTfOrden.getText(), vtnPrin.jTfSubFilum.getText())) {
				VtnPrincipal.mostrarJOptionPane(10);
			} else {
				VtnPrincipal.mostrarJOptionPane(9);
			}
			obtenerRegistrosBaseDeDatos();
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
			del tamaÃ±o del arraylist*/
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
			//crea el archivo aleatorio
			crearArchivoAleatrio();
			// se ejecuta metodos para guardar base de datos en archivo aleatorio
			escribirEnArchivoAleatorio();
			// cierra el archivo aleatorio
			cerrarAleatorio();
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

	/**
	 * Metodo empleado para crear el archivo aleatorio teniendo en cuenta el tamaño por registro y
	 * la naturaleza de RandomAccessFile
	 */
	public void crearArchivoAleatrio() {
		clave = 0;
		// 400 bytes = 40b por campo ya que todos son strings y cada string debe tener maximo 20 carateres
		tamReg = 400;
		//creando el archivo
		try {
			fileAleatorio = new File("aleatorio.dat");
			archivo = new RandomAccessFile(fileAleatorio, "rw");
		} catch (FileNotFoundException fnfe) {
			/* Archivo no encontrado */
		}
	}

	/**
	 * Metodo para escribir arraylist en archivo aleatorio teniedno en cuenta eltamaño de cada
	 * registro en el arraylist y ña gestion de exepciones correspopndiente al manejo de archivos
	 */
	public void escribirEnArchivoAleatorio() {
		try {
			clave = buscarUltimoEnArchivoAleatorio();
			clave++;
			// bucle para obtener los valores de los campos de cada registro
			for (AnimalVO a : listaAnimales) {
				if (a.getFilum().length() < 40) {
					for (int i = a.getFilum().length(); i < 40; i++) {
						a.setFilum(a.getFilum() + " ");
					}
				} else {
					a.setFilum(a.getFilum().substring(0, 40));
				}
				if (a.getSubfilum().length() < 40) {
					for (int i = a.getSubfilum().length(); i < 40; i++) {
						a.setSubfilum(a.getSubfilum() + " ");
					}
				} else {
					a.setSubfilum(a.getSubfilum().substring(0, 40));
				}
				if (a.getClase().length() < 40) {
					for (int i = a.getClase().length(); i < 40; i++) {
						a.setClase(a.getClase() + " ");
					}
				} else {
					a.setClase(a.getClase().substring(0, 40));
				}
				if (a.getOrden().length() < 40) {
					for (int i = a.getOrden().length(); i < 40; i++) {
						a.setOrden(a.getOrden() + " ");
					}
				} else {
					a.setOrden(a.getOrden().substring(0, 40));
				}
				if (a.getFamilia().length() < 40) {
					for (int i = a.getFamilia().length(); i < 40; i++) {
						a.setFamilia(a.getFamilia() + " ");
					}
				} else {
					a.setFamilia(a.getFamilia().substring(0, 40));
				}
				if (a.getGenero().length() < 40) {
					for (int i = a.getGenero().length(); i < 40; i++) {
						a.setGenero(a.getGenero() + " ");
					}
				} else {
					a.setGenero(a.getGenero().substring(0, 40));
				}
				if (a.getEspecie().length() < 40) {
					for (int i = a.getEspecie().length(); i < 40; i++) {
						a.setEspecie(a.getEspecie() + " ");
					}
				} else {
					a.setEspecie(a.getEspecie().substring(0, 40));
				}
				if (a.getNombre().length() < 40) {
					for (int i = a.getNombre().length(); i < 40; i++) {
						a.setNombre(a.getNombre() + " ");
					}
				} else {
					a.setNombre(a.getNombre().substring(0, 40));
				}
				if (a.getImagen().length() < 40) {
					for (int i = a.getImagen().length(); i < 40; i++) {
						a.setImagen(a.getImagen() + " ");
					}
				} else {
					a.setImagen(a.getImagen().substring(0, 40));
				}
				if (a.getSonido().length() < 40) {
					for (int i = a.getSonido().length(); i < 40; i++) {
						a.setSonido(a.getSonido() + " ");
					}
				} else {
					a.setSonido(a.getSonido().substring(0, 40));
				}
				if (archivo.length() != 0) {
					archivo.seek(archivo.length());
				}
				//se escribe en el archivo 
				archivo.writeChars(a.getFilum());
				archivo.writeChars(a.getSubfilum());
				archivo.writeChars(a.getClase());
				archivo.writeChars(a.getOrden());
				archivo.writeChars(a.getFamilia());
				archivo.writeChars(a.getGenero());
				archivo.writeChars(a.getEspecie());
				archivo.writeChars(a.getNombre());
				archivo.writeChars(a.getImagen());
				archivo.writeChars(a.getSonido());
			}
		} catch (FileNotFoundException  fnfe) {
			VtnPrincipal.mostrarJOptionPane(11);
		} catch (IOException ioe) {
			VtnPrincipal.mostrarJOptionPane(11);
		}
	}

	/**
	 * Metodo que devuelve la clave del ultimo registro en el archivo aleatorio
	 * @return
	 */
	public int buscarUltimoEnArchivoAleatorio() {
		clave = 0;
		try {
			archivo.seek(archivo.length() - tamReg);
			clave = archivo.readInt();
		} catch (FileNotFoundException fnfe) {
			/* Archivo no encontrado */
		} catch (IOException ioe) {
			/* Error al escribir */
		}
		return clave;
	}

	/**
	 *
	 * Metodo para cerrar la conexion al archivo y el flujo o stream
	 */
	public void cerrarAleatorio() {
		try {
			archivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Getters y setters
	/**
	 * 
	 * @return 
	 */
	public VtnPrincipal getVtnPrin() {
		return vtnPrin;
	}
	
	/**
	 * 
	 * @param vtnPrin 
	 */
	public void setVtnPrin(VtnPrincipal vtnPrin) {
		this.vtnPrin = vtnPrin;
	}

	/**
	 * 
	 * @return 
	 */
	public Properties getDataProperties() {
		return dataProperties;
	}

	/**
	 * 
	 * @param dataProperties 
	 */
	public void setDataProperties(Properties dataProperties) {
		this.dataProperties = dataProperties;
	}
	
	/**
	 * 
	 * @return 
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * 
	 * @param file 
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
