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
 * Classe représentant la fenêtre du simulateur
 * 
 * @author Département TIC - ESIGELEC
 * @version 1.0
 */
public class Fenetre extends JFrame implements ActionListener {

	/**
	 * numéro de version de la classe
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * container principal de la fenêtre
	 */
	Container container;
	/**
	 * Terrain situé sur le simulateur
	 */
	Terrain terrain;
	/**
	 * Panneau de contrôle du simulateur
	 */
	PanneauDeControle controle;
	/**
	 * texte affiché en haut du simulateur
	 */
	JLabel labelDescription;

	JMenuBar menu;
	JMenu menuFichier;
	JMenuItem menuChangerCouleur = new JMenuItem("Modifier la couleur du terrain");
	JMenuItem menuPanneauDeControle = new JMenuItem("Afficher/Masquer le panneau de contrôle");

	/**
	 * Constructeur de la fenêtre
	 * 
	 * @param largeur largeur de la fenêtre en pixel
	 * @param hauteur hauteur de la fenêtre en pixel
	 */
	public Fenetre(int largeur, int hauteur) {
		super();
		// on fixe la taille de la fenêtre
		this.setSize(largeur, hauteur);

		menu = new JMenuBar();
		menuFichier = new JMenu("Fichier");
		menu.add(menuFichier);
		menuFichier.add(menuChangerCouleur);
		menuFichier.add(menuPanneauDeControle);

		this.setJMenuBar(menu);

		// on récupère le container principal de la fenêtre
		container = this.getContentPane();
		// on modifie la couleur de fond du container
		container.setBackground(Color.WHITE);

		// on désactive le layout manager (gestionnaire de placement) du
		// container
		// afin de pouvoir placer les composants au pixel près
		container.setLayout(null);
		// on définit le texte de la fenêtre
		// labelDescription = new JLabel(
		// "Simulateur de Robot sauveteur en milieu hostile");
		// on définit la position et la taille du texte de la fenêtre
		// labelDescription.setBounds(5, 5, 780, 20);
		// on ajoute le composant texte dans le container de la fenêtre
		// this.add(labelDescription);

		menuChangerCouleur.addActionListener(this);
		menuPanneauDeControle.addActionListener(this);
		// on précise que le programme doit fermer complètement si on clique sur
		// la croix de la fenêtre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Permet d'ajouter le terrain dans le container principal de la fenêtre
	 * 
	 * @param t le terrain à ajouter
	 */
	public void ajouterTerrain(Terrain t) {
		terrain = t;
		// on ajoute le composant graphique du terrain dans le container de la
		// fenetre
		container.add(terrain);
		// on redessine la fenêtre
		repaint();
	}

	/**
	 * Permet d'ajouter le panneau de contrôle dans la fenêtre
	 * 
	 * @param pc le panneau de contrôle à ajouter
	 */
	public void ajouterPanneauDeControle(PanneauDeControle pc) {
		controle = pc;
		// on ajoute le composant graphique du panneau de contrôle dans le
		// container de la fenetre
		container.add(controle);
		// on redessine la fenêtre
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuChangerCouleur) {
			Color c = JColorChooser.showDialog(this, "choisissez la couleur du terrain", CaseVide.couleurCaseVideParDefaut);
			if (c != null)
				CaseVide.couleurCaseVideParDefaut = c;
			terrain.updateIHM();

			// Création d'une instance de la classe Properties
			Properties properties = new Properties();

			// Chemin du fichier .properties
			String fichier = "config.properties";

			// Ajout de propriétés
			properties.setProperty("couleurCaseVide", "" + CaseVide.couleurCaseVideParDefaut.getRGB());

			FileOutputStream output = null;
			try {
				output = new FileOutputStream(fichier);
				// Sauvegarde des propriétés dans le fichier
				properties.store(output, "Configuration d'application");
				// System.out.println("Fichier .properties sauvegardé avec succès.");
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