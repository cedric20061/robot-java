import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Classe repr�sentant la fen�tre du simulateur
 * 
 * @author D�partement TIC - ESIGELEC
 * @version 1.0
 */
public class Fenetre extends JFrame implements ActionListener {

	/**
	 * num�ro de version de la classe
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * container principal de la fen�tre
	 */
	Container container;
	/**
	 * Terrain situ� sur le simulateur
	 */
	Terrain terrain;
	/**
	 * Panneau de contr�le du simulateur
	 */
	PanneauDeControle controle;
	/**
	 * texte affich� en haut du simulateur
	 */
	JLabel labelDescription;

	JMenuBar menu;
	JMenu menuFichier;
	JMenuItem menuChangerCouleur = new JMenuItem("Modifier la couleur du terrain");
	JMenuItem menuPanneauDeControle = new JMenuItem("Afficher/Masquer le panneau de contr�le");

	/**
	 * Constructeur de la fen�tre
	 * 
	 * @param largeur largeur de la fen�tre en pixel
	 * @param hauteur hauteur de la fen�tre en pixel
	 */
	public Fenetre(int largeur, int hauteur) {
		super();
		// on fixe la taille de la fen�tre
		this.setSize(largeur, hauteur);

		menu = new JMenuBar();
		menuFichier = new JMenu("Fichier");
		menu.add(menuFichier);
		menuFichier.add(menuChangerCouleur);
		menuFichier.add(menuPanneauDeControle);

		this.setJMenuBar(menu);

		// on r�cup�re le container principal de la fen�tre
		container = this.getContentPane();
		// on modifie la couleur de fond du container
		container.setBackground(Color.WHITE);

		// on d�sactive le layout manager (gestionnaire de placement) du
		// container
		// afin de pouvoir placer les composants au pixel pr�s
		container.setLayout(null);
		// on d�finit le texte de la fen�tre
		// labelDescription = new JLabel(
		// "Simulateur de Robot sauveteur en milieu hostile");
		// on d�finit la position et la taille du texte de la fen�tre
		// labelDescription.setBounds(5, 5, 780, 20);
		// on ajoute le composant texte dans le container de la fen�tre
		// this.add(labelDescription);

		menuChangerCouleur.addActionListener(this);
		menuPanneauDeControle.addActionListener(this);
		// on pr�cise que le programme doit fermer compl�tement si on clique sur
		// la croix de la fen�tre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Permet d'ajouter le terrain dans le container principal de la fen�tre
	 * 
	 * @param t le terrain � ajouter
	 */
	public void ajouterTerrain(Terrain t) {
		terrain = t;
		// on ajoute le composant graphique du terrain dans le container de la
		// fenetre
		container.add(terrain);
		// on redessine la fen�tre
		repaint();
	}

	/**
	 * Permet d'ajouter le panneau de contr�le dans la fen�tre
	 * 
	 * @param pc le panneau de contr�le � ajouter
	 */
	public void ajouterPanneauDeControle(PanneauDeControle pc) {
		controle = pc;
		// on ajoute le composant graphique du panneau de contr�le dans le
		// container de la fenetre
		container.add(controle);
		// on redessine la fen�tre
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuChangerCouleur) {
			Color c = JColorChooser.showDialog(this, "choisissez la couleur du terrain", CaseVide.couleurCaseVideParDefaut);
			if (c != null)
				CaseVide.couleurCaseVideParDefaut = c;
			terrain.updateIHM();

			// Cr�ation d'une instance de la classe Properties
			Properties properties = new Properties();

			// Chemin du fichier .properties
			String fichier = "config.properties";

			// Ajout de propri�t�s
			properties.setProperty("couleurCaseVide", "" + CaseVide.couleurCaseVideParDefaut.getRGB());

			FileOutputStream output = null;
			try {
				output = new FileOutputStream(fichier);
				// Sauvegarde des propri�t�s dans le fichier
				properties.store(output, "Configuration d'application");
				// System.out.println("Fichier .properties sauvegard� avec succ�s.");
			} catch (IOException ee) {
				ee.printStackTrace();
			} finally {
				try {
					output.close();
				} catch (IOException e1) {
				}
			}

		} else if (e.getSource() == menuPanneauDeControle) {

			if (controle.isVisible()) {
				controle.setVisible(false);
				this.setSize(640, 700);
			} else {
				controle.setVisible(true);
				this.setSize(1050, 700);
			}

		}

	}

}