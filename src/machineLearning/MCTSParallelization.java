package machineLearning;

import java.util.ArrayList;
import java.util.HashMap;

import game.EscampeBoard;

public class MCTSParallelization {
	private static ArrayList<Thread> threads = new ArrayList<Thread>();
	private static int games = 10;
	private static int darts = 1000;
	public static HashMap<String, Double> earningSums;
	
	public static int globalNbVisits;
	public static double globalEarningSum;
	
	public static synchronized void addEarningSum(double earningSum) {
		globalEarningSum += earningSum;
	}
	
	public static synchronized void addNbVisits(int nbVisit) {
		globalNbVisits += nbVisit;
	}
	
	public static synchronized void addChildsToHashMap(ArrayList<Node> childs) {
		for (Node node : childs) {
			if(node.nbVisits!=0) {
				String lastMove = node.board.lastMove;
				if(earningSums.containsKey(lastMove)) {
					earningSums.replace(lastMove, earningSums.get(lastMove) + node.earningSum);
				}
				else {
					earningSums.put(lastMove, node.earningSum);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		earningSums = new HashMap<>();
		
		EscampeBoard board = new EscampeBoard();
		Node node = new Node(true, board);
		node.expand();
		
		
		class MCTSThread extends Thread {
			public void run() {
				
				for(int i = 0; i < darts; i++) {
					node.selectAction();
				}
				//System.out.println(node.toString());
				MCTSParallelization.addEarningSum(node.earningSum);
				MCTSParallelization.addNbVisits(node.nbVisits);
				//System.out.println(node.childs.size());
				MCTSParallelization.addChildsToHashMap(node.childs);
			}
		}
		long begin = System.currentTimeMillis();
		for(int i = 0; i < games; i++) {
			threads.add(new MCTSThread());
			threads.get(i).start();
		}
		// Join all of the threads to the main thread
		try {
			for(Thread thread: threads) {
				thread.join();
			}
			//System.out.println("nb" + globalNbVisits);
			//System.out.println("earning sum " + globalEarningSum);
			//System.out.println(earningSums);
			HashMap.Entry<String, Double> maxEntry = null;
			for (HashMap.Entry<String,Double> entry : earningSums.entrySet())
			{
			    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
			    {
			        maxEntry = entry;
			    }
			}
			System.out.println(maxEntry);
			System.out.println("Time of computing: " + Long.toString((System.currentTimeMillis() - begin)) +"ms" );
			
		}
		catch(InterruptedException e) {
			System.out.println("Something went wrong with the threads.");
		}
	}
}
