/**
 * Programme principal avec la méthode main
 * @author Département TIC - ESIGELEC + Cédric GUIDI
 * @version 2.2
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class SimulateurAppX {
	public static void main(String[] args) {
			
		// Tableau à deux dimensions contenant les phrases de l’interface
		// dans différentes langues (Français, Anglais, Espagnol, Allemand).
		String[][] sentences = {
		    { // Français
		        "Choisir la langue",
		        "Combien de victimes dois-je trouver ?",
		        "Courage !",
		        "La moyenne de gravité est : ",
		        "Pourcentage de terrain inexploré : "
		    },
		    { // Anglais
		        "Choose language",
		        "How many victims have I to save?",
		        "Be strong!",
		        "The average gravity is: ",
		        "Percentage of unexplored area: "
		    },
		    { // Espagnol
		        "Elige idioma",
		        "¿Cuántas víctimas tengo que salvar?",
		        "¡Sé fuerte!",
		        "El promedio de gravedad es: ",
		        "Porcentaje de terreno inexplorado: "
		    },
		    { // Allemand
		        "Sprache wählen",
		        "Wie viele Opfer muss ich retten?",
		        "Sei stark!",
		        "Der Durchschnitt der Schwere ist: ",
		        "Prozentsatz des unerforschten Gebiets: "
		    }
		};
	
		// Scanner pour lire les saisies clavier de l’utilisateur
		Scanner sc = new Scanner(System.in);
	
		// Affichage du menu de sélection de langue
		System.out.println("═══════════Language selection═══════════");
		System.out.println("1- Francais\n2- English\n3- Spanish\n4- Allemand\n");
	
		int language = 2; // valeur par défaut = anglais
		// Boucle pour forcer l’utilisateur à choisir une langue valide (1 à 4)
		do {
			System.out.printf("%s \n", sentences[language-1][0]); // phrase "Choisir la langue" (dans la langue courante)
			language = sc.nextInt(); // saisie de l’utilisateur
		} while(language<=0 || language>4);
	
		// Saisie du nombre de victimes à rechercher
		int victimNumber = 0;
		do {
			System.out.printf("%s \n", sentences[language-1][1]); // message dans la langue choisie
			victimNumber = sc.nextInt();
		} while(victimNumber<=0 || victimNumber>6); // bornes de sécurité (entre 1 et 6 victimes)
	
		// Création de l’environnement de 10x10 cases
		Terrain terrain = Environnement.creerEnvironnement(10, 10);
	
		// Création du robot, initialisé à la position (0,0) et orienté vers le sud
		Robot robot = new Robot(0, 0, "sud");
	
		// Ajout du robot et du nombre de victimes à chercher dans le terrain
		terrain.ajouterRobot(robot);
		terrain.ajouterNombreDeVictimes(victimNumber);
		
		// Mise à jour de l’interface graphique pour afficher le terrain et le robot
		terrain.updateIHM();
		
		// Liste pour stocker les gravités des victimes découvertes
		List<Integer> victimsFoundGravity = new ArrayList<>();
		
		int victimFound = 0;   // compteur de victimes découvertes
		int fieldExplore = 0;  // compteur de cases explorées
		
		// Boucle principale : exploration du terrain
		while(true) {
			fieldExplore++; // on incrémente le nombre de cases explorées
			
			// Si toutes les victimes ont été trouvées → fin de la boucle
			if(victimFound == victimNumber) {
				break;
			}
			
			// Le robot avance et vérifie s’il trouve une victime
			victimFound += avancerEtVerifier(robot, victimsFoundGravity);
			
			// Gestion du déplacement en mode "serpentin" (aller-retour sur les lignes)
			if(robot.getLigne() == 9 || robot.getLigne() == 0) {
				// si le robot est dans un coin (bas droite ou haut droite) → fin
				if (robot.getColonne() == 9 && (robot.getLigne() == 0 || robot.getLigne() == 9)) {
					break;
				}
				
				// sinon, changement de ligne pour continuer l’exploration
				if (robot.getLigne() == 9 || robot.getLigne() == 0) {
			        victimFound += changeLine(robot, victimsFoundGravity);
			        fieldExplore++;
			    }
			}
		}
		
		// Une fois toutes les victimes trouvées, on identifie la gravité la plus élevée
		int higherGravity = max(victimsFoundGravity);
	
		// Le robot fait demi-tour pour revenir au point de départ
		robot.tournerDroite();
		robot.tournerDroite();
	
		// On compte combien de victimes ont cette gravité maximale
		int occurency = countOccurency(victimsFoundGravity, higherGravity);
		int count = 0; // compteur de victimes retrouvées avec cette gravité
	
		// Parcours de retour du robot jusqu’à avoir revu toutes les victimes les plus graves
		while(true) {
			if(robot.isSurVictime()) {
				if(robot.detecterGravite() == higherGravity) {
					count++;
					// Message d’encouragement dans la langue choisie
					System.out.printf("%s !\n", sentences[language-1][2]);
					JOptionPane.showMessageDialog(null, sentences[language-1][2]);
				}
			}
			if(count == occurency) // si toutes les victimes graves sont retrouvées
				break;
			
			robot.avancer();
			
			// Arrêt si le robot est revenu au point de départ
			if(robot.getLigne() == 0 && robot.getColonne() == 0) {
				break;
			}
			// Sinon, gestion du retour en serpentin
			if (robot.getLigne() == 9 || robot.getLigne() == 0) {
				changeLineReturn(robot);
		    }
		}
	
		// Affichage du tableau récapitulatif des victimes et de leurs gravités
		if(victimsFoundGravity.size() >0) {
			System.out.println("═════════════════ Victims List ═════════════════");
			System.out.println("╔════════════╦═════════════════╗");
			System.out.printf ("║ %-10s ║ %-14s  ║%n", "Victims", "Gravity");
			System.out.println("╠════════════╬═════════════════╣");
			
			int gravityTotal = 0;
			for (int i = 0; i < victimsFoundGravity.size(); i++) {
			    System.out.printf("║ %-10s ║  %14d ║%n", "Victim " + (i + 1), victimsFoundGravity.get(i));
			    gravityTotal += victimsFoundGravity.get(i);
			}
	
			System.out.println("╚════════════╩═════════════════╝");
	
			// Calcul de la gravité moyenne
			float gravityMoy = (float) gravityTotal/victimsFoundGravity.size();
			System.out.printf("%s %f\n",sentences[language-1][3], gravityMoy);
	
			// Affichage du pourcentage de terrain non exploré
			System.out.printf("%s %d%%", sentences[language-1][4], 100-fieldExplore);
		}else {
			// Aucun victime trouvée
			System.out.println("No victim found");
		}
		sc.close(); // fermeture du scanner
	}

	/**
	 * Permet de changer de ligne lorsque le robot atteint le haut ou le bas du terrain.
	 * Le robot avance d'une case sur la colonne voisine et se réoriente
	 * pour continuer son exploration en serpentin.
	 *
	 * @param robot le robot qui se déplace
	 * @param victimsFoundGravity la liste des gravités des victimes trouvées
	 * @return le nombre de victimes trouvées pendant ce changement de ligne
	 */
	private static int changeLine(Robot robot, List<Integer> victimsFoundGravity) {
	    int ans = 0;
	    if (robot.getLigne() == 9) {
	        robot.tournerGauche();
	        ans += avancerEtVerifier(robot, victimsFoundGravity);
	        robot.tournerGauche();
	    } else {
	        robot.tournerDroite();
	        ans += avancerEtVerifier(robot, victimsFoundGravity);
	        robot.tournerDroite();
	    }
	    return ans;
	}

	/**
	 * Fait changer le robot de ligne lors de son parcours de retour.
	 * Contrairement à {@code changeLine}, cette méthode ne vérifie pas la présence
	 * de victimes mais ajuste seulement la position et l'orientation du robot
	 * pour continuer le trajet inverse en serpentin.
	 *
	 * @param robot le robot qui se déplace
	 */
	private static void changeLineReturn(Robot robot) {
	    if (robot.getLigne() == 9) {
	    	robot.tournerDroite();
	        robot.avancer();
	        robot.tournerDroite();
	        
	    } else {
	    	robot.tournerGauche();
	        robot.avancer();
	        robot.tournerGauche();
	    }
	}

	/**
	 * Recherche la valeur maximale dans une liste d'entiers.
	 * Utilisée ici pour déterminer la gravité la plus forte
	 * parmi les victimes découvertes par le robot.
	 *
	 * @param victimsFoundGravity la liste des gravités enregistrées
	 * @return la valeur maximale trouvée dans la liste
	 */
	private static int max(List<Integer> victimsFoundGravity) {
		int max=0;
		for(int i=0; i<victimsFoundGravity.size(); i++) {
			if(victimsFoundGravity.get(i)>max) {
				max = victimsFoundGravity.get(i);
			}
		}
		return max;
	}

	/**
	 * Fait avancer le robot d'une case puis vérifie s'il se trouve
	 * sur une victime. Si c'est le cas, la gravité de la victime est ajoutée
	 * à la liste des gravités.
	 *
	 * @param robot le robot qui avance
	 * @param victimsFoundGravity la liste des gravités des victimes trouvées
	 * @return 1 si une victime est trouvée, 0 sinon
	 */
	private static int avancerEtVerifier(Robot robot, List<Integer> victimsFoundGravity) {
	    robot.avancer();
	    if (robot.isSurVictime()) {
	        victimsFoundGravity.add(robot.detecterGravite());
	        return 1;
	    }
	    return 0;
	}

	/**
	 * Compte le nombre de fois qu'une valeur donnée apparaît dans une liste.
	 * Utilisé ici pour savoir combien de victimes possèdent une gravité donnée.
	 *
	 * @param list la liste d'entiers à analyser
	 * @param num la valeur à rechercher
	 * @return le nombre d'occurrences de la valeur {@code num} dans la liste
	 */
	private static int countOccurency(List<Integer> list, int num) {
		int occurency=0;
		for(int i=0; i<list.size(); i++) {
			if(list.get(i) == num) {
				occurency++;
			}
		}
		return occurency;
	}

}
