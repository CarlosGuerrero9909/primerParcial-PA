package Control.Logica;

import Modelo.AnimalVO;
import Vista.VtnPrincipal;

/**
 *
 * @author Carlos Guerrero
 * @author Nicolas Dí­az
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
