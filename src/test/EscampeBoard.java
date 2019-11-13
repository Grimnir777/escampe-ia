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

}
