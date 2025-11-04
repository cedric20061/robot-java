import java.util.ArrayList;


public class SimulateurAppX_V5 {

	public static void main(String[] args) {
		// création de l'environnement et récupération du terrain 
		Terrain terrain = Environnement.creerEnvironnement(10, 10); 

		// création du robot 
		Robot robot = new Robot(0, 0, "sud", "electrique"); 

		// ajout du robot sur le terrain 
		terrain.ajouterRobot(robot); 
		terrain.ajouterObstaclesAleatoiresIsolesAvecBords(50);

		// met à jour les composants graphiques 
		terrain.updateIHM(); 

		double estBatterieRetour = estimerBatterieRetour(robot);
		while(robot.getBatteryPercent() - estBatterieRetour > 18) {
			if(!robot.isObstacleDevantAvecConsommationBatterie()) {
				robot.avancer();
			}else 
				contournerObs(robot);

			if(robot.getLigne() == 9) {
				robot.tournerGauche();
				if(robot.isObstacleDevantAvecConsommationBatterie()) {
					robot.tournerGauche();
					robot.avancer();
					robot.tournerDroite();
				}
				robot.avancer();
				robot.tournerGauche();
			}
			if(robot.getLigne() == 0) {
				robot.tournerDroite();
				if(robot.isObstacleDevantAvecConsommationBatterie()) {
					robot.tournerDroite();
					robot.avancer();
					robot.tournerGauche();
				}
				robot.avancer();
				robot.tournerDroite();
			}
			estBatterieRetour = estimerBatterieRetour(robot);
		}
		int cheminRetour = ("nord".equals(robot.getDirection()) || "sud".equals(robot.getDirection())) ? 2 : 1;
		retournerMaison(robot, cheminRetour);
		robot.compterObstacles(2);
		robot.chargerBatterie();
		System.out.println("Pourcentage de charge après rechargage: " + robot.getBatteryPercent());
	}
	public static void contournerObs(Robot robot) {
		String dir = robot.getDirection();
		int ligne = robot.getLigne();
		int colonne = robot.getColonne();

		// === Cas 1 : bord bas (ligne proche de la limite) ===
		if (ligne >= 8 && "sud".equals(dir)) {
			// Contourne par la gauche (pas de place en bas)
			robot.tournerGauche();
			robot.avancer();
			robot.tournerDroite();
			robot.avancer();
			robot.tournerDroite();
			robot.tournerDroite();
			robot.avancer();
			return;
		}

		// === Cas 2 : bord haut (ligne proche de 0) ===
		if (ligne <= 1 && "nord".equals(dir)) {
			// Contourne par la droite (pas de place en haut)
			robot.tournerDroite();
			robot.avancer();
			robot.tournerGauche();
			robot.avancer();
			robot.tournerGauche();
			robot.tournerGauche();
			robot.avancer();
			return;
		}

		// === Cas 3 : bord droit (colonne proche de la limite) ===
		if (colonne >= 8 && "est".equals(dir)) {
			// Contourne vers le bas
			robot.tournerDroite();
			robot.avancer();
			robot.tournerGauche();
			robot.avancer();
			robot.tournerGauche();
			robot.tournerGauche();
			robot.avancer();
			return;
		}
		/*
	    // === Cas 4 : bord gauche (colonne proche de 0) ===
	    if (colonne <= 1 && "ouest".equals(dir)) {
	        // Contourne vers le haut
	        robot.tournerGauche();
	        robot.avancer();
	        robot.tournerDroite();
	        robot.avancer();
	        robot.tournerDroite();
	        robot.tournerDroite();
	        robot.avancer();
	        return;
	    }*/

		// === Cas 5 : contournement standard, mais direction/position adaptative ===
		boolean contournerAGauche = true; // par défaut à gauche

		// Cas où tourner à gauche serait dangereux car on est au bord
		switch (dir) {
		case "nord":
			if (colonne == 0) contournerAGauche = false; // si on est tout à gauche, on contourne à droite
			break;
		case "sud":
			if (colonne == 9) contournerAGauche = false; // tout à droite, contourner à droite
			break;
		case "est":
			if (ligne == 9) contournerAGauche = false; // tout en bas, contourner à droite
			break;
		case "ouest":
			if (ligne != 0) contournerAGauche = false; // tout en haut, contourner à droite
			break;
		}

		if (contournerAGauche) {
			// --- Contournement standard par la gauche ---
			robot.tournerGauche();
			robot.avancer();
			robot.tournerDroite();
			robot.avancer();
			robot.avancer();
			robot.tournerDroite();
			robot.avancer();
			robot.tournerGauche();
		} else {
			// --- Contournement standard par la droite ---
			robot.tournerDroite();
			robot.avancer();
			robot.tournerGauche();
			robot.avancer();
			robot.avancer();
			robot.tournerGauche();
			robot.avancer();
			robot.tournerDroite();
		}
	}


