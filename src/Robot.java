import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		// si on avance alors qu'il y a un obstacle devant alors on détruit le
		// robot
		if (isObstacleDevant()) {

			detruireRobot();
			retour = -1;
		} else {
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
		// TODO à compléter
		return true;
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
		// TODO à compléter
		return true;

	}

	/**
	 * Permet de détruire le robot
	 */
	public void detruireRobot() {
		// on indique que le robot est détruit
		robotDetruit = true;
		// on modifie son image pour voir une image de robot détruit
		try {
			image = ImageIO.read(new File(getCheminImageRobotDetruit()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// on redessine le terrain
		terrain.repaint();
		System.out.println("Robot Détruit !");
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
	 * getter de l'attribut terrain
	 * 
	 * @return l'attribut terrain
	 */
	public Terrain getTerrain() {
		return terrain;
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

}