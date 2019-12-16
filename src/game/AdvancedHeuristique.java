package game;

import java.util.ArrayList;

public class AdvancedHeuristique implements Heuristique {
	SquareTools squareTool;
	String player;
	String[] possiblesMovesBlanc;
	String[] possiblesMovesNoir;
	Square[][] squares;
	String LicorneB;
	String LicorneN;
	ArrayList<String> paladinsB;
	ArrayList<String> paladinsN;
	
	public AdvancedHeuristique() {
		this.squareTool = new SquareTools();
		this.paladinsB = new ArrayList<>();
		this.paladinsN = new ArrayList<>();
	}
	
	public int eval(EscampeBoard board, String player){
		//System.out.println("case");
		//System.out.println(board.toString());
		this.player = player;

		if(this.player.equals("blanc")) {
			if(board.getLicorneBState() == false) {
				return Integer.MIN_VALUE;
			}
			if(board.getLicorneNState() == false) {
				return Integer.MAX_VALUE;
			}
		}
		else {
			if(board.getLicorneNState() == false) {
				return Integer.MIN_VALUE;
			}
			if(board.getLicorneBState() == false) {
				return Integer.MAX_VALUE;
			}
		}
		
		// Get tous les coups possibles pour les 2 joueurs peu importe le lis�r� 
		// Permet de calculer l'heuristique en fonction des possibilit�s du joueur adverse au prochain tour (dans le but de le bloquer)
		int valH = 0 ;
		if(player.equals("blanc")) {
			this.possiblesMovesBlanc = board.possiblesMoves("blanc");
			this.possiblesMovesNoir = board.possiblesMovesForHeuristique("noir");
		}
		else {
			this.possiblesMovesNoir = board.possiblesMoves("noir");
			this.possiblesMovesBlanc = board.possiblesMovesForHeuristique("blanc");
		}
		this.squares = board.getBoard();
		
		
		
		
		int[] liseresAllies = {0,0,0};
		int[] liseresEnnemis = {0,0,0};
		
		//r�cup�ration de tous les lis�r�s des pi�ces blanc et noir
		for (int ligne = 0; ligne < squares.length; ligne++) {
			for (int col = 0; col < squares[ligne].length; col++) {
				String type = squares[ligne][col].type();
				if(type.equals("B")) {
					int lis = squares[ligne][col].lisere();
					if(player.equals("blanc")) {
						liseresAllies[lis-1]++;
					}
					else {
						liseresEnnemis[lis-1]++;
					}
					this.LicorneB = squareTool.getStringValue(col, ligne);
				}
				else if(type.equals("N")) {
					int lis = squares[ligne][col].lisere();
					if(player.equals("noir")) {
						liseresAllies[lis-1]++;
					}
					else {
						liseresEnnemis[lis-1]++;
					}
					this.LicorneN = squareTool.getStringValue(col, ligne);
				}
				else if(type.equals("b")) {
					int lis = squares[ligne][col].lisere();
					if(player.equals("blanc")) {
						liseresAllies[lis-1]++;
					}
					else {
						liseresEnnemis[lis-1]++;
					}
					this.paladinsB.add(squareTool.getStringValue(col, ligne));
				}
				else if(type.equals("n")) {
					int lis = squares[ligne][col].lisere();
					if(player.equals("noir")) {
						liseresAllies[lis-1]++;
					}
					else {
						liseresEnnemis[lis-1]++;
					}
					this.paladinsN.add(squareTool.getStringValue(col, ligne));
				}
			}
		}
		/*
		System.out.println("lisere allie");
		System.out.println(liseresAllies[0] + " " + liseresAllies[1] + " " + liseresAllies[2]  );
		
		System.out.println("lisere ennemi");
		System.out.println(liseresEnnemis[0] + " " + liseresEnnemis[1] + " " + liseresEnnemis[2]  );
		*/

		//1er crit�re
		//Calcul du nombre de lis�r�s adverse
		//Si un lis�r� en d�faut et si on peut jouer un coup qui termine sur ce lis�r� alors valid� sinon pas valid�
		
		// Si aucune pi�ce ennemie est sur un lis�r� 1
		if(liseresEnnemis[0] == 0) {
			valH = this.defaultAdvLisere(0,valH);
		}
		// Si aucune pi�ce ennemie est sur un lis�r� 2
		if(liseresEnnemis[1] == 0) {
			valH = this.defaultAdvLisere(1,valH);
		}
		// Si aucune pi�ce ennemie est sur un lis�r� 3
		if(liseresEnnemis[2] == 0) {
			valH = this.defaultAdvLisere(2,valH);
		}
		
		//4�me crit�re
		//Trouver le nombre de lis�r�s que l�on couvre.
		//Si �quitable 2 - 2 - 2 valMax = 10
		//Si une valeur est en d�faut : 0-2-4 / 0-1-3 valMin : 0
		//Si correct : 1-2-3 valMoyenne : 5
		
		if((liseresAllies[0] == liseresAllies[1]) && (liseresAllies[0] == liseresAllies[2])) valH+=10;
		if((liseresAllies[0] == 1) || (liseresAllies[1] == 1) || (liseresAllies[2] == 1)) valH+=5;
		
		return valH;
	}
	
	private int defaultAdvLisere(int defaultLisere ,int valH) {
		if(this.player.equals("blanc")) {
			for (String coup : this.possiblesMovesBlanc) {
				String end = coup.substring(3, 5);
				int lisere2 = this.squares[this.squareTool.getLigne(end)][this.squareTool.getCol(end)].lisere();
				
				if(lisere2 == defaultLisere) {
					valH += 100;
					break;
				}
			}
		}
		else {
			for (String coup : this.possiblesMovesNoir) {
				String end = coup.substring(3, 5);
				int ligne2 = this.squareTool.getLigne(end);
				int col2 = this.squareTool.getCol(end);
				int lisere2 = this.squares[ligne2][col2].lisere();
				
				if(lisere2 == defaultLisere) {
					valH += 100;
					break;
				}
			}
		}
		return valH;
	}
}
