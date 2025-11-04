import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.util.ArrayList;
/**
 * Classe représentant le robot
 * 
 * @author Département TIC - ESIGELEC
 * @version 1.2
 */

public class Robot {

	/**
	 * image qui représente le Robot
	 */
	private BufferedImage image;
	/**
	 * numéro de la ligne courante du Robot (commence à 0)
	 */
	private int ligne;
	/**
	 * numéro de la colonne courante du Robot (commence à 0)
	 */
	private int colonne;
	/**
	 * direction courante du Robot : "ouest", "est", "nord" ou "sud"
	 */
	private String direction;
	/**
	 * Terrain sur lequel se trouve le robot
	 */
	private Terrain terrain;
	/**
	 * Vitesse de déplacement du Robot (unité : cases par seconde, ie m/s)
	 */
	private double vitesse = 20;
	/**
	 * Indique si le robot est détruit ou non, un robot détruit ne peut plus se
	 * déplacer
	 */
	private boolean robotDetruit = false;
	/**
	 * Indique la couleur de tracé
	 */
	private int trace = 0;
	/**
	 * Type de moteur du robot;
	 */
	private String engineType = "thermique";
	/*
	 * Pourcentage de la batterie dans le cas d'un moteur électrique
	 */
	private double batteryPercent = 100;
	/*
	 * Nombre de chocs encaissé par le robot
	 */
	private int chocsNumber = 0;
	/*
	 * Position des obtacles rencontré et nombre de rencontre
	 */
	private ArrayList<Integer[]> obstaclesEncountered = new ArrayList<>();
	/**
	 * Constructeur du Robot
	 * 
	 * @param ligne
	 *            ligne initiale du Robot (commence à 0)
	 * @param colonne
	 *            colonne initiale du Robot (commence à 0)
	 * @param direction
	 *            direction initiale du Robot (est, ouest, nord ou sud)
	 */
	public Robot(int ligne, int colonne, String direction) {
		// initialisation des attributs de la classe
		this.ligne = ligne;
		this.colonne = colonne;
		this.direction = direction;
		// initialisation de l'attribut image à partir d'un fichier de type
		// image représentant le Robot à l'écran

		try {
			image = ImageIO.read(new File(getCheminImageRobot()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public Robot(int ligne, int colonne, String direction, String engineType) {
		// initialisation des attributs de la classe
		this.ligne = ligne;
		this.colonne = colonne;
		this.direction = direction;
		this.engineType = engineType;
		// initialisation de l'attribut image à partir d'un fichier de type
		// image représentant le Robot à l'écran

		try {
			image = ImageIO.read(new File(getCheminImageRobot()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Permet de déssiner le robot à l'écran
	 * 
	 * @param g
	 *            objet Graphics sur lequel on dessine le robot
	 * @param largeurCase
	 *            largeur de la case sur laquelle on va dessiner le robot (en
	 *            pixel). L'image sera automatiquement redimmensionnée.
	 * @param hauteurCase
	 *            hauteur de la case sur laquelle on va dessiner le robot (en
	 *            pixel) L'image sera automatiquement redimmensionnée.
	 */
	public void dessiner(Graphics g, int largeurCase, int hauteurCase) {
		// on convertit l'objet Graphics en Graphics2D afin de pouvoir utiliser
		// des méthodes plus pratiques
		Graphics2D g2 = (Graphics2D) g;
		// on trouve l'angle de rotation de l'image en s'appuyant sur la
		// direction du Robot
		double angleRotation = 0;
		if ("est".equals(direction))
			angleRotation = Math.PI / 2;
		else if ("sud".equals(direction))
			angleRotation = Math.PI;
		else if ("ouest".equals(direction))
			angleRotation = 3 * Math.PI / 2;

		// on fait pivoter l'image de angleRotation, le centre de rotation étant
		// le centre de la case
		g2.rotate(angleRotation, largeurCase / 2, hauteurCase / 2);
		// on dessine l'image pivotée sur la case dont on connait le Graphics
		g2.drawImage(image, 0, 0, largeurCase, hauteurCase, null);
		// on remet la rotation à 0
		g2.rotate(-angleRotation, largeurCase / 2, hauteurCase / 2);

	}

	/**
	 * permet d'avancer le robot droit devant lui
	 * 
	 * @return 1 si le robot a pu avancer, -1 si le robot n'a pas pu avancer à
	 *         cause d'un obstacle ou des limites du terrain, -2 si le robot n'a
	 *         pas pu avancer car il est détruit, -3 si le robot n'a plus de
	 *         batterie
	 */
	public int avancer() {
		int retour = 1;

		// si le robot est détruit on ne peut pas avancer
		if (robotDetruit)
			retour = -2;

		else
			compterObstacles(1);
		// si on avance alors qu'il y a un obstacle devant alors on détruit le
		// robot
		if (isObstacleDevant()) {

			detruireRobot();
			retour = -1;
		} else {
			if("thermique".equals(engineType) || ("electrique".equals(engineType) && batteryPercent >=1)){
				// on peut faire avancer le robot suivant sa direction
				if ("nord".equals(direction)) {
					ligne--;

				} else if ("sud".equals(direction)) {
					ligne++;

				} else if ("ouest".equals(direction)) {
					colonne--;

				} else if ("est".equals(direction)) {
					colonne++;

				} else
					retour = -1;

				// on mémorise que la case est maintenant visitée
				if (trace == 1) {
					terrain.getGrille()[ligne][colonne].setBackground(Color.RED);

					terrain.getGrille()[ligne][colonne].setVisitee(true);
				}
				if (trace == 2) {
					terrain.getGrille()[ligne][colonne].setBackground(Color.BLUE);

					terrain.getGrille()[ligne][colonne].setVisitee(true);
				}

				// on redessine le terrain
				terrain.repaint();

				// on effectue une petite pause fonction de la vitesse du robot
				try {
					Thread.sleep((int) (1000 / vitesse));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Si il s'agit d'un robot electrique on reduit sont pourcentage de batterie
				if("electrique".equals(engineType)) {
					batteryPercent -= 1;
				}
			}else {
				System.out.println("Batterie trop faible pour avancer");
				retour = -3;
			}

		}
		return retour;

	}

	/**
	 * Permet de faire reculer le robot
	 * 
	 * @return 1 si le robot a pu reculer, -1 si le robot n'a pas pu reculer à
	 *         cause d'un obstacle ou des limites du terrain, -2 si le robot n'a
	 *         pas pu reculer car il est détruit, -3 si le robot n'a plus de
	 *         batterie
	 */
	public int reculer() {
		int retour = 1;

		// si le robot est détruit on ne peut pas avancer
		if (robotDetruit)
			retour = -2;
		else
			// si on recule alors qu'il y a un obstacle derriere alors on détruit le
			// robot
			if (isObstacleDerriere()) {
				detruireRobot();
				retour = -1;
			}

			else {
				if("thermique".equals(engineType) || ("electrique".equals(engineType) && batteryPercent >=1)){
					// on peut faire reculer le robot suivant sa direction opposée
					if ("sud".equals(direction)) {
						ligne--;
					} else if ("nord".equals(direction)) {
						ligne++;
					} else if ("est".equals(direction)) {
						colonne--;
					} else if ("ouest".equals(direction)) {
						colonne++;
					} else
						retour = -1;

					if (trace == 1) {
						terrain.getGrille()[ligne][colonne].setBackground(Color.RED);

						terrain.getGrille()[ligne][colonne].setVisitee(true);
					}
					if (trace == 2) {
						terrain.getGrille()[ligne][colonne].setBackground(Color.BLUE);

						terrain.getGrille()[ligne][colonne].setVisitee(true);
					}
					// Si il s'agit d'un robot electrique on reduit sont pourcentage de batterie
					if("electrique".equals(engineType)) {
						batteryPercent -= 1;
					}
				}else {
					System.out.println("Batterie trop faible pour reculer");
					retour = -3;
				}

			}
		// on redessine le terrain
		terrain.repaint();
		// on effectue une petite pause fonction de la vitesse du robot
		try {
			Thread.sleep((int) (1000 / vitesse));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return retour;

	}

	/**
	 * Permet de faire pivoter le robot sur sa droite
	 * 
	 * @return 1 si le robot a pu tourner correctement, -2 si le robot n'a pas
	 *         pu tourner car il est détruit, -3 si le robot n'a plus de
	 *         batterie
	 */
	public int tournerDroite() {
		int retour = 1;

		if (robotDetruit)
			retour = -2;
		else {
			if("thermique".equals(engineType) || ("electrique".equals(engineType) && batteryPercent >=0.5)){
				compterObstacles(1);
				// on peut faire tourner le robot vers la droite
				if ("sud".equals(direction)) {
					direction = "ouest";
				} else if ("nord".equals(direction)) {
					direction = "est";
				} else if ("est".equals(direction)) {
					direction = "sud";
				} else if ("ouest".equals(direction)) {
					direction = "nord";
				}
				// Si il s'agit d'un robot electrique on reduit sont pourcentage de batterie
				if("electrique".equals(engineType)) {
					batteryPercent -= 0.5;
				}
			}else {
				System.out.println("Batterie trop faible pour tourner");
				retour = -3;
			}

		}
		// on redessine le terrain
		terrain.repaint();
		// on effectue une petite pause fonction de la vitesse du robot
		try {
			Thread.sleep((int) (1000 / vitesse));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return retour;
	}

	/**
	 * Permet de faire pivoter le robot sur sa droite
	 * 
	 * @return 1 si le robot a pu tourner correctement, -2 si le robot n'a pas
	 *         pu tourner car il est détruit, -3 si le robot n'a plus de
	 *         batterie
	 */
	public int tournerGauche() {
		int retour = 1;

		if (robotDetruit)
			retour = -2;
		else {
			if("thermique".equals(engineType) || ("electrique".equals(engineType) && batteryPercent >=0.5)){
				compterObstacles(1);
				// on peut faire tourner le robot vers la gauche
				if ("sud".equals(direction)) {
					direction = "est";
				} else if ("nord".equals(direction)) {
					direction = "ouest";
				} else if ("est".equals(direction)) {
					direction = "nord";
				} else if ("ouest".equals(direction)) {
					direction = "sud";
				}
				// Si il s'agit d'un robot electrique on reduit sont pourcentage de batterie
				if("electrique".equals(engineType)) {
					batteryPercent -= 0.5;
				}
			}else {
				System.out.println("Batterie trop faible pour tourner");
				retour = -3;
			}

		}

		// on redessine le terrain
		terrain.repaint();
		// on effectue une petite pause fonction de la vitesse du robot
		try {
			Thread.sleep((int) (1000 / vitesse));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return retour;
	}

	/**
	 * Teste la présence d'un obstacle devant le robot
	 * 
	 * @return true si un obstacle est présent devant le robot (le contours du
	 *         terrain est considéré comme un obstacle); false si aucun obstacle
	 *         n'est présent devant le robot
	 */

	public boolean isObstacleDevant() {

		// en fonction de la direction du robot, on teste s'il ne sort pas des
		// limites du terrain, et si la case devant lui n'est pas de type
		// Obstacle
		if ("nord".equals(direction)
				&& ligne > 0
				&& !(terrain.getGrille()[ligne - 1][colonne] instanceof Obstacle))
			return false;
		else if ("sud".equals(direction)
				&& ligne < terrain.getNbLignes() - 1
				&& !(terrain.getGrille()[ligne + 1][colonne] instanceof Obstacle))
			return false;
		else if ("ouest".equals(direction)
				&& colonne > 0
				&& !(terrain.getGrille()[ligne][colonne - 1] instanceof Obstacle))
			return false;
		else if ("est".equals(direction)
				&& colonne < terrain.getNbColonnes() - 1
				&& !(terrain.getGrille()[ligne][colonne + 1] instanceof Obstacle))
			return false;
		else 
			return true;


	}

	/**
	 * Méthode qui permet de détecter un obstacle devant le robot en consommant
	 * la batterie
	 * 
	 * @return true si un obstacle est présent devant le robot (le contours du
	 *         terrain est considéré comme un obstacle); false si aucun obstacle
	 *         n'est présent devant le robot
	 */
	public boolean isObstacleDevantAvecConsommationBatterie() {

		// en fonction de la direction du robot, on teste s'il ne sort pas des
		// limites du terrain, et si la case devant lui n'est pas de type
		// Obstacle
		if("electrique".equals(engineType) && batteryPercent<0.25) {
			System.out.println("Batterie insuffisante pour le repérage d'obstable");
			return false;
		}

		if ("nord".equals(direction)
				&& ligne > 0
				&& !(terrain.getGrille()[ligne - 1][colonne] instanceof Obstacle))
			return false;
		else if ("sud".equals(direction)
				&& ligne < terrain.getNbLignes() - 1
				&& !(terrain.getGrille()[ligne + 1][colonne] instanceof Obstacle))
			return false;
		else if ("ouest".equals(direction)
				&& colonne > 0
				&& !(terrain.getGrille()[ligne][colonne - 1] instanceof Obstacle))
			return false;
		else if ("est".equals(direction)
				&& colonne < terrain.getNbColonnes() - 1
				&& !(terrain.getGrille()[ligne][colonne + 1] instanceof Obstacle))
			return false;
		else {
			// Si il s'agit d'un robot electrique on reduit son pourcentage de batterie
			if("electrique".equals(engineType)) {
				batteryPercent -= 0.25;
			}
			return true;
		}
	}

	/**
	 * Teste la présence d'un obstacle à l'arrière le Robot
	 * 
	 * @return true si un obstacle est présent derrière le robot; false si aucun
	 *         obstacle n'est présent derrière le robot
	 */
	public boolean isObstacleDerriere() {

		// en fonction de la direction du robot, on teste s'il ne sort pas des
		// limites du terrain, et si la case derrière lui n'est pas de type
		// Obstacle
		if ("sud".equals(direction)
				&& ligne > 0
				&& !(terrain.getGrille()[ligne - 1][colonne] instanceof Obstacle))
			return false;
		else if ("nord".equals(direction)
				&& ligne < terrain.getNbLignes() - 1
				&& !(terrain.getGrille()[ligne + 1][colonne] instanceof Obstacle))
			return false;
		else if ("est".equals(direction)
				&& colonne > 0
				&& !(terrain.getGrille()[ligne][colonne - 1] instanceof Obstacle))
			return false;
		else if ("ouest".equals(direction)
				&& colonne < terrain.getNbColonnes() - 1
				&& !(terrain.getGrille()[ligne][colonne + 1] instanceof Obstacle))
			return false;

		else 
			return true;
	}

	/**
	 * Méthode qui permet de détecter un obstacle à l'arrière du robot en
	 * consommant la batterie
	 * 
	 * @return true si un obstacle est présent derrière le robot (le contours du
	 *         terrain est considéré comme un obstacle); false si aucun obstacle
	 *         n'est présent devant le robot
	 */
	public boolean isObstacleDerriereAvecConsommationBatterie() {

		// en fonction de la direction du robot, on teste s'il ne sort pas des
		// limites du terrain, et si la case derrière lui n'est pas de type
		// Obstacle
		if("electrique".equals(engineType) && batteryPercent<0.25) {
			System.out.println("Batterie insuffisante pour le repérage d'obstable");
			return false;
		}
		if ("sud".equals(direction)
				&& ligne > 0
				&& !(terrain.getGrille()[ligne - 1][colonne] instanceof Obstacle))
			return false;
		else if ("nord".equals(direction)
				&& ligne < terrain.getNbLignes() - 1
				&& !(terrain.getGrille()[ligne + 1][colonne] instanceof Obstacle))
			return false;
		else if ("est".equals(direction)
				&& colonne > 0
				&& !(terrain.getGrille()[ligne][colonne - 1] instanceof Obstacle))
			return false;
		else if ("ouest".equals(direction)
				&& colonne < terrain.getNbColonnes() - 1
				&& !(terrain.getGrille()[ligne][colonne + 1] instanceof Obstacle))
			return false;

		else {
			// Si il s'agit d'un robot electrique on reduit sont pourcentage de batterie
			if("electrique".equals(engineType)) {
				batteryPercent -= 0.25;
			}
			return true;
		}


	}

	/**
	 * Permet de détruire le robot
	 */
	public void detruireRobot() {
		// Si le robot a déjà subi 4 chocs, il est détruit
		if (chocsNumber >= 4) {
			robotDetruit = true;
			mettreAJourImage(getCheminImageRobotDetruit());
			terrain.repaint();
			System.out.println("Robot détruit !");
			return;
		}

		// Sinon, on incrémente le nombre de chocs
		chocsNumber++;

		// Animation visuelle du choc (rouge + image normale)
		for (int i = 0; i < 4; i++) {
			String cheminImage = (i % 2 == 0) ? getCheminImageRobotMed() : getCheminImageRobot();
			mettreAJourImage(cheminImage);
			terrain.repaint();
			try {
				Thread.sleep((int) (1000 / vitesse)); // petit délai pour rendre l’effet visible
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		System.out.println("Choc reçu (" + chocsNumber + "/4)");
	}

	/**
	 * Met à jour l'image du robot depuis un chemin donné
	 */
	private void mettreAJourImage(String cheminImage) {
		try {
			image = ImageIO.read(new File(cheminImage));
		} catch (IOException e) {
			System.err.println("Erreur lors du chargement de l'image : " + cheminImage);
			e.printStackTrace();
		}
	}

	/**
	 * Permet de savoir si le roboto se trouve sur une case victime
	 * 
	 * @return true si le robot se trouve sur une case victime; false si le
	 *         robot ne se trouve pas sur une case victime
	 */
	public boolean isSurVictime() {
		// on teste le type de la case du robot
		if (terrain.getGrille()[ligne][colonne] instanceof CaseVictime) {
			return true;
		} else
			return false;

	}

	/**
	 * Permet de sauver une victime, i.e. changer son état
	 * 
	 * @return 1 si la victime a été sauvée; -1 si aucune victime sur la case
	 *         courante du robot
	 */
	public int sauverVictime() {
		// si la case courante est de type victime
		if (terrain.getGrille()[ligne][colonne] instanceof CaseVictime) {
			// on sauve la victime
			((CaseVictime) terrain.getGrille()[ligne][colonne]).sauverVictime();
			return 1;
		} else
			return -1;

	}

	/**
	 * getter de l'attribut vitesse
	 * 
	 * @return l'attribut vitesse
	 */
	public double getVitesse() {
		return vitesse;
	}

	/**
	 * setter de l'attribut vitesse
	 */
	public void setVitesse(double vitesse) {
		this.vitesse = vitesse;
	}

	/**
	 * getter de l'attribut ligne
	 * 
	 * @return l'attribut ligne
	 */
	public int getLigne() {
		return ligne;
	}

	/**
	 * getter de l'attribut colonne
	 * 
	 * @return l'attribut colonne
	 */
	public int getColonne() {
		return colonne;
	}

	/**
	 * getter de l'attribut direction
	 * 
	 * @return l'attribut direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * getter de l'attribut batteryPercent
	 * 
	 * @return l'attribut batteryPercent
	 */
	public double getBatteryPercent() {
		return batteryPercent;
	}

	/**
	 * getter de l'attribut obstaclesEncountered
	 * 
	 * @return obstaclesEncountered
	 */

	public ArrayList<Integer[]> getObstaclesEncountered() {
		return obstaclesEncountered;
	}
	/**
	 * getter de l'attribut terrain
	 * 
	 * @return l'attribut terrain
	 */
	public Terrain getTerrain() {
		return terrain;
	}

	/**
	 * getter de robotDetruit
	 * 
	 * @return robotDetruit
	 */

	public boolean getRobotDetruit() {
		return robotDetruit;
	}
	/**
	 * setter de l'attribut terrain
	 */
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	/**
	 * Méthode permettant de modifier la couleur de tracer
	 * 
	 * @param trace
	 *            correspond au code de couleur : 1 pour le rouge, 2 pour le
	 *            bleu.
	 */
	public void tracer(int trace) {
		this.trace = trace;
		if (trace == 1) {
			terrain.getGrille()[ligne][colonne].setBackground(Color.RED);
			terrain.getGrille()[ligne][colonne].setVisitee(true);
		}
		if (trace == 2) {
			terrain.getGrille()[ligne][colonne].setBackground(Color.BLUE);
			terrain.getGrille()[ligne][colonne].setVisitee(true);
		}

	}

	/**
	 * Méthode permettant de détecter la gravité de l'état d'une victime.
	 * 
	 * @return la gravité de l'état de la victime entre 1 et 10 s'il y a une
	 *         victime sur la case; 0 s'il n'y a pas de victime.
	 */
	public int detecterGravite() {
		if (terrain.getGrille()[ligne][colonne].getClass().getName()
				.equals("CaseVictime"))
			return ((CaseVictime) terrain.getGrille()[ligne][colonne])
					.getGravite();
		else
			return 0;
	}

	/**
	 * Permet de récupérer l'état de la victime une fois que le robot est sur la
	 * victime
	 * 
	 * @return l'état de la victime ou un message indiquant que le robot n'a pas
	 *         encore atteint la victime
	 */
	public String getEtatVictime() {
		// on teste le type de la case du robot
		if (terrain.getGrille()[ligne][colonne] instanceof CaseVictime) {
			return ((CaseVictime) terrain.getGrille()[ligne][colonne])
					.getEtatVictime();
		} else
			return "Le robot n'a pas encore atteint la victime";
	}

	/**
	 * @return le chemin vers l'image du robot
	 */
	protected String getCheminImageRobot() {
		return "./data/robot.png";
	}

	/**
	 * @return le chemin vers l'image du robot détruit
	 */
	protected String getCheminImageRobotDetruit() {
		return "./data/robot_detruit.png";
	}
	protected String getCheminImageRobotMed() {
		return "./data/robot_medical.png";
	}
	/**
	 * Gère le comptage et l'affichage des obstacles rencontrés par le robot.
	 * 
	 * @param mode 1 = comptage lors d'une détection d'obstacle, 2 = affichage du rapport.
	 */

	public void compterObstacles(int mode) {
		if (isObstacleDevant() && mode == 1) {
			int obsLine = ligne;
			int obsCol = colonne;

			// Détermination de la position de l’obstacle selon la direction
			switch (direction) {
			case "sud":
				obsLine++;
				break;
			case "nord":
				obsLine--;
				break;
			case "est":
				obsCol++;
				break;
			case "ouest":
				obsCol--;
				break;
			}

			// Vérifie si l’obstacle a déjà été rencontré
			boolean found = false;
			for (Integer[] obs : obstaclesEncountered) {
				if (obs[0] == obsLine && obs[1] == obsCol) {
					obs[2]++; // Incrémente le compteur
					found = true;
					break;
				}
			}

			// Si c’est un nouvel obstacle, on l’ajoute à la liste
			if (!found && obsLine>=0 && obsCol>=0 && obsLine<10 && obsCol<10) {
				obstaclesEncountered.add(new Integer[]{obsLine, obsCol, 1});
			}
		}
		if(mode == 2) {
			// --- Affichage final ---

			System.out.println("\n===============================");
			System.out.println("      RAPPORT D'OBSTACLES");
			System.out.println("===============================\n");

			System.out.println("Nombre total d'obstacles rencontrés : " + obstaclesEncountered.size());
			System.out.println("Pourcentage de batterie restant    : " + batteryPercent + "%\n");

			if (obstaclesEncountered.isEmpty()) {
				System.out.println("Aucun obstacle rencontré pour le moment.");
			} else {
				System.out.println("Détails des obstacles rencontrés :\n");
				System.out.println(String.format("%-10s %-10s %-10s", "Ligne", "Colonne", "Rencontres"));
				System.out.println("------------------------------------------");
				for (Integer[] obs : obstaclesEncountered) {
					System.out.println(String.format("%-10d %-10d %-10d", obs[0], obs[1], obs[2]));
				}
			}

			System.out.println("\n===============================\n");
		}

	}

	/**
	 * recharge la batterie du robot mais seulement lorsqu'il est en position (0,0)
	 */
	public void chargerBatterie() {
		if(!(ligne==0 && colonne==0)) {
			System.out.println("La charge ne peux s'effectué qu'en (0,0)");
			return;
		}
		batteryPercent = 100;
	}
}