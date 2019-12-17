package game;

import java.util.ArrayList;

public class OptimusHeuritisque implements Heuristique {
	SquareTools squareTool;
	String player;
	String[] possiblesMovesBlanc;
	String[] possiblesMovesNoir;
	Square[][] squares;
	String LicorneB;
	String LicorneN;
	ArrayList<String> paladinsB;
	ArrayList<String> paladinsN;
	int[] liseresAllies = {0,0,0};
	int[] liseresEnnemis = {0,0,0};
	
	public OptimusHeuritisque() {
		this.squareTool = new SquareTools();
		this.paladinsB = new ArrayList<>();
		this.paladinsN = new ArrayList<>();
	}
	
	public int eval(EscampeBoard board, String player){
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
		
		// Get tous les coups possibles pour les 2 joueurs peu importe le liséré 
		// Permet de calculer l'heuristique en fonction des possibilités du joueur adverse au prochain tour (dans le but de le bloquer)
		int valH = 0 ;
		if(player.equals("blanc")) {
			this.possiblesMovesBlanc = board.possiblesMoves("blanc");
			this.possiblesMovesNoir = board.possiblesMovesForHeuristique("noir");
			this.liseresAllies = board.getLiseresBlanc().clone();
			this.liseresEnnemis = board.getLiseresNoir().clone();
		}
		else {
			this.possiblesMovesNoir = board.possiblesMoves("noir");
			this.possiblesMovesBlanc = board.possiblesMovesForHeuristique("blanc");
			this.liseresEnnemis = board.getLiseresBlanc().clone();
			this.liseresAllies = board.getLiseresNoir().clone();
		}
		this.squares = board.getBoard();

		//1er critère
		//Calcul du nombre de lisérés adverse
		//Si un liséré en défaut et si on peut jouer un coup qui termine sur ce liséré alors validé sinon pas validé
		
		// Si aucune pièce ennemie est sur un liséré 1
		if(liseresEnnemis[0] == 0) {
			valH = this.defaultAdvLisere(0,valH);
		}
		// Si aucune pièce ennemie est sur un liséré 2
		if(liseresEnnemis[1] == 0) {
			valH = this.defaultAdvLisere(1,valH);
		}
		// Si aucune pièce ennemie est sur un liséré 3
		if(liseresEnnemis[2] == 0) {
			valH = this.defaultAdvLisere(2,valH);
		}

		//2ème critère
		//Trouver le nombre de lisérés que l’on couvre.
		//Si équitable 2 - 2 - 2 valMax = 10
		//Si une valeur est en défaut : 0-2-4 / 0-1-3 valMin : 0
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
