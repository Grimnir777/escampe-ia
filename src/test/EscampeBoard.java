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
		//TODO jouer premier et deuxieme coup
		//Sauvegarder board
		//creer nouveau board
		//read board 
		//assert les 2 baord sont �gaux
	}
	
	//TODO Play -> Premier coup v�rification que l'on peut faire qu'un seul coup � 17 caract�res
	//TODO Play -> Coup normal
	
	//TODO Play -> Coup normal arriv� sur licorne adverse
	
	
	//TODO isValidMove Premier coup valide
	//TODO isValidMove Premier coup invalide sur plus de 2 lignes
	//TODO isValidMove Premier coup invalide (2 fois la m�me case)
	//TODO isValidMove Premier coup invalide du mauvais c�t�
	

}
