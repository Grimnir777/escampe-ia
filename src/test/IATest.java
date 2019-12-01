package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import game.AlphaBeta;
import game.Heuristique;



public class IATest {
	@Test
	void newBoardTest() {
		game.EscampeBoard board = new game.EscampeBoard();
		board.play("B2/A1/C1/D2/E1/F2", "noir");
		board.play("C6/A6/B5/D5/E6/F5", "blanc");
		AlphaBeta alphaBeta = new AlphaBeta(new Heuristique() , "blanc", "noir",1);
		//%  ABCDEF
		//01 n-n-n- 01
		//02 -N-n-n 02
		//03 ------ 03
		//04 ------ 04
		//05 -b-b-b 05
		//06 b-B-b- 06
		//%  ABCDEF
		assertEquals(alphaBeta.meilleurCoup(board), "B5-B2");
	}
}
