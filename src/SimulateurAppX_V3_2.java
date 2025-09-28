import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class SimulateurAppX_V3_2 {

	public static void main(String[] args) {
		// création de l'environnement et récupération du terrain 
		Terrain terrain = Environnement.creerEnvironnement(10, 10); 

		// création du robot 
		Robot robot = new Robot(0, 0, "est"); 

		// ajout du robot sur le terrain 
		terrain.ajouterRobot(robot); 

		// ajout de la salle 
		//terrain.ajouterSalleCentrale(); 

		// ajout de la salle centrale en version simple (porte sur le mur du haut) 
		// terrain.ajouterSalleCentraleSimple(); 

		// ajout des deux salles (utile pour la variante **) 
		terrain.ajouterDeuxSalles(); 

		// ajout victime dans l'une des deux salles 
		//terrain.ajouterVictimeSalleCentrale(); 


		// ajout victime dans l'une des deux salles (utile pour la variante **) 
		terrain.ajouterVictimeDeuxSalle(); 

		// met à jour les composants graphiques 
		terrain.updateIHM(); 
		for(int i=0; i<5; i++) {
			robot.avancer();
		}
		int gateLine=0, gateCol=0;
		int victimGravity = 0;
		String victimState = "";
		int victimLine = 0;
		int victimCol = 0;
		while(victimGravity ==0 ){
			while(robot.isObstacleDevant()) {
				if(robot.isObstacleDevant()) {
					robot.tournerDroite();
					robot.avancer();
				}
				robot.tournerGauche();
			}
			robot.avancer();
			gateLine = robot.getLigne();
			gateCol = robot.getColonne();
			robot.avancer();
			orienterVers(robot, "nord");
			if(robot.getLigne()<6) {
				while(robot.getLigne() != 0) {
					robot.avancer();
				}
			}else {
				while(robot.getLigne() != 7) {
					robot.avancer();
				}
			}

			orienterVers(robot, "sud");

			if(robot.isSurVictime()) {
				victimGravity = robot.detecterGravite();
			}
			//Exploration de la pièce pour détecter la victime
			while ((victimGravity == 0) && !(robot.getColonne() == 9 && robot.getLigne() == 2) && !(robot.getColonne() == 9 && robot.getLigne() == 9)) {
				// Gestion du déplacement en mode "serpentin" (aller-retour sur les lignes)
				if (((robot.getLigne() == 2 || robot.getLigne() == 0) || 
						(robot.getLigne() == 7 || robot.getLigne() == 9)) &&
						!(robot.getColonne() == 9 && robot.getLigne() == 2) &&
						!(robot.getColonne() == 9 && robot.getLigne() == 9) &&
						!(robot.getColonne() == 7 && robot.getLigne() == 0) &&
						!(robot.getColonne() == 7 && robot.getLigne() == 7)) {

					if (robot.getLigne() == 2 || robot.getLigne() == 9) {
						robot.tournerGauche();
						robot.avancer();
						if (robot.isSurVictime()) {
							victimGravity = robot.detecterGravite();
							victimLine   = robot.getLigne();
							victimCol = robot.getColonne();
							victimState  = robot.getEtatVictime();
						}
						robot.tournerGauche();
					} else {
						robot.tournerDroite();
						robot.avancer();
						if (robot.isSurVictime()) {
							victimGravity = robot.detecterGravite();
							victimLine   = robot.getLigne();
							victimCol = robot.getColonne();
							victimState   = robot.getEtatVictime();
						}
						robot.tournerDroite();
					}
				}

				// Le robot avance et vérifie s’il trouve une victime
				if (victimGravity == 0) {
					// ==== Ancien avancerEtVerifier(robot) ====
					robot.avancer();
					if (robot.isSurVictime()) {
						victimGravity = robot.detecterGravite();
						victimLine   = robot.getLigne();
						victimCol = robot.getColonne();
						victimState   = robot.getEtatVictime();
					}
				}
			}
			// Sorti du robot explorateur de la pièce 3*3
			while (!(robot.getLigne() == gateLine && robot.getColonne() == gateCol)) {

				int rLine = robot.getLigne();
				int rCol  = robot.getColonne();
				String dir = robot.getDirection();

				// On essaye d'abord de réduire la différence en LIGNE
				if (rLine < gateLine && !"sud".equals(dir)) {
					orienterVers(robot, "sud");
				} else if (rLine > gateLine && !"nord".equals(dir)) {
					orienterVers(robot, "nord");
				} else if (rCol < gateCol && !"est".equals(dir)) {
					orienterVers(robot, "est");
				} else if (rCol > gateCol && !"ouest".equals(dir)) {
					orienterVers(robot, "ouest");
				}

				// Avancer si libre
				if (!robot.isObstacleDevant()) {
					robot.avancer();
				} else {
					// Contourner un mur
					// On tente gauche pour longer le mur
					robot.tournerGauche();
					if (robot.isObstacleDevant()) {
						// Si encore bloqué, on tourne à droite (demi-tour)
						robot.tournerDroite();
						robot.tournerDroite();
					}
				}
			}
			if(victimGravity != 0) {
				robot.avancer();
				orienterVers(robot, "nord");
				while(robot.getLigne() != 0) {
					robot.avancer();
				}
				orienterVers(robot, "ouest");
				while(robot.getColonne() != 0) {
					robot.avancer();
				}
			}else {
				robot.avancer();
				orienterVers(robot, "sud");
				while(robot.getLigne() !=6) {
					robot.avancer();
				}
				robot.tournerGauche();
			}
		}

		// Enregistrement les commandes directives pour arriver à la porte et la trraverser
		ArrayList<String> orders = new ArrayList<String>();
		for(int i=0; i<gateLine; i++) {
			orders.add("A");
		}
		orders.add("G");
		for(int i=0; i<=gateCol; i++) {
			orders.add("A");
		}

		int dCol  = victimCol - (gateCol + 1);
		int dLine = victimLine - gateLine;
		String insideDir = "est";

		// Ajouter l'itinéraire vers la victime

		// Déplacements verticaux
		if (dLine != 0) {
			String wantDir = dLine > 0 ? "sud" : "nord";
			orienterOrdersVers(orders, insideDir, wantDir);
			insideDir = wantDir;
			for (int i = 0; i < Math.abs(dLine); i++) orders.add("A");
		}

		// Déplacements horizontaux
		if (dCol != 0) {
			String wantDir = dCol > 0 ? "est" : "ouest";
			orienterOrdersVers(orders, insideDir, wantDir);
			insideDir = wantDir;
			for (int i = 0; i < Math.abs(dCol); i++) orders.add("A");
		}


		//Modification de l'ittinéraire orders pour l'ajout de la vérification de son état
		int initSize = orders.size();
		int j=0;
		String currentOrientation = "sud";
		for(int i=0; i<initSize; i++) {
			String dir = orders.get(i+j);
			currentOrientation = nouvelleDirection(currentOrientation, dir);
			if(i%5 == 0 && i!=0) {
				ArrayList<String> toAdd = new ArrayList<String>();
				orienterOrdersVers(toAdd, currentOrientation, "nord");
				orienterOrdersVers(toAdd, "nord", currentOrientation);
				for(String l: toAdd) {
					orders.add(i+j, l);
					j++;
				}
			}
		}
		// Une fois que le premier robot est revenu, on utilise le robot médical pour le 2nd parcours 
		RobotMedicalSpecialise robotMedical = new RobotMedicalSpecialise(0, 0, "sud"); 
		terrain.ajouterRobot(robotMedical);
		for(String order: orders) {
			if(order.equals("A")) {
				robotMedical.avancer();
			}
			if(order.equals("G")) {
				robotMedical.tournerGauche();
			}
			if(order.equals("D")) {
				robotMedical.tournerDroite();
			}
		}
		String appropriateTool = "";
		if(victimState.equals("saignement")) appropriateTool = "Trousse de soins ";
		else if (victimState.equals("asphyxie")) appropriateTool = "Masque d’oxygénation artificielle ";
		else if(victimState.equals("fracture"))appropriateTool = "Matelas immobilisateur ";
		else if(victimState.equals("arret cardiaque"))appropriateTool = "Défibrillateur ";

		String sentence = "Etat de la victime: " + victimState + "\nGravité de la victime: "+ victimGravity +"\nOutil nécessaire: " + appropriateTool;
		JOptionPane.showMessageDialog(null, sentence);

		// ajouter ici le code de déplacement du second robot (médical)
	}
	/**
	 * Oriente virtuellement le robot de currDir vers targetDir en ajoutant
	 * les commandes nécessaires dans orders.
	 */
	private static int orienterOrdersVers(ArrayList<String> orders, String currDir, String targetDir) {
		if (targetDir.equals(currDir)) return 0;

		// ordre horaire
		String[] directions = {"nord", "est", "sud", "ouest"};
		int currIndex = -1, targetIndex = -1;

		for (int i = 0; i < directions.length; i++) {
			if (directions[i].equals(currDir))   currIndex = i;
			if (directions[i].equals(targetDir)) targetIndex = i;
		}
		if (currIndex == -1 || targetIndex == -1) return 0;

		int diff = (targetIndex - currIndex + 4) % 4; // 0..3
		if (diff == 1) {             // un pas à droite
			orders.add("D");
			return 1;
		} else if (diff == 2) {       // demi-tour
			orders.add("D");
			orders.add("D");
			return 2;
		} else if (diff == 3) {       // un pas à gauche
			orders.add("G");
			return 1;
		}
		return 0;
	}

	/** Oriente le robot jusqu'à la direction désirée */
	private static int orienterVers(Robot robot, String dir) {
		String currDir = robot.getDirection();

		// Si déjà orienté correctement
		if (dir.equals(currDir)) {
			return 0;
		}

		// Ordre des directions dans le sens horaire
		String[] directions = {"nord", "est", "sud", "ouest"};

		// Trouver les index des directions actuelle et cible
		int currIndex = -1;
		int targetIndex = -1;

		for (int i = 0; i < directions.length; i++) {
			if (directions[i].equals(currDir)) currIndex = i;
			if (directions[i].equals(dir)) targetIndex = i;
		}

		if (currIndex == -1 || targetIndex == -1) {
			return 0;
		}

		// Calcul du nombre de pas à tourner dans chaque sens
		int diff = (targetIndex - currIndex + 4) % 4;  // 0..3
		// diff = 1 -> tournerDroite une fois
		// diff = 2 -> demi-tour
		// diff = 3 -> tournerGauche une fois

		if (diff == 1) {
			robot.tournerDroite();
			return 1;
		} else if (diff == 2) {
			robot.tournerDroite();
			robot.tournerDroite();
			return 2;
		} else if (diff == 3) {
			robot.tournerGauche();
			return 1;
		}
		return 0;
	}

	public static String nouvelleDirection(String currDir, String move) {
		if (move.equals("A")) return currDir; // avancer ne change pas la direction

		String[] directions = {"nord", "est", "sud", "ouest"};
		int index = -1;
		for (int i = 0; i < directions.length; i++) {
			if (directions[i].equals(currDir)) {
				index = i;
				break;
			}
		}
		if (index == -1) return currDir; // sécurité

		if (move.equals("D")) { // tourner droite
			index = (index + 1) % 4;
		} else if (move.equals("G")) { // tourner gauche
			index = (index + 3) % 4;
		}

		return directions[index];
	}
}
