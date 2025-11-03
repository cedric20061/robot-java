
public class SimulateurAppX_V5_Test2 {

	public static void main(String[] args) {

		// Création de l’environnement de 10x10 cases
		Terrain terrain = Environnement.creerEnvironnement(10, 10);

		// Création du robot, initialisé à la position (0,0) et orienté vers le sud
		Robot robot = new Robot(0, 0, "sud", "electrique");

		// Ajout du robot et du nombre de victimes à chercher dans le terrain
		terrain.ajouterRobot(robot);

		// Mise à jour de l’interface graphique pour afficher le terrain et le robot
		terrain.updateIHM();

		while(robot.getBatteryPercent()>0) {
			if((robot.getLigne() == 9 || robot.getLigne() == 0) && 
					!(robot.getColonne() == 0 && robot.getLigne() == 0) && 
					!(robot.getColonne() == 9 && robot.getLigne() == 0)) {
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
			robot.avancer();

		}

		robot.compterObstacles(2);
	}

}
