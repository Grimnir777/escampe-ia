package game;

import java.util.ArrayList;

public class Heuristique {
	public int eval(EscampeBoard board, String player){
		
		//TODO make a get fct of liseres in class EscampeBoard
		int [][] liseres = new int[][] {{1,2,2,3,1,2},
			{3,1,3,1,3,2},
			{2,3,1,2,1,3},
			{2,1,3,2,3,1},
			{1,3,1,3,1,2},
			{3,2,2,1,3,2}};
			
			
		//Get tous les coups possibles pour les 2 joueurs
		int valH = 0 ;
		String[] possiblesMovesBlanc = board.possiblesMoves("blanc"); 
		String[] possiblesMovesNoir = board.possiblesMoves("noir");
		Square[][] squares = board.getBoard();
		
		
		String LicorneB;
		String LicorneN;
		ArrayList<String> paladinsB = new ArrayList<>();
		ArrayList<String> paladinsN = new ArrayList<>();
		
		int[] liseresAllies = {0,0,0};
		int[] liseresEnnemis = {0,0,0};
		
		for (int ligne = 0; ligne < squares.length; ligne++) {
			for (int col = 0; col < squares[ligne].length; col++) {
				String type = squares[ligne][col].type();
				if(type.equals("B")) {
					int lis = squares[ligne][col].lisere();
					if(player.equals("blanc")) {
						liseresAllies[lis]++;
					}
					else {
						liseresEnnemis[lis]++;
					}
					LicorneB = board.colHashMap.get(col) + Integer.toString(ligne);
				}
				if(type.equals("N")) {
					int lis = squares[ligne][col].lisere();
					if(player.equals("noir")) {
						liseresAllies[lis]++;
					}
					else {
						liseresEnnemis[lis]++;
					}
					LicorneN = board.colHashMap.get(col) + Integer.toString(ligne);
				}
				if(type.equals("b")) {
					int lis = squares[ligne][col].lisere();
					if(player.equals("blanc")) {
						liseresAllies[lis]++;
					}
					else {
						liseresEnnemis[lis]++;
					}
					paladinsB.add(board.colHashMap.get(col) + Integer.toString(ligne));
				}
				if(type.equals("n")) {
					int lis = squares[ligne][col].lisere();
					if(player.equals("noir")) {
						liseresAllies[lis]++;
					}
					else {
						liseresEnnemis[lis]++;
					}
					paladinsN.add(board.colHashMap.get(col) + Integer.toString(ligne));
				}
			}
		}
		//1er crit�re
		//Calcul du nombre de lis�r�s adverse
		//Si un lis�r� en d�faut et si on peut jouer un coup qui termine sur ce lis�r� alors valid� sinon pas valid�
		
		
		//2�me crit�re
		//Trouver les coups qui permettent de tuer notre licorne
		//Voir les lis�r�s de d�part de ces coups => �viter de terminer sur un de ces lis�r�s
		
		//3�me crit�re
		//Trouver les coups qui permettent de tuer la licorne adverse
		//Si possible de le jouer
		
		
		//4�me crit�re
		//Trouver le nombre de lis�r�s que l�on couvre.
		//Si �quitable 2 - 2 - 2 valMax = 10
		//Si une valeur est en d�faut : 0-2-4 / 0-1-3 valMin : 0
		//Si correct : 1-2-3 valMoyenne : 5
		
		if((liseresAllies[0] == liseresAllies[1]) && (liseresAllies[0] == liseresAllies[2])) valH+=10;
		if((liseresAllies[0] == 1) || (liseresAllies[1] == 1) || (liseresAllies[2] == 1)) valH+=5;
		
		return valH;
	}
}
