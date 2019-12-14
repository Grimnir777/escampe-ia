package game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EscampeBoard implements Partie1{
	
	private int [][] liseres = new int[][] {{1,2,2,3,1,2},
											{3,1,3,1,3,2},
											{2,3,1,2,1,3},
											{2,1,3,2,3,1},
											{1,3,1,3,1,2},
											{3,2,2,1,3,2}};
											
	private Square[][] board;
	
	private boolean licorneB;
	private boolean licorneN;
	
	private boolean firstB;
	private boolean firstN;
	private boolean firstChooseUp;
	private int lastLisere;
	int nb_coupinit_noir = 0;
	int nb_coupinit_blanc =0;
	
	Map<Integer, Character> colHashMap  = new HashMap<Integer, Character>() {
		private static final long serialVersionUID = -3708238127665825076L;
	{
	    put(0,'A');
	    put(1,'B');
	    put(2,'C');
	    put(3,'D');
	    put(4,'E');
	    put(5,'F');
	}};
	
	public EscampeBoard() {
		this.board = new Square[6][6];
		for (int i = 0; i < board.length; i++) {
			Square[] squares = board[i];
			for (int j = 0; j < squares.length; j++) {
				squares[j] = new Square(this.liseres[i][j]);
			}
		}
		this.licorneB = true;
		this.licorneN = true;
		this.firstN = true;
		this.firstB = true;
		this.lastLisere = -1;
	}
	public EscampeBoard(EscampeBoard toCopy) {
		this.board = toCopy.board.clone();
		
		this.licorneB = toCopy.licorneB;
		this.licorneN = toCopy.licorneN;
		
		this.firstB = toCopy.firstB;
		this.firstN = toCopy.firstN;
		this.firstChooseUp = toCopy.firstChooseUp;
		this.lastLisere = toCopy.lastLisere;
		this.squareTool = new SquareTools();
	}
	
	public Square[][] getBoard() {
		return this.board.clone();
	}
	
	public EscampeBoard copy() {
		return new EscampeBoard(this);
	}
	
	@Override
<<<<<<< HEAD
=======
	public EscampeBoard clone(){
		EscampeBoard newBoard = null;
		 try {
			 newBoard = (EscampeBoard) super.clone();
		 }
		 catch (CloneNotSupportedException e){
		 throw new InternalError();
		 }
		 newBoard.board = new Square[6][6];
		 for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				try {
					newBoard.board[i][j] = board[i][j].clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 }
		 newBoard.squareTool = new SquareTools();
		 return newBoard;
	}

	
	public boolean getLicorneBState() {
		return this.licorneB;
	}
	public boolean getLicorneNState() {
		return this.licorneN;
	}
	
	public int getPreviousLisere() {
		return this.lastLisere;
	}
	
	@Override
>>>>>>> parent of ef70786... First win of IA :pray:
	public String toString() {
		String r = "%  ABCDEF\n";
		for (int i = 0; i < board.length; i++) {
			Square[] squares = board[i];
			r+= "0" + Integer.toString(i+1) + " ";
			for (int j = 0; j < squares.length; j++) {
				r+=squares[j].type();
			}
			r+= " 0" + Integer.toString(i+1) + "\n";
		}
		r += "%  ABCDEF\n";
		return r;
	}
	
	
	
	/** initialise un plateau à partir d'un fichier texte
	* @param fileName le nom du fichier à lire
	*/
	public void setFromFile(String fileName) {
		
		List<String> charBoard = null;
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			charBoard = stream
					.filter(line -> !line.startsWith("%"))
					.map(line -> line.split(" ")[1])
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}


		for (int line = 0; line < charBoard.size(); line++) {
			for (int col = 0; col < charBoard.get(line).length(); col++) {
				switch (charBoard.get(line).charAt(col)) {
					case '-':
						this.board[line][col].resetSquare();
						break;
					case 'N':
						this.board[line][col].setSquare("noir", SquareType.licorne);				
						break;
					case 'n':
						this.board[line][col].setSquare("noir",SquareType.paladin);		
						break;
					case 'B':
						this.board[line][col].setSquare("blanc",SquareType.licorne);		
						break;
					case 'b':
						this.board[line][col].setSquare("blanc",SquareType.paladin);		
						break;
					default:
						break;
				}
			}
		}
	}
	
	
	/** sauve la configuration de l'état courant (plateau et pièces restantes) dans un fichier
	* @param fileName le nom du fichier à sauvegarder
	* Le format doit être compatible avec celui utilisé pour la lecture.
	*/
	public void saveToFile(String fileName) {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.print(this);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
	/** indique si le coup <move> est valide pour le joueur <player> sur le plateau courant
	* @param move le coup à jouer,
	* sous la forme "B1-D1" en général,
	* sous la forme "C6/A6/B5/D5/E6/F5" pour le coup qui place les pièces
	* @param player le joueur qui joue, représenté par "noir" ou "blanc"
	*/
	public boolean isValidMove(String move, String player) {
		if(move.contains("/") && move.length() == 17) {
			//check si premier coup
			//si joueur noir n'a pas joué et que le joueur blanc veut jouer ==> false
			// ou si joueur blanc a déjà joué son coup
			if( (this.firstN && player.equals("blanc")) || !this.firstB) { 
				return false;
			}
			
			List<String> moveSquares =  new ArrayList<String>(Arrays.asList(move.split("/")));
			
			//check doublons
			Set<String> set = new HashSet<String>(moveSquares);
			if(set.size() < moveSquares.size()){
			    return false;
			}
			
			//Check si toutes les lignes fournies sont du meme cote
			boolean pattern1=false;
			boolean pattern2=false;
			for (String mv : moveSquares) {
				if(!mv.matches("^[ABCDEF][12]$")) {pattern1 = true;}
				if(!mv.matches("^[ABCDEF][56]$")) {pattern2 = true;}
			}
			
			if((pattern1 && pattern2) || (!pattern1 && !pattern2)) return false;
			
			//check ligne
			if(player.equals("blanc")) { // si on arrive ici le premier coup a été joué
				for (String mv : moveSquares) {
					int line = Character.getNumericValue(mv.charAt(1));
					if(this.firstChooseUp && (line==1 || line==2)) return false;
					if(!this.firstChooseUp && (line==5 || line==6)) return false;
				}
			}
			return true;
		}
		else if(move.contains("-") && move.length() == 5) { // pattern XX-XX
			String[] moveSquares = move.split("-");

			//Check pattern de chaque case
			for (String mv : moveSquares) {
				if(!mv.matches("^[ABCDEF][123456]$")) return false;
			}
			
			int ligneCase1 = Character.getNumericValue(moveSquares[0].charAt(1)) - 1;
			int colCase1 = this.getIndexOfCol(moveSquares[0].charAt(0));
			
			if(this.lastLisere == -1) {
				if( player.equals("noir")) return false;
			}
			else { //lisere autre que -1 (en jeu)
				//si la case de départ est bien sur un lisere identique au dernier coup
				//et que le joueur ne s'est pas fait sauter son tour lastLisere = 0
				if((this.board[ligneCase1][colCase1].lisere() != this.lastLisere) && this.lastLisere != 0) return false;
			}
			
			//Test si pièce alliée sur la case de départ
			String pieceOnSquare = this.board[ligneCase1][colCase1].type();
			if(player.equals("blanc")) {
				if(!pieceOnSquare.equals("B") && !pieceOnSquare.equals("b") )
				{
					return false;
				}
			}
			else if (player.equals("noir")) {
				if(!pieceOnSquare.equals("N") && !pieceOnSquare.equals("n") )
				{
					return false;
				}
			}

			int ligneCase2 =  Character.getNumericValue(moveSquares[1].charAt(1)) - 1;
			int colCase2 = this.getIndexOfCol(moveSquares[1].charAt(0));

			
			//Test si pas de pièce ennemie sur la case d'arrivée sauf si licorne 
			// si licorne essaie de prendre licorne alors faux
			String pieceOnFinalSquare = this.board[ligneCase2][colCase2].type();
			if(player.equals("blanc")) {
				if(pieceOnFinalSquare.equals("n") ) return false;
				if(pieceOnFinalSquare.equals("N") && pieceOnSquare.equals("B")) return false;
			}
			else if (player.equals("noir")) {
				if(pieceOnFinalSquare.equals("b") ) return false;
				if(pieceOnFinalSquare.equals("B") && pieceOnSquare.equals("N")) return false;
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	private ArrayList<String> movesForSquare(int ligne, int col, int level, String initSquare, String player, ArrayList<String> previous) {
		ArrayList<String> results = new ArrayList<String>();
		
		String nameOfSquare = this.colHashMap.get(col) + Integer.toString(ligne+1);
		//Si déjà visité alors retourner array vide
		for (String pr : previous) {
			if(pr.equals(nameOfSquare)) return results;
		}
		
		// Si en dehors du plateau retourner array vide
		if(ligne<0 | ligne >5 | col<0 | col>5) {
			return results;
		}
		String typeOfSquare = this.board[ligne][col].type();
		
		
		//Si on tombe sur la licorne adverse au niveau 0, alors le coup est valide
		if(level == 0) {
			if((player.equals("blanc") && typeOfSquare.equals("N"))  || ( player.equals("noir") && typeOfSquare.equals("B"))) {
					String mvmt = initSquare + "-" + this.colHashMap.get(col) + Integer.toString(ligne+1);
					if(this.isValidMove(mvmt, player)) {
						results.add(mvmt);
						return results;
					}
			}
		}
		
		if(!typeOfSquare.equals("-")) {
			return results;
		}
		if(level>0) {
			previous.add(nameOfSquare);
			results.addAll(movesForSquare(ligne-1, col, level-1, initSquare,player,previous));
			results.addAll(movesForSquare(ligne+1, col, level-1, initSquare,player,previous));
			results.addAll(movesForSquare(ligne, col-1, level-1, initSquare,player,previous));
			results.addAll(movesForSquare(ligne, col+1, level-1, initSquare,player,previous));
		}
		else {
			results.add(initSquare + "-" + this.colHashMap.get(col) + Integer.toString(ligne+1));
		}
		return results;
	}
	
	
	/** calcule les coups possibles pour le joueur <player> sur le plateau courant
	* @param player le joueur qui joue, représenté par "noir" ou "blanc"
	*/
	public String[] possiblesMoves(String player) {		

		
		ArrayList<String> moves = new ArrayList<String>();
	
		if (player =="noir" && firstN==true) {
			for(int f=0;f<7; f++) {
				for(int h=0;h<12;h++) {
				
					nb_coupinit_noir++;
				}
			}
			
		}else if(player=="blanc" && firstB==true) {
			for(int k=0;k<7; k++) {
				for(int v=0;v<12;v++) {
					
					nb_coupinit_blanc++;
				}
			}
		}
		
		for (int i = 0; i < board.length; i++) {
			Square[] squares = board[i];
			for (int j = 0; j < squares.length; j++) {
				if(( (squares[j].type().equals("B") | squares[j].type().equals("b")) && player.equals("blanc") ) || ( (squares[j].type().equals("N") | squares[j].type().equals("n")) && player.equals("noir") ) ) { //pion qui peut etre déplacé
					int level = squares[j].lisere();
					if(level == this.lastLisere || this.lastLisere == -1 || this.lastLisere == 0) {
						String start = this.colHashMap.get(j) + Integer.toString(i+1);
						ArrayList<String> previous = new ArrayList<String>();
						previous.add(start);
						moves.addAll(movesForSquare(i-1,j,level-1,start,player,previous));
						moves.addAll(movesForSquare(i+1,j,level-1,start,player,previous));
						moves.addAll(movesForSquare(i,j-1,level-1,start,player,previous));
						moves.addAll(movesForSquare(i,j+1,level-1,start,player,previous));							
					}
				}
			}
		}
		String[] arr = new String[moves.size()]; 
		arr = moves.toArray(arr);
		return arr;
	}
	
	
	/** modifie le plateau en jouant le coup move avec la piece choose
	* @param move le coup à jouer, sous la forme "C1-D1" ou "C6/A6/B5/D5/E6/F5"
	* @param player le joueur qui joue, représenté par "noir" ou "blanc"
	*/
	public void play(String move, String player) {
		if(move.contains("/") && move.length() == 17) {
			List<String> moveSquares =  new ArrayList<String>(Arrays.asList(move.split("/")));
			int ligne = Character.getNumericValue(moveSquares.get(0).charAt(1)) - 1;
			int col = -1;
			col = this.getIndexOfCol(moveSquares.get(0).charAt(0));
			this.board[ligne][col].setSquare(player, SquareType.licorne);
			
			for (int i = 1; i < moveSquares.size(); i++) {
				ligne = Character.getNumericValue(moveSquares.get(i).charAt(1)) - 1;
				col = this.getIndexOfCol(moveSquares.get(i).charAt(0));
				this.board[ligne][col].setSquare(player, SquareType.paladin);
			}
			if(player.equals("blanc")) {this.firstB = false;}
			else if(player.equals("noir")) {this.firstN = false;}
		}
		else if(move.contains("-") && move.length() == 5) {
			List<String> moveSquares =  new ArrayList<String>(Arrays.asList(move.split("-")));
			int ligne1 = Character.getNumericValue(moveSquares.get(0).charAt(1)) - 1;
			int col1 = this.getIndexOfCol(moveSquares.get(0).charAt(0));
			int ligne2 = Character.getNumericValue(moveSquares.get(1).charAt(1)) - 1;
			int col2 = this.getIndexOfCol(moveSquares.get(1).charAt(0));
			
			if(player.equals("blanc")) {
				String typeOfPiece = this.board[ligne1][col1].type();
				if(this.board[ligne2][col2].type().equals("N")) {
					this.licorneN = false;
				}
				if(typeOfPiece.equals("b")) {
					this.board[ligne2][col2].setSquare(player, SquareType.paladin);
				}
				if(typeOfPiece.equals("B")) {
					this.board[ligne2][col2].setSquare(player, SquareType.licorne);
				}
			}
			else if (player.equals("noir"))	{
				String typeOfPiece = this.board[ligne1][col1].type();
				if(this.board[ligne2][col2].type().equals("B")) {
					this.licorneB = false;
				}
				if(typeOfPiece.equals("n")) {
					this.board[ligne2][col2].setSquare(player, SquareType.paladin);
				}
				if(typeOfPiece.equals("N")) {
					this.board[ligne2][col2].setSquare(player, SquareType.licorne);
				}
			}
			this.lastLisere = this.board[ligne2][col2].lisere();
			this.board[ligne1][col1].resetSquare();
		}
	}
	
	
	/** vrai lorsque le plateau corespond à une fin de partie
	*/
	public boolean gameOver() {
		return !this.licorneB | !this.licorneN;
	}
	
	private int getIndexOfCol(char charToFind) {
		int r = -1;
		for (Entry<Integer, Character> entry : this.colHashMap.entrySet()) {
	        if (Objects.equals(charToFind, entry.getValue())) {
	        	r = entry.getKey();
	        }
	    }
		return r;
	}
	
	
	public static void main(String[] args) {
		// Quelques tests basiques, pour voir plus de test voir le package test (junit tests)
		EscampeBoard e = new EscampeBoard();
		
		System.out.println("\n-------- Quelques tests de la fonction isValidMove --------");
		System.out.println("Coup correct sur 2 lignes (C6/A6/B5/D5/E6/F5) : " + e.isValidMove("C6/A6/B5/D5/E6/F5","noir"));
		System.out.println("Coup incorrect avec un duplicat (C6/C6/B5/D5/E6/F5) : " + e.isValidMove("C6/C6/B5/D5/E6/F5","noir"));
		System.out.println("Coup incorrect car sur les mauvaises lignes (C3/A1/A1/D2/E2/F2) : " + e.isValidMove("C3/A1/A1/D2/E2/F2","blanc"));
		System.out.println("Coup incorrect car joueur blanc (C1/A1/A1/D2/E2/F2) : " + e.isValidMove("C1/A1/A1/D2/E2/F2","blanc"));

		
		System.out.println("\n-------- 2 premier coups --------");
		System.out.println("Coup noir (C6/A6/B5/D5/E6/F5)");
		System.out.println("Coup blanc (C1/A1/B1/D2/E2/F2)");
		
		e.play("C6/A6/B5/D5/E6/F5", "noir");
	    e.play("C1/A1/B1/D2/E2/F2", "blanc");
		System.out.println(e);
		
		System.out.println("\n-------- Mouvements pour joueur blanc --------");
		String[] movesB = e.possiblesMoves("blanc");
		for (String string : movesB) {
			System.out.println("--> " + string);
		}
		
		System.out.println("\n-------- Mouvements pour joueur noir --------");
		String[] movesN = e.possiblesMoves("noir");
		for (String string : movesN) {
			System.out.println("--> " + string);
		}
		
		//Test de sauvegarde et lecture de plateau
		System.out.println("\n-------- Test de lecture du plateau actuel, sauvegarde et lecture --------");
		e.saveToFile("testB.txt");
		e = new EscampeBoard();
		e.setFromFile("testB.txt");
		System.out.println(e);
		
		System.out.println("\n-------- Quelques tests de la fonction isValidMove --------");
		System.out.println("Coup incorrect (A1-A4) : " + e.isValidMove("A1-A4","blanc"));
		System.out.println("Coup incorrect (A1-A2) : " + e.isValidMove("A1-A2","blanc"));
		
		e.play("A1-A2", "blanc");
		System.out.println("\n-------- Coup joué A1-A2 par le joueur blanc --------");
		System.out.println(e);
<<<<<<< HEAD
=======
		*/
		
		
		
		EscampeBoard e = new EscampeBoard();
		e.play("B2/A1/C1/D2/E1/F2", "noir");
		e.play("C6/A6/B5/D5/E6/F5", "blanc");
		
		String[] movesB = e.possiblesMoves("blanc");
		String[] movesN = e.possiblesMoves("noir");
		System.out.println("blanc");
		for (String string : movesB) {
			System.out.println("--> " + string);
		}
		System.out.println("noir");
		for (String string : movesN) {
			System.out.println("==> " + string);
		}
		
		//System.out.println(e.possiblesMoves("blanc"));
		AlphaBeta ab = new AlphaBeta(new Heuristique() , "blanc", "noir",3);
		System.out.println("meilleur coup : " + ab.meilleurCoup(e));
		
		

		
>>>>>>> parent of ef70786... First win of IA :pray:
	}
}
