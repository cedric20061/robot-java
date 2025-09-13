import java.awt.Color;

/**
 * Classe représentant la case de départ du Robot
 * 
 * @author Département TIC - ESIGELEC
 * @version 1.0
 */
public class CaseDepart extends Case {

	/**
	 * numéro de version de la classe
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de la classe
	 * 
	 * @param t
	 *            le terrain sur lequel se trouve la case
	 * @param ligne
	 *            numero de la ligne de la case
	 * @param colonne
	 *            numero de la colonne de la case
	 */
	public CaseDepart(Terrain t, int ligne, int colonne) {
		// appel du constructeur de la classe mère (Case)
		super(t, ligne, colonne);
		// on fixe la couleur de fond de la case de depart
		this.setBackground(Color.RED);
	}

}