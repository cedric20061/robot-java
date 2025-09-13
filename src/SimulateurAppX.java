/**
 * Programme principal avec la m�thode main
 * @author D�partement TIC - ESIGELEC
 * @version 2.2
 */
public class SimulateurAppX {

	public static void main(String[] args) {
		// cr�ation de l'environnement et r�cup�ration du terrain
		Terrain terrain = Environnement.creerEnvironnement(10, 10);

		// cr�ation du robot
		Robot robot = new Robot(0, 0, "sud");

		// ajout du robot sur le terrain
		terrain.ajouterRobot(robot);
		terrain.ajouterPlusieursVictimes();
		
		// mise � jour des composants graphiques
		
		terrain.updateIHM();
		
		// ajouter ici le code de d�placement du robot
		
		for(int i=0;i<4;i++) {
			robot.avancer();
					
		}
		
		robot.tournerGauche();
		
		
		
		
	}

}
