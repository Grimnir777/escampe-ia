package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import game.Square;

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
		
		board.play("C6/A6/B5/D5/E6/F5", "noir");
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
	}
	
	


	@Test
	void PositionPionTest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		board.play("C6/A6/B5/D5/E6/F5", "noir");
		Square[][] squares = board.getBoard();
		assertEquals(squares[5][2].type(), "N"); //C6
		assertEquals(squares[5][0].type(), "n"); //A6
		assertEquals(squares[4][1].type(), "n"); //B5
		assertEquals(squares[4][3].type(), "n"); //D5
		assertEquals(squares[5][4].type(), "n"); //E6
		assertEquals(squares[4][5].type(), "n"); //F5
	}
	

	@Test
	void IsvalidFirstmovetest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		// Pattern invalide
		assertEquals(board.isValidMove("Z6/Y6/T5/Y5/U6/P5", "noir"), false);
		
		// Premier coup valide
		assertEquals(board.isValidMove("C6/A6/B5/D5/E6/F5", "noir"), true);
		
		// Premier coup invalide sur plus de 2 lignes
		assertEquals(board.isValidMove("C6/A4/B3/D1/E2/F5", "noir"), false);
		
		// Premier coup invalide (2 fois la même case)
		assertEquals(board.isValidMove("C6/C6/A5/D5/E6/F5", "noir"),false);

		// Premier coup invalide car joueur noir n'a pas joué
		assertEquals(board.isValidMove("C6/A6/B5/D5/E6/F5", "blanc"),false);
		
		// Joueur noir joue un premier coup valide
		board.play("C6/A6/B5/D5/E6/F5", "noir");
		
		// Premier coup invalide car le joueur noir a choisi ce côté
		assertEquals(board.isValidMove("C6/A6/B5/D5/E6/F5", "blanc"),false);
		
		// Premier coup valide de l'autre côté
		assertEquals(board.isValidMove("C1/A1/B2/D2/E1/F2", "blanc"),true);
	}
	
	
	@Test
	void IsvalidClassicMovetest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		// Pattern incorrect
		assertEquals(board.isValidMove("Z1-Z2", "noir"), false);
		
		// Aucun coup d'init n'a été joué (dernier liséré à -1)
		assertEquals(board.isValidMove("A1-A2", "noir"), false);
		assertEquals(board.isValidMove("A1-A2", "blanc"), false);
		
		
		board.play("A5/B5/C5/D5/E5/F5", "noir");
		board.play("A2/B2/C2/D2/E2/F2", "blanc");
		
		// Pas de pièce blanche sur la case A1
		assertEquals(board.isValidMove("A1-A2", "blanc"), false);
		
		// Le joueur blanc n'a pas joué son premier coup après l'initialisation
		assertEquals(board.isValidMove("A5-A4", "noir"), false);
		
		//Le joueur blanc joue un coup valide
		assertEquals(board.isValidMove("B2-B3", "blanc"), true);
		board.play("B2-B3", "blanc");
		
		
		// Un paladin sur un autre paladin
		assertEquals(board.isValidMove("D5-D2", "noir"), false);
		
		// Pas de pièce noire sur la case D6
		assertEquals(board.isValidMove("D6-D4", "noir"), false);
		
		// Le coup ne fait pas partie des coups possibles
		assertEquals(board.isValidMove("C5-C3", "noir"), false);
		

	}
	
	@Test
	void IsvalidSpecialMovetest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		board.play("A5/B5/C5/D5/E5/F5", "noir");
		board.play("A2/B2/C2/D2/E2/F2", "blanc");
		
		// On essaie de tuer la licorne adverse avec notre propre licorne
		assertEquals(board.isValidMove("A2-A5", "blanc"), false);
	}
	
	@Test
	void EndGametest() {
		game.EscampeBoard board = new game.EscampeBoard();
		
		board.play("A5/B5/C5/D5/E5/F5", "noir");
		board.play("A2/B2/C2/D2/E2/F2", "blanc");
		board.play("A2-A5", "blanc");

		assertEquals(board.gameOver(),true);
	}
	

	

}
