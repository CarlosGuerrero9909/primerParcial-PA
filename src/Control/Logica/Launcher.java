package Control.Logica;

import Modelo.AnimalVO;
import Vista.VtnPrincipal;

/**
 *
 * @author Carlos Guerrero
 * @author Nicolas Dí­az
 */
public class Launcher {
	/**
	 * Metodo principal, encargado de lanzar el programa y la ventana principal
	 * @param args 
	 */
	public static void main(String[] args) {
		AnimalVO animal = new AnimalVO();
		VtnPrincipal vtnPrin = new VtnPrincipal();
		Gestor gestor = new Gestor(vtnPrin, animal);
		gestor.iniciarVtnPrincipal();
		vtnPrin.setVisible(true);

	}
}
