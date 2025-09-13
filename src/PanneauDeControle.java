import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe représentant le panneau de contrôle du simulateur
 * 
 * @author Département TIC - ESIGELEC
 * @version 1.0
 */
public class PanneauDeControle extends JPanel implements ActionListener {
	/**
	 * numéro de version de la classe
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Terrain associé au panneau de contrôle
	 */
	Terrain terrain;
	/**
	 * bouton qui permet de faire avancer le robot
	 */
	JButton boutonAvancer;
	/**
	 * bouton qui permet de faire reculer le robot
	 */
	JButton boutonReculer;
	/**
	 * bouton qui permet de faire tourner à droite le robot
	 */
	JButton boutonTournerDroite;
	/**
	 * bouton qui permet de faire tourner à gauche le robot
	 */
	JButton boutonTournerGauche;
	/**
	 * bouton qui permet de détruire le robot
	 */
	JButton boutonDetruireRobot;
	/**
	 * bouton qui permet de tester la présence d'un obstacle devant le robot
	 */
	JButton boutonIsObstacleDevant;
	/**
	 * bouton qui permet de tester la présence d'un obstacle derrière le robot
	 */
	JButton boutonIsObstacleDerriere;
	/**
	 * bouton qui permet de tester la présence d'une victime sur la case du
	 * robot
	 */
	JButton boutonIsSurVictime;
	/**
	 * JLabel contenant le titre du panneau de contrôle
	 */
	JLabel titre;

	/**
	 * Constructeur de la classe PanneauDeContrôle
	 * 
	 * @param t
	 *            le terrain associé au panneau de contrôle
	 */
	public PanneauDeControle(Terrain terrain) {
		// on initialise le terrain associé au panneau de contrôle
		this.terrain = terrain;
		// on fixe la couleur de fond du panneau de contrôle
		this.setBackground(new Color(118, 200, 34));
		// on désactive le gestionnaire de placement pour positionner les
		// composants au pixel près
		this.setLayout(null);
		// on instancie tous les composants du panneau de contrôle
		boutonAvancer = new JButton("avancer()");
		boutonReculer = new JButton("reculer()");
		boutonTournerDroite = new JButton("tournerDroite()");
		boutonTournerGauche = new JButton("tournerGauche()");
		boutonDetruireRobot = new JButton("detruireRobot()");
		boutonIsObstacleDevant = new JButton("isObstacleDevant()");
		boutonIsObstacleDerriere = new JButton("isObstacleDerriere()");
		boutonIsSurVictime = new JButton("isSurVictime()");
		titre = new JLabel("Panneau de contrôle du robot :", JLabel.CENTER);

		// on fixe les positions et tailles des composants
		boutonAvancer.setBounds(150, 100, 100, 50);
		boutonReculer.setBounds(150, 200, 100, 50);
		boutonTournerGauche.setBounds(10, 150, 140, 50);
		boutonTournerDroite.setBounds(250, 150, 140, 50);
		boutonDetruireRobot.setBounds(250, 350, 140, 30);
		boutonIsSurVictime.setBounds(10, 350, 140, 30);
		boutonIsObstacleDevant.setBounds(125, 60, 150, 30);
		boutonIsObstacleDerriere.setBounds(125, 260, 150, 30);
		titre.setBounds(10, 10, 370, 30);

		// on modifie les couleurs des composants
		boutonAvancer.setBackground(Color.ORANGE);
		boutonReculer.setBackground(Color.ORANGE);
		boutonTournerGauche.setBackground(Color.ORANGE);
		boutonTournerDroite.setBackground(Color.ORANGE);
		boutonIsObstacleDevant.setBackground(new Color(255, 255, 0));
		boutonIsObstacleDerriere.setBackground(new Color(255, 255, 0));
		boutonDetruireRobot.setBackground(Color.RED);
		boutonIsSurVictime.setBackground(Color.RED);
		boutonIsSurVictime.setForeground(Color.WHITE);
		boutonDetruireRobot.setForeground(Color.WHITE);
		titre.setBackground(Color.RED);
		titre.setForeground(Color.WHITE);

		// on ajoute les écouteurs (listeners) sur les boutons
		boutonAvancer.addActionListener(this);
		boutonReculer.addActionListener(this);
		boutonTournerDroite.addActionListener(this);
		boutonTournerGauche.addActionListener(this);
		boutonDetruireRobot.addActionListener(this);
		boutonIsObstacleDevant.addActionListener(this);
		boutonIsObstacleDerriere.addActionListener(this);
		boutonIsSurVictime.addActionListener(this);

		// on ajoute les composants sur le panneau de contrôle
		this.add(boutonAvancer);
		this.add(boutonReculer);
		this.add(boutonTournerDroite);
		this.add(boutonTournerGauche);
		this.add(boutonDetruireRobot);
		this.add(boutonIsObstacleDevant);
		this.add(boutonIsObstacleDerriere);
		this.add(boutonIsSurVictime);
		this.add(titre);

	}

	@Override
	/**
	 * Méthode appelée lorsque l'utilisateur clique sur un bouton (possédant un listener)
	 */
	public void actionPerformed(ActionEvent ae) {

		// test des boutons cliqués et appels des méthodes concernées
		// getSource() permet de connaitre le composant à l'origine de
		// l'événement (du clic)

		if (ae.getSource() == boutonAvancer)
			terrain.getRobot().avancer();
		else if (ae.getSource() == boutonReculer)
			terrain.getRobot().reculer();
		else if (ae.getSource() == boutonTournerDroite)
			terrain.getRobot().tournerDroite();
		else if (ae.getSource() == boutonTournerGauche)
			terrain.getRobot().tournerGauche();
		else if (ae.getSource() == boutonDetruireRobot)
			terrain.getRobot().detruireRobot();
		else if (ae.getSource() == boutonIsObstacleDevant)
			System.out.println("isObstacleDevant : "
					+ terrain.getRobot().isObstacleDevant());
		else if (ae.getSource() == boutonIsObstacleDerriere)
			System.out.println("isObstacleDerriere : "
					+ terrain.getRobot().isObstacleDerriere());
		else if (ae.getSource() == boutonIsSurVictime)
			System.out.println("isSurVictime : "
					+ terrain.getRobot().isSurVictime());

	}

}