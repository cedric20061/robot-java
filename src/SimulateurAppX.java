/**
 * Programme principal avec la méthode main
 * @author Département TIC - ESIGELEC
 * @version 2.2
 */
public class SimulateurAppX {

	public static void main(String[] args) {
		// création de l'environnement et récupération du terrain
		Terrain terrain = Environnement.creerEnvironnement(10, 10);

		// création du robot
		Robot robot = new Robot(0, 0, "sud");

		// ajout du robot sur le terrain
		terrain.ajouterRobot(robot);
		terrain.ajouterPlusieursVictimes();
		
		// mise à jour des composants graphiques
		
		terrain.updateIHM();
		
		// ajouter ici le code de déplacement du robot
		
		for(int i=0;i<4;i++) {
			robot.avancer();
					
		}
		
		robot.tournerGauche();
		
		
		
		
	}

}
