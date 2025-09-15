/**
 * Programme principal avec la méthode main
 * @author Département TIC - ESIGELEC
 * @version 2.2
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class SimulateurAppX {

	public static void main(String[] args) {
		
		String[][] sentences = {
			    {"Choisir la langue", "Combien de victimes dois-je trouver ?", "Courage !", "La moyenne de gravité est : "},
			    {"Choose language", "How many victims have I to save?", "Be strong!", "The average gravity is: "},
			    {"Elige idioma", "¿Cuántas víctimas tengo que salvar?", "¡Sé fuerte!", "El promedio de gravedad es: "},
			    {"Sprache wählen", "Wie viele Opfer muss ich retten?", "Sei stark!", "Der Durchschnitt der Schwere ist: "}
			};

		Scanner sc = new Scanner(System.in);
		System.out.println("═══════════Language selection═══════════");
		System.out.println("1- Francais\n2- English\n3- Spanish\n4- Allemand\n");
		int language = 2;
		do {
			System.out.printf("%s \n", sentences[language-1][0]);
			language = sc.nextInt();
			
		} while(language<=0 || language>4);
		
		int victimNumber = 0;
		do {
			System.out.printf("%s \n", sentences[language-1][1]);
			victimNumber = sc.nextInt();
			
		} while(victimNumber<=0 || victimNumber>6);
		
		// création de l'environnement et récupération du terrain
		Terrain terrain = Environnement.creerEnvironnement(10, 10);

		// création du robot
		Robot robot = new Robot(0, 0, "sud");

		// ajout du robot sur le terrain
		terrain.ajouterRobot(robot);
		terrain.ajouterNombreDeVictimes(victimNumber);
		
		// mise à jour des composants graphiques
		
		terrain.updateIHM();
		
		// ajouter ici le code de déplacement du robot
		

		List<Integer> victimsFoundGravity = new ArrayList<>();
		
		int victimFound = 0;
		while(true) {
			
			if(victimFound == victimNumber) {
				break;
			}
			
			victimFound += avancerEtVerifier(robot, victimsFoundGravity);
			if(robot.getLigne() == 9 || robot.getLigne() == 0) {
				if (robot.getColonne() == 9 && (robot.getLigne() == 0 || robot.getLigne() == 9)) {
					break;
				}
				
				if (robot.getLigne() == 9 || robot.getLigne() == 0) {
			        victimFound += changeLine(robot, victimsFoundGravity);
			    }
				
			}
			
		}
		
		int higherGravity = max(victimsFoundGravity);
		robot.tournerDroite();
		robot.tournerDroite();
		int occurency = countOccurency(victimsFoundGravity, higherGravity);
		int count=0;
		while(true) {
			if(robot.isSurVictime()) {
				if(robot.detecterGravite() == higherGravity) {
					count++;
					System.out.printf("%s !\n", sentences[language-1][2]);
					JOptionPane.showMessageDialog(null, sentences[language-1][2]);
				}
			}
			if(count == occurency)
				break;
			robot.avancer();
			if(robot.getLigne() == 0 && robot.getColonne() == 0) {
				break;
			}
			if (robot.getLigne() == 9 || robot.getLigne() == 0) {
				changeLineReturn(robot);
		    }
		}
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
			float gravityMoy = (float) gravityTotal/victimsFoundGravity.size();
			System.out.printf("%s %f",sentences[language-1][3], gravityMoy);

		}else {
			System.out.println("No victim found");
		}
		sc.close();
	}
	
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
	
	private static int max(List<Integer> victimsFoundGravity) {
		int max=0;
		for(int i=0; i<victimsFoundGravity.size(); i++) {
			if(victimsFoundGravity.get(i)>max) {
				max = victimsFoundGravity.get(i);
			}
		}
		return max;
	}
	
	private static int avancerEtVerifier(Robot robot, List<Integer> victimsFoundGravity) {
	    robot.avancer();
	    if (robot.isSurVictime()) {
	        victimsFoundGravity.add(robot.detecterGravite());
	        return 1;
	    }
	    return 0;
	}

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
