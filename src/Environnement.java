import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JLabel;

/**
 * Classe qui permet de cr�er l'environnement du simulateur
 * 
 * @author D�partement TIC - ESIGELEC
 * @version 1.0
 */
public class Environnement {

	/**
	 * permet de cr�er l'environnement du simulateur (fen�tre + terrain)
	 * 
	 * @return le terrain situ� sur la fen�tre
	 */
	public static Terrain creerEnvironnement(int nbLignes, int nbColonnes) {

		// cr�ation de la fen�tre
		Fenetre f = new Fenetre(640, 700);
		f.setTitle("Robot ESIGELEC v. 2.0");
		// blocage du redimmensionnement de la fenetre
		f.setResizable(false);
		// cr�ation du terrain
		Terrain terrain = new Terrain(nbLignes, nbColonnes);
		terrain.setBounds(20, 37, 580, 580);
		f.ajouterTerrain(terrain);
		// cr�ation de la zone de controle
		PanneauDeControle pc = new PanneauDeControle(terrain);
		pc.setBounds(610, 40, 400, 400);
		f.ajouterPanneauDeControle(pc);
		pc.setVisible(false);
		//f.setSize(1050,700);
		
		// ajout des no de ligne 
		for(int i=0;i<10;i++) {
			JLabel l=new JLabel(""+i);
			l.setForeground(Color.GRAY);
			l.setBounds(8,54+ i*58,20, 20);
			f.getContentPane().add(l);
		}
		
		// ajout des no de colonne 
		for(int i=0;i<10;i++) {
			JLabel l=new JLabel(""+i);
			l.setForeground(Color.GRAY);
			l.setBounds(48+ i*58,13,20, 20);
					f.getContentPane().add(l);
				}
			
		//on charge les eventuelles properties qui seraient enregistrees dans un fichier de config
		Properties prop = new Properties();
        InputStream input = null;

        try {
            // Charger le fichier properties
            input = new FileInputStream("config.properties");

            // Lire les propri�t�s du fichier
            prop.load(input);

            // Acc�der aux propri�t�s par leur cl�
            String couleurCaseVide = prop.getProperty("couleurCaseVide");
            try {
            	CaseVide.couleurCaseVideParDefaut = new Color(Integer.parseInt(couleurCaseVide));
            } catch(Exception i) {
            	System.err.println("Configuration couleur incorrecte.\n" + i);
            }


        } catch (IOException ex) {
            
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		
		
		// on centre la fen�tre � l'ecran
		f.setLocationRelativeTo(f.getParent());
		// on rend la fen�tre visible (par d�faut elle est invisible)
		f.setVisible(true);

		return terrain;

	}

}