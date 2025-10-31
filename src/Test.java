import java.util.Scanner;


public class Test {
	public static void main(String[] args) {
		// Création de l’environnement de 10x10 cases
		Terrain terrain = Environnement.creerEnvironnement(10, 10);

		// Création du robot, initialisé à la position (0,0) et orienté vers le sud
		Robot robot = new Robot(0, 0, "sud");

		// Ajout du robot et du nombre de victimes à chercher dans le terrain
		terrain.ajouterRobot(robot);

		// Mise à jour de l’interface graphique pour afficher le terrain et le robot
		terrain.updateIHM();
		robot.tournerGauche();
		while(!robot.isObstacleDevant()) {
			robot.avancer();
			robot.tournerDroite();
			robot.avancer();
			robot.tournerGauche();
			
		}



	}
}
