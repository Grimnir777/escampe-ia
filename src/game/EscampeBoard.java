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
	}
	
	public Square[][] getBoard() {
		return this.board.clone();
	}
	
	@Override
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
			List<String> moveSquares =  new ArrayList<String>(Arrays.asList(move.split("/")));
			
			//Check pattern
			for (String mv : moveSquares) {
				if(!mv.matches("^[ABCDEF][123456]$")) return false;
			}
			
			//check doublons
			Set<String> set = new HashSet<String>(moveSquares);
			if(set.size() < moveSquares.size()){
			    return false;
			}
			
			//TODO Check si bon coté (si le joueur noir a joué en haut ou en bas son premier coup)
			//check ligne
			if(player.equals("blanc")) {
				for (String mv : moveSquares) {
					int line = Character.getNumericValue(mv.charAt(1));
					if( line != 5 && line != 6) {
						return false;
					}
				}
			}
			else if (player.equals("noir")) {
				for (String mv : moveSquares) {
					int line = Character.getNumericValue(mv.charAt(1));
					if( line != 1 && line != 2) {
						return false;
					}
				}
			}
			return true;
		}
		else if(move.contains("-") && move.length() == 5) { // pattern XX-XX
			String[] moveSquares = move.split("-");
			
			
			//Check pattern
			for (String mv : moveSquares) {
				if(!mv.matches("^[ABCDEF][123456]$")) return false;
			}
			
			
			int ligneCase1 = Character.getNumericValue(moveSquares[0].charAt(1)) - 1;
			int colCase1 = -1;
			
			for (Entry<Integer, Character> entry : this.colHashMap.entrySet()) {
		        if (Objects.equals(moveSquares[0].charAt(0), entry.getValue())) {
		        	colCase1 = entry.getKey();
		        }
		    }
			if(colCase1 == -1 ) {
				return false;
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
			int colCase2 = -1;
			
			for (Entry<Integer, Character> entry : this.colHashMap.entrySet()) {
		        if (Objects.equals(moveSquares[1].charAt(0), entry.getValue())) {
		        	colCase2 = entry.getKey();
		        }
		    }
			if(colCase2 == -1 ) {
				return false;
			}
			
			//Test si pas de pièce ennemie sur la case d'arrivée sauf si licorne
			String pieceOnFinalSquare = this.board[ligneCase2][colCase2].type();
			if(player.equals("blanc")) {
				if(pieceOnFinalSquare.equals("n") ) return false;
			}
			else if (player.equals("noir")) {
				if(pieceOnFinalSquare.equals("b") ) return false;
			}
			
			//Calcul des coups possibles depuis la position courante
			ArrayList<String> moves = new ArrayList<String>();
			int lisere = this.board[ligneCase1][colCase1].lisere();
			moves.addAll(movesForSquare(ligneCase1-1, colCase1, lisere-1, moveSquares[0],player));
			moves.addAll(movesForSquare(ligneCase1+1, colCase1, lisere-1, moveSquares[0],player));
			moves.addAll(movesForSquare(ligneCase1, colCase1-1, lisere-1, moveSquares[0],player));
			moves.addAll(movesForSquare(ligneCase1, colCase1+1, lisere-1, moveSquares[0],player));
			
			if(moves.contains(move)) {
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	private ArrayList<String> movesForSquare(int ligne, int col, int level, String initSquare, String player) {
		ArrayList<String> results = new ArrayList<String>();
		
		if(ligne<0 | ligne >5 | col<0 | col>5) {
			return results;
		}
		String typeOfSquare = this.board[ligne][col].type();
		
		
		//Si on tombe sur la licorne adverse au niveau 0, alors le coup est valide
		if(level == 0) {
			if(player.equals("blanc")) {
				if(typeOfSquare.equals("N")) {
					results.add(initSquare + "-" + this.colHashMap.get(col) + Integer.toString(ligne+1));
					return results;
				}
			}
			else if (player.equals("noir")) {
				if(typeOfSquare.equals("B")) {
					results.add(initSquare + "-" + this.colHashMap.get(col) + Integer.toString(ligne+1));
					return results;
				}
			}
		}
		
		if(!typeOfSquare.equals("-")) {
			return results;
		}
		if(level>0) {
			results.addAll(movesForSquare(ligne-1, col, level-1, initSquare,player));
			results.addAll(movesForSquare(ligne+1, col, level-1, initSquare,player));
			results.addAll(movesForSquare(ligne, col-1, level-1, initSquare,player));
			results.addAll(movesForSquare(ligne, col+1, level-1, initSquare,player));
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
		if(player.equals("blanc")) {
			for (int i = 0; i < board.length; i++) {
				Square[] squares = board[i];
				for (int j = 0; j < squares.length; j++) {
					if(squares[j].type().equals("B") | squares[j].type().equals("b")) { //pion qui peut etre déplacé
						int level = squares[j].lisere();
						String start = this.colHashMap.get(j) + Integer.toString(i+1);
						moves.addAll(movesForSquare(i-1,j,level-1,start,player));
						moves.addAll(movesForSquare(i+1,j,level-1,start,player));
						moves.addAll(movesForSquare(i,j-1,level-1,start,player));
						moves.addAll(movesForSquare(i,j+1,level-1,start,player));
					}
				}
			}
		}
		else if (player.equals("noir")) {
			for (int i = 0; i < board.length; i++) {
				Square[] squares = board[i];
				for (int j = 0; j < squares.length; j++) {
					if(squares[j].type().equals("N") | squares[j].type().equals("n")) { //pion qui peut etre déplacé
						int level = squares[j].lisere();
						moves.addAll(movesForSquare(i-1,j,level-1,this.colHashMap.get(j) + Integer.toString(i+1),player));
						moves.addAll(movesForSquare(i+1,j,level-1,this.colHashMap.get(j) + Integer.toString(i+1),player));
						moves.addAll(movesForSquare(i,j-1,level-1,this.colHashMap.get(j) + Integer.toString(i+1),player));
						moves.addAll(movesForSquare(i,j+1,level-1,this.colHashMap.get(j) + Integer.toString(i+1),player));
					}
				}
			}
		}
		else {
			throw new Error("wrong player type : blanc ou noir");
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
		
	}
	
	
	/** vrai lorsque le plateau corespond à une fin de partie
	*/
	public boolean gameOver() {
		return !this.licorneB | !this.licorneN;
	}
	
	
	public static void main(String[] args) {

		EscampeBoard e = new EscampeBoard();
		e.board[0][1].setSquare("noir", SquareType.paladin); //ligne 0 col B
		e.board[0][3].setSquare("noir", SquareType.licorne); //ligne 0 col D
		e.board[0][5].setSquare("noir", SquareType.paladin); //ligne 0 col F
		e.board[1][0].setSquare("noir", SquareType.paladin); //ligne 1 col A
		e.board[1][2].setSquare("noir", SquareType.paladin); //ligne 1 col C
		e.board[1][4].setSquare("noir", SquareType.paladin); //ligne 1 col E
		
		e.board[5][1].setSquare("blanc", SquareType.paladin); //ligne 5 col B
		e.board[5][3].setSquare("blanc", SquareType.licorne); //ligne 5 col D
		e.board[5][5].setSquare("blanc", SquareType.paladin); //ligne 5 col F
		e.board[4][0].setSquare("blanc", SquareType.paladin); //ligne 4 col A
		e.board[4][2].setSquare("blanc", SquareType.paladin); //ligne 4 col C
		e.board[4][4].setSquare("blanc", SquareType.paladin); //ligne 4 col E

		System.out.println("%%%% State %%%%");
		System.out.println(e);
		
		String[] arr = e.possiblesMoves("blanc");
		for (String string : arr) {
			System.out.println("--> " + string + "\n");
		}
		
		e.saveToFile("testB.txt");
		e = new EscampeBoard();
		e.setFromFile("testB.txt");
		System.out.println(e);
		
		System.out.println(e.isValidMove("C6/A6/B5/D5/E6/F5","blanc")); //true
		System.out.println(e.isValidMove("C6/A6/B4/D5/E6/F5","blanc")); //false (4)
		
		System.out.println(e.isValidMove("C1/A1/A1/D2/E2/F2","noir")); //false duplicate
		System.out.println(e.isValidMove("C1/A1/B1/D2/E2/F2","noir")); // true
		
		System.out.println(e.isValidMove("A5-A4","blanc"));
		System.out.println(e.isValidMove("A5-A6","blanc"));
		System.out.println(e.isValidMove("B6-C5","blanc"));
	}
}
