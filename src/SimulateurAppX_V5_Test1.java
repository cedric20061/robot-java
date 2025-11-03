
public class SimulateurAppX_V5_Test1 {

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

		while(!robot.getRobotDetruit()) {
			robot.avancer();
		}

	}

}
