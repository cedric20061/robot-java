import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Classe représentant la case contenant une victime
 * 
 * @author Département TIC - ESIGELEC
 * @version 1.0
 */
public class CaseVictime extends Case {

	/**
	 * numéro de version de la classe
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * image qui représente la victime
	 */
	private BufferedImage imageVictime;

	/**
	 * Permet de préciser l'état de la victime (saignement, asphyxie, fracture,
	 * arret cardiaque, sauvee)
	 */
	private String etatVictime;

	/**
	 * Contient la gravité (entre 1 et 10) de l'état de la victime.
	 */
	private int gravite;

	/**
	 * Constructeur de la classe Victime
	 * 
	 * @param terrain
	 *            le terrain sur lequel se trouve la victime
	 * @param ligne
	 *            numero de la ligne où se trouve la victime
	 * @param colonne
	 *            numero de la colonne où se trouve la victime
	 */
	public CaseVictime(Terrain terrain, int ligne, int colonne) {
		// appel du constructeur de la classe mère (Case)
		super(terrain, ligne, colonne);

		// l'état de la victime est généré aléatoirement
		Random rand = new Random();
		gravite = rand.nextInt(10) + 1;
		switch (rand.nextInt(4)) {

		case 0:
			etatVictime = "saignement";
			break;
		case 1:
			etatVictime = "asphyxie";
			break;
		case 2:
			etatVictime = "fracture";
			break;
		case 3:
			etatVictime = "arret cardiaque";
			break;
		}

		// on fixe la couleur de fond de la case de la victime
		this.setBackground(CaseVide.couleurCaseVideParDefaut);

		// initialisation de l'attribut image à partir d'un fichier de type
		// image représentant une victime à l'écran
		if (imageVictime == null) {
			
			try {// a corriger
				switch (etatVictime) {
				case "asphyxie" : 
					imageVictime = ImageIO.read(new File("./data/victime_violette.png"));
					break;
				case "saignement" : 
					imageVictime = ImageIO.read(new File("./data/victime_rouge.png"));
					break;
				case "fracture" : 
					imageVictime = ImageIO.read(new File("./data/victime_bleue.png"));
					break;
				case "arret cardiaque" : 
					imageVictime = ImageIO.read(new File("./data/victime_jaune.png"));
					break;
				default : 
					imageVictime = ImageIO.read(new File("./data/victime.png"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

	/**
	 * redéfinition de la méthode paintComponent afin de préciser comment une
	 * victime doit s'afficher à l'écran
	 * 
	 * @param g
	 *            Objet Graphics associé à la Case Victime
	 */
	protected void paintComponent(Graphics g) {
		// appel de la méthode de dessin d'un JPanel normal
		super.paintComponent(g);
		// si la victime est sauvée on modifie la couleur de fond de la case
		if ("sauvee".equals(etatVictime)) {
			this.setBackground(Color.GREEN);
			// on dessine l'image de la victime
			g.drawImage(imageVictime, 0, 0, this.getWidth(), this.getHeight(),
					null);
		} else {
			// on fixe la couleur de fond de la case de la victime
			this.setBackground(CaseVide.couleurCaseVideParDefaut);
			// on dessine l'image de la victime
			g.drawImage(imageVictime, 0, 0, this.getWidth(), this.getHeight(),
					null);
		}

	}

	/**
	 * Permet de mémoriser que la victime a été sauvée
	 */
	public void sauverVictime() {

		etatVictime = "sauvee";
		System.out.println("Victime sauvée !");
	}

	/**
	 * Getter de l'attribut etatVictime
	 * 
	 * @return etatVicitime
	 */
	public String getEtatVictime() {
		return etatVictime;
	}

	/**
	 * Getter de l'attribut gravite
	 * 
	 * @return gravite
	 */
	public int getGravite() {
		return gravite;
	}

}