package game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import algos.AdvancedHeuristique;
import algos.AlphaBeta;
import algos.BasicHeuristique;
import algos.OptimusHeuristique;

public class EscampeBoard implements Partie1, Cloneable{
	
	private int [][] liseres = new int[][] {{1,2,2,3,1,2},
											{3,1,3,1,3,2},
											{2,3,1,2,1,3},
											{2,1,3,2,3,1},
											{1,3,1,3,1,2},
											{3,2,2,1,3,2}};
											
	protected Square[][] board;
	
	private boolean licorneB;
	private boolean licorneN;
	
	private boolean firstB;
	private boolean firstN;
	private boolean firstChooseDown;
	private int lastLisere;
	public String lastMove;
	
	private int[] liseresBlanc= {0,0,0};
	private int[] liseresNoir = {0,0,0};
	
	public SquareTools squareTool;
	
	private ArrayList<String> caseFirstDown = new ArrayList<String>() {
		private static final long serialVersionUID = -3708238127665825076L;
	{
	    add("A1");
	    add("B1");
	    add("C1");
	    add("D1");
	    add("E1");
	    add("F1");
	    add("A2");
	    add("B2");
	    add("C2");
	    add("D2");
	    add("E2");
	    add("F2");
	}};
	
	private ArrayList<String> caseFirstUp = new ArrayList<String>() {
		private static final long serialVersionUID = -4694394783351557969L;
	{
	    add("A5");
	    add("B5");
	    add("C5");
	    add("D5");
	    add("E5");
	    add("F5");
	    add("A6");
	    add("B6");
	    add("C6");
	    add("D6");
	    add("E6");
	    add("F6");
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
		this.squareTool = new SquareTools();
	}
	
	/*
	 * Getter & Setters
	 * */
	
	public Square[][] getBoard() {
		return this.board.clone();
	}
	
	public int[] getLiseresBlanc() {
		return liseresBlanc;
	}

	public int[] getLiseresNoir() {
		return liseresNoir;
	}
	
	/*
	 * Methode clone
	 * */
	
	@Override
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
					e.printStackTrace();
				}
			}
		 }
		 newBoard.squareTool = new SquareTools();
		 newBoard.liseresBlanc = this.liseresBlanc.clone();
		 newBoard.liseresNoir = this.liseresNoir.clone();
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
	
	public boolean getFirstChooseDown() {
		return firstChooseDown;
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
					int line = squareTool.getCol(mv);
					if(this.firstChooseDown && (line==4 || line==5)) return false;
					if(!this.firstChooseDown && (line==0 || line==1)) return false;
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
			
			int ligneCase1 = this.squareTool.getLigne(moveSquares[0]);
			int colCase1 = this.squareTool.getCol(moveSquares[0]);
			
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

			int ligneCase2 = this.squareTool.getLigne(moveSquares[1]);
			int colCase2 = this.squareTool.getCol(moveSquares[1]); 

			
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
		
		String nameOfSquare = this.squareTool.getStringValue(col,ligne);
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
					String mvmt = initSquare + "-" + this.squareTool.getStringValue(col,ligne);
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
			results.add(initSquare + "-" + this.squareTool.getStringValue(col, ligne));
		}
		return results;
	}
	
	
	/** calcule les coups possibles pour le joueur <player> sur le plateau courant
	* @param player le joueur qui joue, représenté par "noir" ou "blanc"
	*/
	public String[] possiblesMoves(String player) {		
		ArrayList<String> moves = new ArrayList<String>();
		if(this.firstN &&  player.equals("noir")) {
			moves.addAll(this.possibleMovesFirst(this.caseFirstDown));
			moves.addAll(this.possibleMovesFirst(this.caseFirstUp));
		}
		if(this.firstB && !this.firstN && player.equals("blanc")) {
			if(this.firstChooseDown) {
				moves.addAll(this.possibleMovesFirst(this.caseFirstUp));
			} else {
				moves.addAll(this.possibleMovesFirst(this.caseFirstDown));
			}
		}
		
		
		for (int i = 0; i < board.length; i++) {
			Square[] squares = board[i];
			for (int j = 0; j < squares.length; j++) {
				if(( (squares[j].type().equals("B") | squares[j].type().equals("b")) && player.equals("blanc") ) || ( (squares[j].type().equals("N") | squares[j].type().equals("n")) && player.equals("noir") ) ) { //pion qui peut etre déplacé
					int level = squares[j].lisere();
					if(level == this.lastLisere || this.lastLisere == -1 || this.lastLisere == 0) {
						String start = this.squareTool.getStringValue(j,i);
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
	
	public ArrayList<String> possibleMovesFirst(ArrayList<String> cases) {
		ArrayList<String> combinaisons = new ArrayList<String>();
		for (int u = 0; u < cases.size(); u++) {
			ArrayList<String> newArray = (ArrayList<String>) cases.clone();
			newArray.remove(u);
			for(int i=  0; i <= 6; i++)
	        for(int j=i+1; j <= 7; j++)
	        for(int k=j+1; k <= 8; k++)
	        for(int l=k+1; l <= 9; l++)
	        for(int m=l+1; m <= 10; m++)
        	combinaisons.add(cases.get(u) + "/" + newArray.get(i) + "/" + newArray.get(j)  + "/"+ newArray.get(k)  + "/"+ newArray.get(l) + "/" + newArray.get(m)  );
		}
		return combinaisons;     
	}
	
	
	
	public String[] possiblesMovesForHeuristique(String player) {		
		ArrayList<String> moves = new ArrayList<String>();
		
		for (int i = 0; i < board.length; i++) {
			Square[] squares = board[i];
			for (int j = 0; j < squares.length; j++) {
				if(( (squares[j].type().equals("B") | squares[j].type().equals("b")) && player.equals("blanc") ) || ( (squares[j].type().equals("N") | squares[j].type().equals("n")) && player.equals("noir") ) ) { //pion qui peut etre déplacé
					int level = squares[j].lisere();

					String start = this.squareTool.getStringValue(j, i);
					ArrayList<String> previous = new ArrayList<String>();
					previous.add(start);
					moves.addAll(movesForSquare(i-1,j,level-1,start,player,previous));
					moves.addAll(movesForSquare(i+1,j,level-1,start,player,previous));
					moves.addAll(movesForSquare(i,j-1,level-1,start,player,previous));
					moves.addAll(movesForSquare(i,j+1,level-1,start,player,previous));							
					
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
			int[] liseres = {0,0,0};
			List<String> moveSquares =  new ArrayList<String>(Arrays.asList(move.split("/")));
			int ligne = this.squareTool.getLigne(moveSquares.get(0));
			int col = -1;
			col = this.squareTool.getCol(moveSquares.get(0));
			this.board[ligne][col].setSquare(player, SquareType.licorne);
			liseres[this.board[ligne][col].lisere()-1]++;
			for (int i = 1; i < moveSquares.size(); i++) {
				ligne = this.squareTool.getLigne(moveSquares.get(i));
				col = this.squareTool.getCol(moveSquares.get(i));
				this.board[ligne][col].setSquare(player, SquareType.paladin);
				liseres[this.board[ligne][col].lisere()-1]++;
			}
			
			if(player.equals("blanc")) {
				this.firstB = false;
				this.liseresBlanc = liseres.clone();
			}
			else if(player.equals("noir")) {
				this.firstN = false;
				if(ligne==4 || ligne == 5) firstChooseDown=true;
				else firstChooseDown=false;
				this.liseresNoir = liseres.clone();
			}
		}
		else if(move.contains("-") && move.length() == 5) {
			List<String> moveSquares =  new ArrayList<String>(Arrays.asList(move.split("-")));
			
			int ligne1 = this.squareTool.getLigne(moveSquares.get(0)); 
			int col1 = this.squareTool.getCol(moveSquares.get(0));
			int ligne2 = this.squareTool.getLigne(moveSquares.get(1));
			int col2 = this.squareTool.getCol(moveSquares.get(1));
			
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
				liseresBlanc[this.board[ligne1][col1].lisere()-1]--;
				liseresBlanc[this.board[ligne2][col2].lisere()-1]++;
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
				liseresNoir[this.board[ligne1][col1].lisere()-1]--;
				liseresNoir[this.board[ligne2][col2].lisere()-1]++;
			}
			this.lastLisere = this.board[ligne2][col2].lisere();
			this.board[ligne1][col1].resetSquare();
		}
		this.lastMove = move;
		
	}
	
	
	/** vrai lorsque le plateau corespond à une fin de partie
	*/
	public boolean gameOver() {
		return !this.licorneB | !this.licorneN;
	}

	public void pass() {
		this.lastLisere = 0;
	}

	public static void main(String[] args) {
		/*
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
		*/
		/*
		EscampeBoard e = new EscampeBoard();
		e.play("C6/A6/B5/D5/E6/F5", "noir");
		//e.play("B6/A5/C5/D5/E6/F6", "noir");
		e.play("C2/F1/A2/C1/D1/D2", "blanc");
		
		System.out.println("Placement initial");
		for (int lis : e.getLiseresNoir()) {
			System.out.println("LIS Noir  ==> " + lis);
		}
		for (int lis : e.getLiseresBlanc()) {
			System.out.println("LIS Blanc ==> " + lis);
		}
		
		
		System.out.println("blanc");
		for (String string : e.possiblesMoves("blanc")) {
			System.out.println("--> " + string);
		}
		System.out.println("\n-------- Coup joué C2-B4 par le joueur blanc --------");
		e.play("C2-B4", "blanc");
		for (int lis : e.getLiseresBlanc()) {
			System.out.println("LIS Blanc ==> " + lis);
		}
		
		
		System.out.println("noir");
		for (String string : e.possiblesMoves("noir")) {
			System.out.println("==> " + string);
		}
		System.out.println("\n-------- Coup joué A5-B5 par le joueur noir --------");
		e.play("A5-B5", "noir");
		for (int lis : e.getLiseresNoir()) {
			System.out.println("LIS Noir  ==> " + lis);
		}


		System.out.println("blanc");
		for (String string : e.possiblesMoves("blanc")) {
			System.out.println("--> " + string);
		}
		
		
		AlphaBeta ab = new AlphaBeta(new AdvancedHeuristique() , "blanc", "noir",6);
		long begin = System.currentTimeMillis();
		System.out.println("meilleur coup : " + ab.meilleurCoup(e));
		System.out.println("Time of computing: " + Long.toString((System.currentTimeMillis() - begin)) +"ms" );
		

		AlphaBeta ab2 = new AlphaBeta(new OptimusHeuristique() , "blanc", "noir",6);
		begin = System.currentTimeMillis();
		System.out.println("meilleur coup : " + ab2.meilleurCoup(e));
		System.out.println("Time of computing: " + Long.toString((System.currentTimeMillis() - begin)) +"ms" );
		
		
		AlphaBeta ab3 = new AlphaBeta(new BasicHeuristique() , "blanc", "noir",6);
		begin = System.currentTimeMillis();
		System.out.println("meilleur coup : " + ab3.meilleurCoup(e));
		System.out.println("Time of computing: " + Long.toString((System.currentTimeMillis() - begin)) +"ms" );
		*/
		
		EscampeBoard e = new EscampeBoard();
		e.play("C6/A6/B5/D5/E6/F5", "noir");
		e.play("C1/A2/B2/D2/E2/F2", "blanc");
		e.play("F2-E3", "blanc");
		e.pass();
		e.play("E3-F3", "blanc");
		e.play("E6-F4", "noir");
		e.play("D2-D3", "blanc");
		e.play("F5-E4", "noir");
		System.out.println("blanc");
		for (String string : e.possiblesMoves("blanc")) {
			System.out.println("--> " + string);
		}
		
		AlphaBeta ab = new AlphaBeta(new AdvancedHeuristique() , "blanc", "noir",6);
		long begin = System.currentTimeMillis();
		System.out.println("meilleur coup : " + ab.meilleurCoup(e));
		System.out.println("Time of computing: " + Long.toString((System.currentTimeMillis() - begin)) +"ms" );
		

		AlphaBeta ab2 = new AlphaBeta(new OptimusHeuristique() , "blanc", "noir",6);
		begin = System.currentTimeMillis();
		System.out.println("meilleur coup : " + ab2.meilleurCoup(e));
		System.out.println("Time of computing: " + Long.toString((System.currentTimeMillis() - begin)) +"ms" );
		
		
		AlphaBeta ab3 = new AlphaBeta(new BasicHeuristique() , "blanc", "noir",6);
		begin = System.currentTimeMillis();
		System.out.println("meilleur coup : " + ab3.meilleurCoup(e));
		System.out.println("Time of computing: " + Long.toString((System.currentTimeMillis() - begin)) +"ms" );
		
	}
}
