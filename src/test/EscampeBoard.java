package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import game.EscampeBoard;
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
		board.saveToFile("testC");
		
		//creer nouveau board
		game.EscampeBoard ne = new game.EscampeBoard();
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
		assertEquals(squares, board);
	}
	
	


	@Test
	void Playtest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		//TODO Play -> Premier coup vérification que l'on peut faire qu'un seul coup à 17 caractères
		board.play("C6/A6/B5/D5/E6/F5", "noir");
		board.play("A5/B6/C5/A6/B5/C2", "noir");
		//board.play("C0/A0/B0/D0/E0/F0", "noir");
		
		
		//TODO Play -> Coup normal
		board.play("B5-A5", "noir"); 
		
		//TODO Play -> Coup normal arrivé sur licorne adverse
		//board.play("A6-C0", "noir"); 
	}
	

	@Test
	void Isvalidmovetest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		//TODO isValidMove Premier coup valide
		board.isValidMove("C6/A6/B5/D5/E6/F5", "noir");
		
		//TODO isValidMove Premier coup invalide sur plus de 2 lignes
		board.isValidMove("C6/A4/B3/D1/E2/F5", "blanc");
		
		//TODO isValidMove Premier coup invalide (2 fois la même case)
		board.isValidMove("C6/C6/A5/D5/E6/F5", "noir");

		//TODO isValidMove Premier coup invalide du mauvais côté
		board.isValidMove("C6/A6/B5/D5/E6/F5", "blanc");
	}
	

	

}
