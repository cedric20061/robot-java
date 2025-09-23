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
		        "Pourcentage de terrain inexploré : ",
		        "La gravité de la victime trouvée est : "
		    },
		    { // Anglais
		        "Choose language",
		        "How many victims have I to save?",
		        "Be strong!",
		        "The average gravity is: ",
		        "Percentage of unexplored area: ",
		        "The gravity of the found victim is: "
		    },
		    { // Espagnol
		        "Elige idioma",
		        "¿Cuántas víctimas tengo que salvar?",
		        "¡Sé fuerte!",
		        "El promedio de gravedad es: ",
		        "Porcentaje de terreno inexplorado: ",
		        "La gravedad de la víctima encontrada es: "
		    },
		    { // Allemand
		        "Sprache wählen",
		        "Wie viele Opfer muss ich retten?",
		        "Sei stark!",
		        "Der Durchschnitt der Schwere ist: ",
		        "Prozentsatz des unerforschten Gebiets: ",
		        "Die Schwere des gefundenen Opfers ist: "
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
		
		// création de l'environnement et récupération du terrain 
		Terrain t = Environnement.creerEnvironnement(10,10); 
		   
		// creation du robot 
		Robot robot = new Robot(0, 0, "sud"); 
		   
		// ajout du robot sur le terrain 
		t.ajouterRobot(robot);  
		   
		//ajouter tous les murs avec une porte à une position aléatoire 
		t.ajouterTousLesMurs(); 
		
		//ajouter tous les murs avec deux portes à des positions aléatoires 
		//t.ajouterTousLesMursDeuxPortes(); 
		   
		//ajouter une victime à la colonne 9 aléatoirement 
		t.ajouterVictimePositionAleatoireColonne9(); 
		
		//met à jour les composants graphiques 
		t.updateIHM();
		
		char[][] map = new char[10][10];
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				map[i][j] = (j % 2 == 1) ? 'O' : ' ';
			}
		}
		int sens = 0;
		while(robot.getColonne() != 8) {
			if(robot.getDirection() == "nord" || robot.getDirection() == "sud") {
				orientRobotForVerticalMove(robot);
				
				if(!robot.isObstacleDevant())  {
					map[robot.getLigne()][robot.getColonne()+1] = 'P';
					robot.avancer();
					robot.avancer();
					int line = 0;
					if(robot.getLigne() >= 5) {
						robot.tournerDroite();
						line = 9;
						sens  = 1;
					}else {
						robot.tournerGauche();
						sens = 0;
					}
					while(robot.getLigne() != line) {
						robot.avancer();
					}
					robot.tournerDroite();
					robot.tournerDroite();
				}
			}else {
				if(sens == 0) {
					robot.tournerDroite();
				} else {
					robot.tournerGauche();
				}
				robot.avancer();
			}
			
		}
		
		int victimFound = 0;
		int victimLine = 0;
		int victimGravity = 0;
		while (victimFound != 1) {
			if(robot.isSurVictime()) {
				victimFound++;
				victimGravity = robot.detecterGravite();
				victimLine = robot.getLigne();
			}
			if(victimFound != 1) {
				robot.avancer();
			}
		}
		// Finir de construire la map à partir des portes
		int width = map[0].length;

	    for (int col = 0; col < width - 1; col += 2) {
	        int leftDoorLine  = 0;
	        int rightDoorLine = victimLine;

	        // Cherche la porte de gauche si elle existe
	        if (col - 1 >= 0) {
	            leftDoorLine = findFirstDoorLine(map, col - 1, 0);
	        }

	        // Cherche la porte de droite si elle existe
	        if (col + 1 <= width - 2) {
	            rightDoorLine = findFirstDoorLine(map, col + 1, victimLine);
	        }

	        // Détermine les bornes min/max
	        int minLine = leftDoorLine>rightDoorLine ? rightDoorLine : leftDoorLine; 
	        int maxLine = leftDoorLine<rightDoorLine ? rightDoorLine : leftDoorLine;

	        // Remplit la colonne courante avec des '+'
	        for (int row = minLine; row <= maxLine; row++) {
	            map[row][col] = '+';
	        }
	    }

	    // Place la victime
	    map[victimLine][width - 2] = 'V';
		// Affichage de la map
		display2DTablePrettier(map);
		
		
		int rotation = 0;

		while (!(robot.getLigne() == 0 && robot.getColonne() == 0)) {
		
		    int ligne = robot.getLigne();
		    int colonne = robot.getColonne();
		    String direction = robot.getDirection();
		
		    boolean nordPossible = direction.equals("nord")
		        && (ligne != 0)
		        && ((map[ligne - 1][colonne] == '+')
		         || (map[ligne - 1][colonne] == 'P'));
		
		    boolean estPossible = direction.equals("est")
		        && (colonne != 9)
		        && ((map[ligne][colonne + 1] == '+')
		         || (map[ligne][colonne + 1] == 'P'));
		
		    boolean ouestPossible = direction.equals("ouest")
		        && (colonne != 0)
		        && ((map[ligne][colonne - 1] == '+')
		         || (map[ligne][colonne - 1] == 'P'));
		
		    boolean sudPossible = direction.equals("sud")
		        && (ligne != 9)
		        && ((map[ligne + 1][colonne]== '+')
		         || (map[ligne + 1][colonne] == 'P'));
		
		    if (nordPossible || estPossible || ouestPossible || sudPossible) {
		    	map[robot.getLigne()][robot.getColonne()] = '-';
		        robot.avancer();
		    } else {
		        robot.tournerDroite();
		        
		    }
		}

		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				if(map[i][j] == '-') {
					map[i][j] = '+';
				}
			}
		}
		map[victimLine][width - 2] = 'V';
		System.out.println(sentences[language-1][5] + victimGravity);
		sc.close(); // fermeture du scanner
	}

	// Tourner le robot à gauche ou droite selon la direction actuelle
	private static void orientRobotForVerticalMove(Robot robot) {
	    String dir = robot.getDirection();
	    if (dir.equals("nord")) {
	        robot.tournerDroite();
	    } else if (dir.equals("sud")) {
	        robot.tournerGauche();
	    }
	}
	// Recherche la première ligne qui contient un 'P' dans une colonne donnée
	private static int findFirstDoorLine(char[][] map, int col, int defaultLine) {
	    for (int row = 0; row < map.length; row++) {
	        if (map[row][col] == 'P') {
	            return row;
	        }
	    }
	    return defaultLine;
	}
	public static void display2DTable(char[][] tab) {
	    int rows = tab.length;
	    int cols = tab[0].length;

	    // Construire une ligne horizontale (bord supérieur/inférieur)
	    String horizontal = "+";
	    for (int j = 0; j < cols; j++) {
	        horizontal += "---+";
	    }

	    // Affichage du tableau
	    for (int i = 0; i < rows; i++) {
	        System.out.println(horizontal); // Ligne du dessus
	        for (int j = 0; j < cols; j++) {
	            System.out.print("| " + tab[i][j] + " ");
	        }
	        System.out.println("|");       // Bord droit
	    }
	    System.out.println(horizontal);     // Dernière ligne
	}
	public static void display2DTablePrettier(char[][] tab) {
	    int rows = tab.length;
	    int cols = tab[0].length;

	    // Bordures
	    String top    = buildLine("╔", "╦", "╗", cols, rows);
	    String middle = buildLine("╠", "╬", "╣", cols, rows);
	    String bottom = buildLine("╚", "╩", "╝", cols, rows);

	    // Affichage du tableau
	    System.out.println(top); // Ligne du haut
	    for (int i = 0; i < rows; i++) {
	        System.out.print("║");
	        for (int j = 0; j < cols; j++) {
	            System.out.print(" " + tab[i][j] + " ║");
	        }
	        System.out.println();
	        if (i < rows - 1) System.out.println(middle); // Ligne intermédiaire
	    }
	    System.out.println(bottom); // Ligne du bas
	}
	// Fonction simple pour construire une ligne horizontale
    public static String buildLine(String left, String mid, String right, int cols, int row) {
        String line = left;
        for (int j = 0; j < cols; j++) {
            line += "═══";
            if (j < cols - 1) line += mid;
        }
        line += right;
        return line;
    }
}
