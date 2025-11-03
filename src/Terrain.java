import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JPanel;

/**
 * Classe représentant le Terrain
 * 
 * @author Département TIC - ESIGELEC
 * @version 1.0
 */
public class Terrain extends JPanel {
	/**
	 * numéro de version de la classe
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * nombre de lignes du terrain
	 */
	private int nbLignes;
	/**
	 * nombre de colonnes du terrain
	 */
	private int nbColonnes;
	/**
	 * tableau de cases contenant la liste des objets "Case" du Terrain
	 */
	private Case[][] grille;
	/**
	 * référence du robot qui se trouve sur le terrain
	 */
	private Robot robot;

	/**
	 * Contructeur de la classe Terrain
	 * 
	 * @param nbLignes
	 *            nombre de lignes du terrain
	 * @param nbColonnes
	 *            nombre de colonnes du terrain
	 */
	public Terrain(int nbLignes, int nbColonnes) {
		super();
		// on initialise les attributs de la classe Terrain
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		// on fixe le gestionnaire de placement à GridLayout (une grille avec
		// des bordures de 1px)
		this.setLayout(new GridLayout(nbLignes, nbColonnes, 1, 1));
		// on crée la grille contenant les cases du terrain
		// pour le moment toutes les cases sont vides
		grille = new Case[nbLignes][nbColonnes];
		for (int i = 0; i < nbLignes; i++)
			for (int j = 0; j < nbColonnes; j++)
				grille[i][j] = new CaseVide(this, i, j);

	}

