package machineLearning;

import java.text.DecimalFormat;

import algos.AlphaBeta;
import algos.OptimusHeuristique;
import game.EscampeBoard;


public class MC {
	public static void main(String[] args) {
		testMidGame();
	}
	
	public static void testBegin() {
		EscampeBoard board = new EscampeBoard();
		//F6/E5/A6/C6/D6/E6 heuristique niv 1

		//AlphaBeta ab = new AlphaBeta(new OptimusHeuristique() , "noir", "blanc",1);
		//System.out.println("meilleur coup : " + ab.meilleurCoup(board));
		
		Node node = new Node(true, board);
		node.expand();

		double nbIter = 500;
		for (int i = 0; i < nbIter; i++) {
			System.out.print(new DecimalFormat("0.00").format((i/nbIter)*100) + " %\r");
			node.selectAction();
		}
		
		
		System.out.println(node.earningSum);
		Node bestNode = node.findBestNode();
		System.out.println(bestNode.board.lastMove);
	}
	
	public static void testMidGame() {
		EscampeBoard board = new EscampeBoard();
		//board.play("C6/A5/B5/D5/E5/F5", "noir");
		//board.play("C2/F1/A2/C1/D1/D2", "blanc");
		board.play("C2/F1/A2/C1/D1/D2", "noir");
		board.play("A5/B5/C5/D5/E5/F5", "blanc");
		board.play("C5-C4", "blanc");
		
		
		AlphaBeta ab = new AlphaBeta(new OptimusHeuristique() , "noir", "blanc",5);
		System.out.println("meilleur coup avec heuristique : " + ab.meilleurCoup(board));
		
		Node node = new Node(true, board);
		node.expand();

		long nbIter = 5000;
		for (int i = 0; i < nbIter; i++) {
			node.selectAction();
		}
		
		
		System.out.println(node.earningSum);
		Node bestNode = node.findBestNode();
		System.out.println(bestNode.board.lastMove);
	}
}
