package game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	boolean licorneB;
	boolean licorneN;
	
	Map<Integer, Character> colHashMap  = new HashMap<Integer, Character>() {{
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
		List<String> fileContent = new ArrayList<>();
		List<List<String>> charBoard = null;
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			charBoard = stream
					.filter(line -> !line.startsWith("%"))
					.map(line -> line.split(" ")[1])
					.map(line -> Arrays.asList(line.split("")))
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		charBoard.stream().forEach(c -> {
			c.forEach(System.out::print);
			System.out.print("\n");
		});
		
	}
	
	
	/** sauve la configuration de l'état courant (plateau et pièces restantes) dans un fichier
	* @param fileName le nom du fichier à sauvegarder
	* Le format doit être compatible avec celui utilisé pour la lecture.
	*/
	public void saveToFile(String fileName) {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println("% ABCDEF");
		writer.println("01 -n-N-n 01");
		writer.println("02 n-n-n- 02");
		writer.println("03 ------ 03");
		writer.println("04 ------ 04");
		writer.println("05 b-b-b- 05");
		writer.println("06 -b-B-b 06");
		writer.println("% ABCDEF");
	
		writer.close();
		//Files.write(Paths.get(fileName),(Iterable<String>)stream::iterator);
	}
	
	
	/** indique si le coup <move> est valide pour le joueur <player> sur le plateau courant
	* @param move le coup à jouer,
	* sous la forme "B1-D1" en général,
	* sous la forme "C6/A6/B5/D5/E6/F5" pour le coup qui place les pièces
	* @param player le joueur qui joue, représenté par "noir" ou "blanc"
	*/
	public boolean isValidMove(String move, String player) {
		
		return true;
	}
	
	private ArrayList<String> movesForSquare(int ligne, int col, int level, String initSquare) {
		ArrayList<String> results = new ArrayList<String>();
		
		if(ligne<0 | ligne >5 | col<0 | col>5) {
			return results;
		}
		String typeOfSquare = this.board[ligne][col].type();
		if(!typeOfSquare.equals("-")) {
			return results;
		}
		if(level>0) {
			results.addAll(movesForSquare(ligne-1, col, level-1, initSquare));
			results.addAll(movesForSquare(ligne+1, col, level-1, initSquare));
			results.addAll(movesForSquare(ligne, col-1, level-1, initSquare));
			results.addAll(movesForSquare(ligne, col+1, level-1, initSquare));
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
						System.out.println("calcul pour : " + this.colHashMap.get(j) + Integer.toString(i+1));
						moves.addAll(movesForSquare(i-1,j,level-1,this.colHashMap.get(j) + Integer.toString(i+1)));
						moves.addAll(movesForSquare(i+1,j,level-1,this.colHashMap.get(j) + Integer.toString(i+1)));
						moves.addAll(movesForSquare(i,j-1,level-1,this.colHashMap.get(j) + Integer.toString(i+1)));
						moves.addAll(movesForSquare(i,j+1,level-1,this.colHashMap.get(j) + Integer.toString(i+1)));
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
						moves.addAll(movesForSquare(i,j,level,this.colHashMap.get(j+1) + Integer.toString(i+1)));
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
		//test
		Square s = new Square(1);
		System.out.println(s.lisere());
		
		Square s2 = new Square(2);
		System.out.println(s2.lisere());
	
		Square s3 = new Square(3);
		System.out.println(s3.lisere());
		System.out.println(s3.type()); // expect Empty -
		
		
		s3.setSquare("noir", 1); // expect Licorne noire N
		System.out.println(s3.type());
		
		s3.setSquare("noir", 2); // expect paladin noire n
		System.out.println(s3.type());
		
		
		s3.setSquare("blanc", 1); // expect Licorne blanche B
		System.out.println(s3.type());
		
	
		s3.setSquare("blanc", 2); // expect Paladin blanc b
		System.out.println(s3.type());
		
		EscampeBoard e = new EscampeBoard();
		e.board[0][1].setSquare("noir", 2); //ligne 0 col B
		e.board[0][3].setSquare("noir", 1); //ligne 0 col D
		e.board[0][5].setSquare("noir", 2); //ligne 0 col F
		e.board[1][0].setSquare("noir", 2); //ligne 1 col A
		e.board[1][2].setSquare("noir", 2); //ligne 1 col C
		e.board[1][4].setSquare("noir", 2); //ligne 1 col E
		
		e.board[5][1].setSquare("blanc", 2); //ligne 5 col B
		e.board[5][3].setSquare("blanc", 1); //ligne 5 col D
		e.board[5][5].setSquare("blanc", 2); //ligne 5 col F
		e.board[4][0].setSquare("blanc", 2); //ligne 4 col A
		e.board[4][2].setSquare("blanc", 2); //ligne 4 col C
		e.board[4][4].setSquare("blanc", 2); //ligne 4 col E
		System.out.println(e);
		String[] arr = e.possiblesMoves("blanc");
		for (String string : arr) {
			System.out.println("--> " + string + "\n");
		}
		
		e.saveToFile("test2.txt");
		e.setFromFile("test2.txt");
	}
}