	public static double estimerBatterieRetour(Robot robot) {
		int ligne = robot.getLigne();
		int colonne = robot.getColonne();
		String dir = robot.getDirection();

		ArrayList<Integer[]> obstacles = robot.getObstaclesEncountered();

		int obsChemin1 = 0;
		for (Integer[] obs : obstacles) {
			if ((obs[0] < ligne && obs[1] == colonne) || (obs[0] == 0 && obs[1] < colonne))
				obsChemin1++;
		}

		int obsChemin2 = 0;
		for (Integer[] obs : obstacles) {
			if ((obs[1] < colonne && obs[0] == ligne) || (obs[1] == 0 && obs[0] < ligne))
				obsChemin2++;
		}

		// Ajout : obstacles sur les bords comptent double (plus difficile à contourner)
		for (Integer[] obs : obstacles) {
			if (obs[0] == 0 || obs[1] == 0 || obs[0] == 9 || obs[1] == 9) {
				obsChemin1 += 0.5;
				obsChemin2 += 0.5;
			}
		}

		// Forçage du chemin selon la direction
		int cheminChoisi;
		if ("nord".equals(dir) || "sud".equals(dir)) {
			cheminChoisi = 2; // d'abord colonne 0
		} else {
			cheminChoisi = 1; // d'abord ligne 0
		}

		int obsCount = cheminChoisi == 1 ? obsChemin1 : obsChemin2;
		int moves = ligne + colonne;
		double rotations = 2 * 0.5;
		double obstacleEnergy = obsCount * 0.25;
		double moveEnergy = moves * 1.0;

		return moveEnergy + rotations + obstacleEnergy;
	}

	public static void retournerMaison(Robot robot, int chemin) {
		System.out.println("Retour à la base initié...");
		// Définir le chemin selon la valeur reçue
		if (chemin == 1) {
			// Chemin : ligne 0 → colonne 0
			if (!"nord".equals(robot.getDirection())) {
				while (!"nord".equals(robot.getDirection()))
					robot.tournerGauche();
			}

			while (robot.getLigne() > 0) {
				if (!isObstacleDevantSansConso(robot)) robot.avancer();
				else {
					if(robot.getLigne() != 1) {
						contournerObs(robot);
					}else {
						robot.tournerGauche();
						robot.avancer();
						robot.tournerDroite();
						robot.avancer();
						robot.tournerGauche();
					}
				}
				if (robot.getLigne() == 0) break;
			}

			if (!"ouest".equals(robot.getDirection())) {
				while (!"ouest".equals(robot.getDirection()))
					robot.tournerGauche();
			}

			while (robot.getColonne() > 0) {
				if (!isObstacleDevantSansConso(robot)) robot.avancer();
				else contournerObs(robot);
			}

		} else {
			// Chemin : colonne 0 → ligne 0
			if (!"ouest".equals(robot.getDirection())) {
				while (!"ouest".equals(robot.getDirection()))
					robot.tournerGauche();
			}

			while (robot.getColonne() > 0) {
				if (!isObstacleDevantSansConso(robot)) robot.avancer();
				else {
					if(robot.getColonne() != 1) {
						contournerObs(robot);
					}else {
						robot.tournerDroite();
						robot.avancer();
						robot.tournerGauche();
						robot.avancer();
						robot.tournerDroite();
					}
				}
				if (robot.getColonne() == 0) break;
			}

			if (!"nord".equals(robot.getDirection())) {
				while (!"nord".equals(robot.getDirection()))
					robot.tournerGauche();
			}

			while (robot.getLigne() > 0) {
				if (!isObstacleDevantSansConso(robot)) robot.avancer();
				else contournerObs(robot);
			}
		}

		System.out.println("Retour au point initial effectué !");
	}


	public static boolean isObstacleDevantSansConso(Robot robot) {
		int ligne = robot.getLigne();
		int colonne = robot.getColonne();
		String dir = robot.getDirection();
		ArrayList<Integer[]> obstacles = robot.getObstaclesEncountered();

		int nextL = ligne;
		int nextC = colonne;

		switch (dir) {
		case "nord": nextL--; break;
		case "sud": nextL++; break;
		case "ouest": nextC--; break;
		case "est": nextC++; break;
		}

		for (Integer[] obs : obstacles) {
			if (obs[0] == nextL && obs[1] == nextC) return true;
		}

		// bord du terrain aussi considéré comme obstacle
		return nextL < 0 || nextC < 0 || nextL > 9 || nextC > 9;
	}

}
