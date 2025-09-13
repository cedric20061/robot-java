import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Classe représentant une case du Terrain
 * 
 * @author Département TIC - ESIGELEC
 * @version 1.0
 */
public class Case extends JPanel {

	/**
	 * numéro de version de la classe
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * terrain sur lequel se trouve la case
	 */
	private Terrain terrain;
	/**
	 * numéro de ligne de la case
	 */
	private int ligne;
	/**
	 * numéro de colonne de la case
	 */
	private int colonne;
	/**
	 * permet de savoir si la case a déjà été visitée par le robot
	 */
	private boolean visitee = false;

	/**
	 * Constructeur de la classe Case
	 * 
	 * @param t
	 *            terrain sur lequel se trouve cette case
	 * @param ligne
	 *            numéro de la ligne de la case (les n° commencent à 0)
	 * @param colonne
	 *            numéro de la colonne de la case (les n° commencent à 0)
	 */
	public Case(Terrain terrain, int ligne, int colonne) {
		super();
		this.terrain = terrain;
		this.ligne = ligne;
		this.colonne = colonne;
	}

	@Override
	/**
	 * redéfinition de la méthode de dessin d'un JPanel 
	 * @param g objet Graphics associé à cette case
	 */
	protected void paintComponent(Graphics g) {
		// appel de la méthode de dessin d'un JPanel normal
		super.paintComponent(g);
		// récupération de l'éventuel Robot qui se trouve sur la case
		Robot r = this.getRobot();
		// si un robot se trouve sur la case on le dessine
		// en appelant la méthode dessiner du Robot
		if (r != null)
			r.dessiner(g, this.getWidth(), this.getHeight());

	}

	/**
	 * Permet de récupérer le robot qui se trouve sur la case courante s'il y en
	 * a un
	 * 
	 * @return Le Robot qui se trouve sur la case null si aucun robot ne se
	 *         trouve sur la case
	 */
	public Robot getRobot() {
		if (terrain.getRobot() == null)
			return null;
		// si un robot se trouve sur cette case, on le retourne, sinon on
		// retourne null
		else if (terrain.getRobot().getLigne() == this.ligne
				&& terrain.getRobot().getColonne() == this.colonne)
			return terrain.getRobot();
		else
			return null;
	}

	/**
	 * setter de l'attribut visitee
	 * 
	 * @param visitee
	 *            nouvelle valeur de l'attribut visitee
	 */
	public void setVisitee(boolean visitee) {
		this.visitee = visitee;
	}

	/**
	 * getter de l'attribut visitee, permet de récupérer la valeur de l'attribut
	 * 
	 * @return l'attribut visitee
	 */
	public boolean isVisitee() {
		return visitee;
	}

}