package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import game.Square;
import game.SquareType;

class EscampeBoard {

	@Test
	void newBoardTest() {
		game.EscampeBoard board = new game.EscampeBoard();
		Square[][] squares = board.getBoard();
		for (int line = 0; line < squares.length; line++) {
			for (int col = 0; col < squares[line].length; col++) {
				assertEquals(squares[line][col].type(),"-");
			}
		}
	}
	
	@Test
	void newBoardNoMoveTest() {
		game.EscampeBoard board = new game.EscampeBoard();
		assertEquals(board.possiblesMoves("noir").length,0);
		assertEquals(board.possiblesMoves("blanc").length,0);
	}
	
	@Test
	void SaveAndReadBoardTest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		
		//TODO jouer premier et deuxieme coup
		board.play("C6/A6/B5/D5/E6/F5", "noir");
		board.play("C6-D6", "noir"); 
		//Sauvegarder board
		board.saveToFile("testC.txt");
		
		
		game.EscampeBoard board2 = new game.EscampeBoard();
		board2.setFromFile("testC.txt");
		
		Square[][] squares = board.getBoard();
		Square[][] squares2 = board2.getBoard();
		
		for (int line = 0; line < squares.length; line++) {
			for (int col = 0; col < squares[line].length; col++) {
				assertEquals(squares[line][col].type(),squares2[line][col].type());
			}
		}
		
		//assertEquals(board.getBoard(),board2.getBoard());
		/*
		//creer nouveau board
		//game.EscampeBoard ne = new game.EscampeBoard();
		Square[][] squares = board.getBoard();
		squares[5][3].setSquare("noir", SquareType.licorne); //ligne 6 col D
		squares[5][0].setSquare("noir", SquareType.paladin); //ligne 6 col A
		squares[4][1].setSquare("noir", SquareType.paladin); //ligne 5 col B
		squares[4][3].setSquare("noir", SquareType.paladin); //ligne 5 col D
		squares[5][4].setSquare("noir", SquareType.paladin); //ligne 6 col E
		squares[4][5].setSquare("noir", SquareType.paladin); //ligne 5 col F
		board.saveToFile("testD.txt");
		
		
		//read board 
		board.setFromFile("testD.txt");
		
		//assert les 2 board sont égaux
		for (int line = 0; line < squares.length; line++) {
			for (int col = 0; col < squares[line].length; col++) {
				assertEquals(squares[line][col].type(),"-");
			}
		}*/
		
	}
	
	


	@Test
	void Playtest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		//TODO Play -> Premier coup vérification que l'on peut faire qu'un seul coup à 17 caractères
		board.play("C6/A6/B5/D5/E6/F5", "noir");
		Square[][] squares = board.getBoard();
		assertEquals(squares[5][2].type(), "N");
		System.out.println(squares[5][2].type());
		
		//TODO Play -> Coup normal
		board.play("B5-A5", "noir"); 
		
		//TODO Play -> Coup normal arrivé sur licorne adverse
		//board.play("A6-C0", "noir"); 
	}
	

	@Test
	void IsvalidFirstmovetest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		// isValidMove Premier coup valide
		assertEquals(board.isValidMove("C6/A6/B5/D5/E6/F5", "noir"), true);
		
		//isValidMove Premier coup invalide sur plus de 2 lignes
		assertEquals(board.isValidMove("C6/A4/B3/D1/E2/F5", "blanc"), false);
		//isValidMove Premier coup invalide (2 fois la même case)
		assertEquals(board.isValidMove("C6/C6/A5/D5/E6/F5", "noir"),false);

		//isValidMove Premier coup invalide car joueur noir n'a pas joué
		assertEquals(board.isValidMove("C6/A6/B5/D5/E6/F5", "blanc"),false);
		
		//joue coup valide
		board.play("C6/A6/B5/D5/E6/F5", "noir");
		//isValidMove Premier coup invalide car le joueur noir a choisi ce côté
		assertEquals(board.isValidMove("C6/A6/B5/D5/E6/F5", "blanc"),false);
		
		//par contre on peut jouer ce coup là de l'autre côté
		assertEquals(board.isValidMove("C1/A1/B2/D2/E1/F2", "blanc"),true);
	}
	

	

}
