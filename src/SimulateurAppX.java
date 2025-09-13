/**
 * Programme principal avec la méthode main
 * @author Département TIC - ESIGELEC
 * @version 2.2
 */

import java.util.ArrayList;
import java.util.List;

public class SimulateurAppX {

	public static void main(String[] args) {
		// création de l'environnement et récupération du terrain
		Terrain terrain = Environnement.creerEnvironnement(10, 10);

		// création du robot
		Robot robot = new Robot(8, 7, "sud");

		// ajout du robot sur le terrain
		terrain.ajouterRobot(robot);
		terrain.ajouterPlusieursVictimes();
		
		// mise à jour des composants graphiques
		
		terrain.updateIHM();
		
		// ajouter ici le code de déplacement du robot
		

		List<Integer> victimsFoundGravity = new ArrayList<>();
		
		while(true) {
			robot.avancer();
			if(robot.isSurVictime()) {
				victimsFoundGravity.add(robot.detecterGravite());
				
			}
			if(robot.getLigne() == 9 || robot.getLigne() == 0) {
				if (robot.getColonne() == 9 && (robot.getLigne() == 0 || robot.getLigne() == 9)) {
					break;
				}
				
				if (robot.getLigne() == 9 || robot.getLigne() == 0) {
			        changeLine(robot);
			    }
				
			}
		}
		
		if(victimsFoundGravity.size() >0) {
			System.out.println("═════════════════ Victims List ═════════════════");
			System.out.println("╔════════════╦═════════════════╗");
			System.out.printf ("║ %-10s ║ %-14s  ║%n", "Victims", "Gravity");
			System.out.println("╠════════════╬═════════════════╣");
			int gravityTotal = 0;
			for (int i = 0; i < victimsFoundGravity.size(); i++) {
			    System.out.printf("║ %-10s ║  %14d ║%n", "Victim " + (i + 1), victimsFoundGravity.get(i));
			    gravityTotal += victimsFoundGravity.get(i);
			}

			System.out.println("╚════════════╩═════════════════╝");
			float gravityMoy = (float) gravityTotal/victimsFoundGravity.size();
			System.out.printf("The average gravity is: %f", gravityMoy);

		}else {
			System.out.println("No victim found");
		}
		
	}
	
	private static void changeLine(Robot robot) {
	    if (robot.getLigne() == 9) {
	        robot.tournerGauche();
	        robot.avancer();
	        robot.tournerGauche();
	    } else {
	        robot.tournerDroite();
	        robot.avancer();
	        robot.tournerDroite();
	    }
	}

}
