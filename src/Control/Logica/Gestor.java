package Control.Logica;

import Modelo.AnimalVO;
import Vista.VtnInsertar;
import Vista.VtnPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Carlos Guerrero
 * @author Nicolas Díaz
 */
public class Gestor implements ActionListener{
	private VtnPrincipal vtnPrin;
	private AnimalVO animal;

	/**
	 * Metodo constructor de la clase Gestor, encargado de iniciar los objetos y principales acciones necesarias para 
	 * el control y gestion de la logica del programa
	 * @param vtnPrin
	 * @param animal 
	 */
	public Gestor(VtnPrincipal vtnPrin, AnimalVO animal) {
		this.vtnPrin = vtnPrin;
		this.animal = animal;
		
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
	public void iniciarVtnPrincipal(){
		vtnPrin.setTitle("Taxonomia del Reino: Animal");
		vtnPrin.setLocationRelativeTo(null);
	}
	
	/**
	 * Metodo sobreescrito de la Clase ActionListener, donde con el parametro se capturara el evento respectivo a cada
	 * jBtn para ejecutar las respectivas acciones
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
}
