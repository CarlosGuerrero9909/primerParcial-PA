/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control.Logica;

import Modelo.AnimalVO;
import Vista.VtnPrincipal;

/**
 *
 * @author Usuario
 */
public class Launcher {
	public static void main(String[] args) {
		AnimalVO animal = new AnimalVO();
		VtnPrincipal vtnPrin = new VtnPrincipal();
		Gestor gestor = new Gestor(vtnPrin, animal);
		gestor.iniciarVtnPrincipal();
		vtnPrin.setVisible(true);

	}
}