	/**
	 * Permet d'ajouter le robot sur le terrain
	 * 
	 * @param r
	 *            le robot à ajouter
	 */
	public void ajouterRobot(Robot r) {
		// on mémorise le robot ajouté
		robot = r;
		// on fixe le terrain associé au robot
		r.setTerrain(this);
		// la case de départ du Robot a un type différent que l'on change :
		// "CaseDepart"
		this.grille[r.getLigne()][r.getColonne()] = new CaseDepart(this,
				r.getLigne(), r.getColonne());
		// on met à jour les composants graphiques du terrain
		this.updateIHM();

		// on fait une pause de 2 secondes pour ne pas que le robot parte trop
		// rapidement
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Permet d'ajouter des obstacles sur le terrain avec des positions
	 * aléatoires
	 * 
	 * @param nbObstacles
	 *            nombre d'obstacles qui seront ajoutés au maximum
	 */
	public void ajouterObstaclesAleatoires(int nbObstacles) {

		// On instancie la classe qui permet de générer des nombres
		// pseudo-aléatoires
		Random r = new Random();
		// on place les obstacles
		for (int i = 0; i < nbObstacles; i++) {
			int l = r.nextInt(nbLignes);
			int c = r.nextInt(nbColonnes);
			// on crée un obstacle et on le met à la bonne place dans la grille
			grille[l][c] = new Obstacle(this, l, c);
		}
		// on met à jour les composants graphiques du terrain
		// updateIHM();

	}

	/**
	 * Permet d'ajouter des obstacles isolés sur le terrain avec des positions
	 * aléatoires. Isolé signifie que le robot peut faire le tour tous les
	 * l'obtacles. Il ne sont pas au bord du terrain ni à côté d'un autre
	 * obstacle.
	 * 
	 * @param nbObstacles
	 *            nombre d'obstacles qui seront ajoutés au maximum
	 */
	public void ajouterObstaclesAleatoiresIsoles(int nbObstacles) {

		// On instancie la classe qui permet de générer des nombres
		// pseudo-aléatoires
		Random r = new Random();
		// on place les obstacles
		for (int i = 0; i < nbObstacles; i++) {
			int l = r.nextInt(nbLignes);
			int c = r.nextInt(nbColonnes);

			if (!isObstacleACote(l, c)) {
				// on crée un obstacle et on le met à la bonne place dans la
				// grille
				grille[l][c] = new Obstacle(this, l, c);
			}
		}

	}

	/**
	 * Permet d'ajouter des obstacles isolés avec possibilité sur les bords du
	 * terrain avec des positions aléatoires. Isolé signifie que le robot peut
	 * faire le tour tous les l'obtacles. Les obstacles peuvent cependant être
	 * situés sur les bords.
	 * 
	 * @param nbObstacles
	 *            nombre d'obstacles qui seront ajoutés au maximum
	 */
	public void ajouterObstaclesAleatoiresIsolesAvecBords(int nbObstacles) {

		// On instancie la classe qui permet de générer des nombres
		// pseudo-aléatoires
		Random r = new Random();
		// on place les obstacles
		for (int i = 0; i < nbObstacles; i++) {
			int l = r.nextInt(nbLignes);
			int c = r.nextInt(nbColonnes);

			if (!isObstacleACoteNonBord(l, c) && !(l==0 && c==0)) {
				// on crée un obstacle et on le met à la bonne place dans la
				// grille
				grille[l][c] = new Obstacle(this, l, c);
			}
		}

	}

	/**
	 * Permet d'ajouter un obstacle sur le terrain à une position donnée
	 * 
	 * @param ligne
	 *            la ligne de l'obstacle à ajouter (commence à 0)
	 * @param colonne
	 *            la colonne de l'obstacle à ajouter (commence à 0)
	 */
	public void ajouterObstacle(int ligne, int colonne) {

		// on crée l' obstacle et on le met à la bonne place dans la grille
		grille[ligne][colonne] = new Obstacle(this, ligne, colonne);

	}

	/**
	 * Permet d'ajouter une case vide sur le terrain à une position donnée
	 * 
	 * @param ligne
	 *            la ligne de la case vide à ajouter (commence à 0)
	 * @param colonne
	 *            la colonne de la case vide à ajouter (commence à 0)
	 */
	public void ajouterCaseVide(int ligne, int colonne) {

		// on crée une case vide et on la met à la bonne place dans la grille
		grille[ligne][colonne] = new CaseVide(this, ligne, colonne);
	}

	/**
	 * Permet d'ajouter un mur sur une colonne donnée
	 * 
	 * @param colonne
	 *            la colonne où le mûr est placé (commence à 0)
	 */
	public void ajouterMur(int colonne) {

		// On instancie la classe qui permet de générer des nombres
		// pseudo-aléatoires
		Random r = new Random();
		int l = r.nextInt(nbLignes);
		// on place les obstacles
		for (int i = 0; i < 10; i++) {

			if (i != l || colonne==9)
				// on crée un obstacle et on le met à la bonne place dans la
				// grille
				grille[i][colonne] = new Obstacle(this, i, colonne);
		}

	}

	/**
	 * Permet d'ajouter un mur avec 2 portes
	 * 
	 * @param colonne
	 *            numero de colonne (commence à 0)
	 * 
	 */

	public void ajouterMurDeuxPortes(int colonne) {

		// On instancie la classe qui permet de générer des nombres
		// pseudo-aléatoires
		Random r = new Random();
		int l = r.nextInt(5);

		int m = 9 - r.nextInt(5);
		// on place les obstacles
		for (int i = 0; i < 10; i++) {

			if (((i != l) && (i != m)) || colonne==9)
				// on crée un obstacle et on le met à la bonne place dans la
				// grille
				grille[i][colonne] = new Obstacle(this, i, colonne);
		}

	}

	/**
	 * Permet d'ajouter tous les murs avec une seule porte
	 * 
	 * 
	 */
	public void ajouterTousLesMurs() {
		for (int i = 1; i < 10; i = i + 2)
			ajouterMur(i);

	}

	/**
	 * Permet d'ajouter tous les murs avec deux portes
	 * 
	 * 
	 */

	public void ajouterTousLesMursDeuxPortes() {
		for (int i = 1; i < 10; i = i + 2)
			ajouterMurDeuxPortes(i);

	}
	
	/**
	 * Permet d'ajouter une salle centrale avec une porte sur un des 4 murs
	 */
	public void ajouterSalleCentrale() {
		// Generate the 4 full walls
		genererMursSalleCentrale();
		
		// Insert a door
		Random r = new Random();
		int x = r.nextInt(4) + 3; // range 3..6
		int y = r.nextInt(2) + 1; // 1 or 2
		
		if (x == 3 || x == 6) {
			// Vertical wall, so y must be 4 or 5
			y += 3;
		} else {
			// Horizontal walls, so y must be 3 or 6
			y *= 3;
		}
		this.ajouterCaseVide(x, y);
	}

	/**
	 * Permet d'ajouter une salle centrale avec une porte sur le 1er mur horizontal
	 */
	public void ajouterSalleCentraleSimple() {
		// Generate the 4 full walls
		genererMursSalleCentrale();
		
		// Insert a door either at (3,4) or (3,5)
		Random r = new Random();
		this.ajouterCaseVide(3, 4 + r.nextInt(2));
	}
	
	/**
	 * Permet de générer les 4 murs d'une salle au centre du terrain (sans porte)
	 */
	private void genererMursSalleCentrale() {
		for (int i = 0; i < 4; i++) {
			this.ajouterObstacle(3, 3 + i);
			this.ajouterObstacle(6, 3 + i);
			this.ajouterObstacle(3 + i, 3);
			this.ajouterObstacle(3 + i, 6);
		}
	}

	/**
	 * Permet d'ajouter deux salles avec une porte
	 * 
	 * 
	 */

	public void ajouterDeuxSalles() {
		Random r = new Random();

		int k = r.nextInt(3);
		int m = r.nextInt(3);

		for (int i = 0; i < 4; i++) {
			this.ajouterObstacle(i, 6);
			this.ajouterObstacle(3, 9 - i);
			this.ajouterObstacle(6, 9 - i);
			this.ajouterObstacle(9 - i, 6);
		}

		this.ajouterCaseVide(m, 6);
		this.ajouterCaseVide(9 - k, 6);

	}

	
	/**
	 * Permet d'ajouter quatre salles avec une porte 
	 * 
	 *            
	 */
	
	public void ajouterQuatreSalles()
	{
		Random r = new Random();
		int l = r.nextInt(3);
		int k = r.nextInt(3);
		int m = r.nextInt(3);
		int n = r.nextInt(3);	
		for(int i=0;i<4;i++)
		{
			
			
			this.ajouterObstacle(3,i);
			
			this.ajouterObstacle(i,3);		
			this.ajouterObstacle(6,i);
			this.ajouterObstacle(i,6);
			this.ajouterObstacle(3,9-i);
			this.ajouterObstacle(9-i,3);
			this.ajouterObstacle(6,9-i);
			this.ajouterObstacle(9-i,6);

            

		}

		
		this.ajouterCaseVide(l, 3);
		this.ajouterCaseVide(9-k, 3);
		this.ajouterCaseVide(m, 6);
		this.ajouterCaseVide(9-n, 6);

		
		
	}
	/**
	 * Permet de tester la presence d'un obstacle à la position indiquée. La
	 * bordure du terrain est aussi considérée comme étant un obstacle
	 * 
	 * @param ligne
	 *            ligne de la case testée
	 * @param colonne
	 *            colonne de la case testée
	 * @return true si un obstacle est présent
	 * @return false si aucun obstacle n'est présent
	 */
	private boolean isObstacle(int ligne, int colonne) {
		// on considère la bordure du terrain comme un obstacle
		if (ligne < 0 || ligne >= this.nbLignes || colonne < 0
				|| colonne >= this.nbColonnes)
			return true;
		else if (grille[ligne][colonne] instanceof Obstacle)
			return true;
		else
			return false;

	}

	/**
	 * Permet de tester la presence d'un obstacle à la position indiquée. La
	 * bordure du terrain n'est pas considérée comme étant un obstacle
	 * 
	 * @param ligne
	 *            ligne de la case testée
	 * @param colonne
	 *            colonne de la case testée
	 * @return true si un obstacle est présent
	 * @return false si aucun obstacle n'est présent
	 */
	private boolean isObstacleNonBord(int ligne, int colonne) {
		// on ne considère pas la bordure du terrain comme un obstacle
		if (ligne < 0 || ligne >= this.nbLignes || colonne < 0
				|| colonne >= this.nbColonnes)
			return false;
		else if (grille[ligne][colonne] instanceof Obstacle)
			return true;
		else
			return false;

	}

	/**
	 * Permet de tester la présence d'un obstacle à coté de la position
	 * indiquée. La bordure du terrain est aussi considérée comme étant un
	 * obstacle.
	 * 
	 * @param ligne
	 *            ligne de la case testée
	 * @param colonne
	 *            colonne de la case testée
	 * @return true si un obstacle est présent juste à coté de la case testée
	 *         (diagonales comprises)
	 * @return false si aucun obstacle n'est présent juste à coté de la case
	 *         testée (diagonales comprises)
	 */
	private boolean isObstacleACote(int ligne, int colonne) {
		// on teste les 8 cases autour de la case testée
		if (isObstacle(ligne - 1, colonne))
			return true;
		else if (isObstacle(ligne - 1, colonne + 1))
			return true;
		else if (isObstacle(ligne, colonne + 1))
			return true;
		else if (isObstacle(ligne + 1, colonne + 1))
			return true;
		else if (isObstacle(ligne + 1, colonne))
			return true;
		else if (isObstacle(ligne + 1, colonne - 1))
			return true;
		else if (isObstacle(ligne, colonne - 1))
			return true;
		else if (isObstacle(ligne - 1, colonne - 1))
			return true;
		else
			return false;

	}

	/**
	 * Permet de tester la présence d'un obstacle à coté de la position
	 * indiquée. La bordure du terrain n'est pas considérée comme étant un
	 * obstacle.
	 * 
	 * @param ligne
	 *            ligne de la case testée
	 * @param colonne
	 *            colonne de la case testée
	 * @return true si un obstacle est présent juste à coté de la case testée
	 *         (diagonales comprises)
	 * @return false si aucun obstacle n'est présent juste à coté de la case
	 *         testée (diagonales comprises)
	 */
	private boolean isObstacleACoteNonBord(int ligne, int colonne) {
		// on teste les 8 cases autour de la case testée
		if (isObstacleNonBord(ligne - 1, colonne))
			return true;
		else if (isObstacleNonBord(ligne - 1, colonne + 1))
			return true;
		else if (isObstacleNonBord(ligne, colonne + 1))
			return true;
		else if (isObstacleNonBord(ligne + 1, colonne + 1))
			return true;
		else if (isObstacleNonBord(ligne + 1, colonne))
			return true;
		else if (isObstacleNonBord(ligne + 1, colonne - 1))
			return true;
		else if (isObstacleNonBord(ligne, colonne - 1))
			return true;
		else if (isObstacleNonBord(ligne - 1, colonne - 1))
			return true;
		else
			return false;

	}

	/**
	 * Permet d'ajouter une victime sur le terrain à une position aléatoire
	 * (sauf case 0,0)
	 * 
	 */
	public void ajouterVictimePositionAleatoire() {
		Random r = new Random();
		int l = r.nextInt(nbLignes);
		int c = r.nextInt(nbColonnes);
		// si c'est la case 0,0 qui est tirée on déplace la victime sur la case
		// 0,1
		if (l == 0 && c == 0)
			c = 1;
		// on crée une case de type Victime et on l'ajoute à la grille
		grille[l][c] = new CaseVictime(this, l, c);
		// on met à jour les composants graphiques du terrain
		// updateIHM();

	}
	
	
	
	
	
		

	/**
	 * Permet d'ajouter plusieurs entre 3 et 5 victimes placées aléatoirement
	 * 
	 */
	public void ajouterPlusieursVictimes() {
		Random alea = new Random();
		int nombreVictimes = alea.nextInt(3) + 3;
		int l;
		int c;
		int compteur = 0;

		while (compteur != nombreVictimes) {
			l = alea.nextInt(nbLignes);
			c = alea.nextInt(nbColonnes);
			// si != (0,0)
			if (l != 0 || c != 0) {
				// S'il ne s'agit pas d'une case avec une victime, on en place
				// une
				if (!(this.grille[l][c] instanceof CaseVictime)) {
					
					grille[l][c] = new CaseVictime(this, l, c);
					compteur++;
				}
			}
		}

		// updateIHM();
	}

	
	
	/**
	 * Permet d'ajouter plusieurs entre un nombre de victimes placées aléatoirement
	 * @param nombre Correspond au nombre de vicitmes à placer (maximum 10)
	 */
	public void ajouterNombreDeVictimes(int nombre) {
		if(nombre > 10)
			nombre = 10;
		Random alea = new Random();
		int l;
		int c;
		int compteur = 0;

		while (compteur < nombre) {
			l = alea.nextInt(nbLignes);
			c = alea.nextInt(nbColonnes);
			// si != (0,0)
			if (l != 0 || c != 0) {
				// S'il ne s'agit pas d'une case avec une victime, on en place
				// une
				if (!(this.grille[l][c] instanceof CaseVictime)) {
					
					grille[l][c] = new CaseVictime(this, l, c);
					compteur++;
				}
			}
		}
		// updateIHM();
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Permet d'ajouter une victime sur le terrain
	 * 
	 * @param ligne
	 *            ligne de la victime (commence à 0)
	 * @param colonne
	 *            colonne de la victime (commence à 0)
	 */
	public void ajouterVictime(int ligne, int colonne) {
		// on crée une case de type Victime et on l'ajoute à la grille
		grille[ligne][colonne] = new CaseVictime(this, ligne, colonne);
		// on met à jour les composants graphiques du terrain
		// updateIHM();

	}

	/**
	 * Permet d'ajouter une victime sur le terrain à une position aléatoire sur
	 * la colonne 9
	 * 
	 */
	public void ajouterVictimePositionAleatoireColonne9() {
		Random r = new Random();
		int l = r.nextInt(nbLignes);
		int c = 8;
		// si c'est la case 0,0 qui est tirée on déplace la victime sur la case
		// 0,1
		if (l == 0 && c == 0)
			c = 1;
		// on crée une case de type Victime et on l'ajoute à la grille
		grille[l][c] = new CaseVictime(this, l, c);
		// on met à jour les composants graphiques du terrain
		// updateIHM();

	}
	
	/**
	 * Permet d'ajouter aléatoirement une victime dans la salle centrale
	 */

	public void ajouterVictimeSalleCentrale() {

		Random r = new Random();
		int l = r.nextInt(2) + 4;
		int c = r.nextInt(2) + 4;
		grille[l][c] = new CaseVictime(this, l, c);

	}

	/**
	 * Perme d'ajouter aléatoirement une victime dans l'une des deux salles
	 */

	public void ajouterVictimeDeuxSalle() {

		Random r = new Random();
		int l = r.nextInt(3);
		int c = r.nextInt(3);
		int w = r.nextInt(2);
		// si c'est la case 0,0 qui est tirée on déplace la victime sur la case
		// 0,1
		// on crée une case de type Victime et on l'ajoute à la grille
		if (w == 1)
			grille[9 - l][9 - c] = new CaseVictime(this, 9 - l, 9 - c);
		// on met à jour les composants graphiques du terrain
		// updateIHM();
		// if (w==1)
		// grille[7+l][c] = new CaseVictime(this, 7+l, c);
		if (w == 0)
			grille[l][7 + c] = new CaseVictime(this, l, 7 + c);

	}
	
	/**
	 * Perme d'ajouter aléatoirement une victime dans l'une des quatres salles
	 */
public void ajouterVictimeQuatreSalles(){
		
		Random r=new Random();
		int l = r.nextInt(3);
		int c = r.nextInt(3);
		int w = r.nextInt(3);
		//si c'est la case 0,0 qui est tirée on déplace la victime sur la case 0,1
				// on crée une case de type Victime et on l'ajoute à la grille
		if (w==2)
		  grille[9-l][9-c] = new CaseVictime(this, 9-l, 9-c);
		// on met à jour les composants graphiques du terrain
		//updateIHM();
		if (w==1)
			  grille[7+l][c] = new CaseVictime(this, 7+l, c);
		if (w==0)
			  grille[l][7+c] = new CaseVictime(this, l, 7+c);
		
	}

	/**
	 * Permet de mettre à jour les cases graphiques à partir de la grille de
	 * cases
	 */
	public void updateIHM() {
		// on enleve tous les composants graphiques du terrain (les cases)
		this.removeAll();

		// on ajoute toutes les cases sur le terrain
		for (int i = 0; i < nbLignes; i++)
			for (int j = 0; j < nbColonnes; j++) {
				this.add(grille[i][j]);
			}
		// force la mise à jour des composants graphiques
		this.validate();
		// on redessine le terrain
		this.repaint();

	}

	/**
	 * getter de l'attribut nbColonnes
	 * 
	 * @return l'attribut nbColonnes
	 */
	public int getNbColonnes() {
		return nbColonnes;
	}

	/**
	 * getter de l'attribut grille
	 * 
	 * @return l'attribut grille
	 */
	public Case[][] getGrille() {
		return grille;
	}

	/**
	 * getter de l'attribut robot
	 * 
	 * @return l'attribut robot
	 */
	public Robot getRobot() {
		return robot;
	}

	/**
	 * getter de l'attribut nbLignes
	 * 
	 * @return l'attribut nbLignes
	 */
	public int getNbLignes() {
		return nbLignes;
	}

}