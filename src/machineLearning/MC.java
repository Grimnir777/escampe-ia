package machineLearning;

import algos.AdvancedHeuristique;
import algos.AlphaBeta;
import game.EscampeBoard;

public class MC {
	public static void main(String[] args) {
		EscampeBoard board = new EscampeBoard();
		board.play("C6/A6/B5/D5/E6/F5", "noir");
		board.play("C2/F1/A2/C1/D1/D2", "blanc");
		
		AlphaBeta ab = new AlphaBeta(new AdvancedHeuristique() , "noir", "blanc",2);
		System.out.println("meilleur coup : " + ab.meilleurCoup(board));
		
		Node node = new Node(true, board);
		node.expand();
		node.selectAction();
		node.selectAction();
		/*
		for (int i = 0; i < 10; i++) {
			if(i%10==0)System.out.println("+10");
			node.selectAction();
		}
		*/
		
		System.out.println(node.earningSum);
		Node bestNode = node.findBestNode();
		System.out.println(bestNode.board.toString());
	}
}
