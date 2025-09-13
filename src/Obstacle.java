import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe représentant une case contenant un mur (obstacle)
 * 
 * @author Département TIC - ESIGELEC
 * @version 1.0
 */
public class Obstacle extends Case {

	/**
	 * numéro de version de la classe
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * image qui représente un obstacle
	 */
	private static BufferedImage image;

	/**
	 * Constructeur de la classe Obstacle
	 * 
	 * @param t
	 *            le terrain sur lequel se trouve l'obstacle
	 * @param ligne
	 *            numero de la ligne de l'obstacle
	 * @param colonne
	 *            numero de la colonne de l'obstacle
	 */
	public Obstacle(Terrain t, int ligne, int colonne) {
		// appel du constructeur de la classe mère (Case)
		super(t, ligne, colonne);

		// initialisation de l'attribut image à partir d'un fichier de type
		// image représentant un obstacle à l'écran
		if (image == null) {
			try {
				image = ImageIO.read(new File("./data/obstacle.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * redéfinition de la méthode paintComponent afin de préciser comment un
	 * obstacle doit s'afficher
	 * 
	 * @param g
	 *            Objet Graphics associé à la Case
	 */
	protected void paintComponent(Graphics g) {
		// appel de la méthode de dessin d'un JPanel normal
		super.paintComponent(g);
		// on ajoute le dessin de l'image de l'obstacle
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

	}

}