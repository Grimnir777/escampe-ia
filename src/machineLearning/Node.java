package machineLearning;

import java.util.ArrayList;

import game.EscampeBoard;

public class Node {
	public boolean leef;			 // true if the childs haven't been created yet
	public boolean terminal;		 // true if no action possible
	public boolean playedByIA;		 // true if node played by IA
	public Node parent;				 // direct parent of the node
	public ArrayList<Node> childs;	 // direct childs of the node
	public int nbVisits;			 // how many times did we pass by this node 
	public double earningSum;		 // earning sum reached by this node or its descendant
	
	
	// specific
	public EscampeBoard board;
	
	
	@Override
	public String toString() {
		String val =  
				  "leaf :"+ this.leef
				+ "\nterminal :"+ this.terminal
				+ "\nplayedByIA :"+ this.playedByIA
				+ "\nchilds size :"+ this.childs.size() 
				+ "\nhas parent ? :"+ (this.parent!=null)
				+ "\nearningSum :"+ this.earningSum
				+ "\nnbVisits :"+ this.nbVisits + "\n\n"
				;
		return val;
	}
	public Node(boolean playedByIA, EscampeBoard board) {
		this.leef = false;
		this.terminal = false; 
		this.playedByIA = playedByIA; 
		this.parent = null;
		this.childs = new ArrayList<Node>();
		this.nbVisits = 0;
		this.earningSum = 0.0;
		this.board = board.clone();
	}
	
	public Node(boolean playedByIA, EscampeBoard board, boolean leef) {
		this.leef = leef;
		this.terminal = false; 
		this.playedByIA = playedByIA; 
		this.parent = null;
		this.childs = new ArrayList<Node>();
		this.nbVisits = 0;
		this.earningSum = 0.0;
		this.board = board.clone();
	}
	
	// from the current node go to a leef
	public void selectAction() {
		this.terminal = true;
		Node node = this;
		//System.out.println("root ");
		//System.out.println( node.toString());
		while (!node.leef) {
			node = node.select();
		}
		//System.out.println("at the end of select ");
		//System.out.println(node.toString());
		node.rollOut(node);
		//System.out.println("After rollout ");
		//System.out.println(node.toString());
	}
	
	// from the current Node choose the best child
	public Node select() {
		Node choosenNode = null;
		double bestValue = Double.MIN_VALUE;
		for (Node child : this.childs) {
			double value;
			if(child.nbVisits == 0) {
				value = Math.random() + 1.41 * Math.sqrt(Math.log(this.nbVisits+1));
			}
			else {
				value = (child.earningSum / child.nbVisits) + (1.41 * Math.sqrt(Math.log(this.nbVisits+1)/child.nbVisits));
			}
			
			if (value > bestValue) {
				choosenNode = child;
				bestValue = value;
			}
		}
		if(choosenNode != null) {
			choosenNode.parent = this;			
		}
		else {
			return this;
		}
		return choosenNode;
	}
	
	public void rollOut(Node node) {
		Node child = null;
		if(!node.terminal) {
			node.expand();
			child = winningNode(node, node.playedByIA);
			if (child == null) {
				child = node.childs.get((int) Math.random() * node.childs.size());
			}
			child.parent = node;
			if (!child.terminal) {
				this.rollOut(child);
			}
			else {
				child.updateValue(child.earningSum);
			}
		}
	}
	
	public void expand() {
		this.leef = false;
		this.childs = findChilds();
		for (Node child : this.childs) {
			child.value();
		}
	}
	
	public void updateValue(double value) {
		this.nbVisits++;
		this.earningSum += value;
		if(this.parent != null) {
			this.parent.updateValue(value*0.9);
		}
	}
	
	public void value() {
		if(this.board.gameOver() && this.board.getLicorneNState()) this.earningSum=100;
		if(this.board.gameOver() && this.board.getLicorneBState()) this.earningSum=-100;
	}
	
	public Node winningNode(Node node, boolean isIA) {

		for (Node n : node.childs) {
			if(isIA && !n.board.getLicorneBState()) {
				n.terminal = true;
				return n;
			}
			if(!isIA && !n.board.getLicorneNState()) {
				n.terminal = true;
				return n;
			}
		}
		return null;
	}
	
	// Create the childs of current node and 
	public ArrayList<Node> findChilds() {
		ArrayList<Node> childsOfNode = new ArrayList<Node>();
		String player = this.playedByIA? "noir":"blanc";
		
		String[] possibleMoves = this.board.possiblesMoves(player);
		if(possibleMoves.length == 0) {
			EscampeBoard boardCP = this.board.clone();
			boardCP.pass();
			Node n = new Node(!this.playedByIA,boardCP,true);
			childsOfNode.add(n);
		}
		for (String move : possibleMoves) {
			EscampeBoard boardCP = this.board.clone();
			boardCP.play(move, player);
			Node n = new Node(!this.playedByIA,boardCP,true);
			childsOfNode.add(n);
		}
		return childsOfNode;
	}
	
	public Node findBestNode() {
		Node returnNode = this.childs.get(0);
		double bestValue = returnNode.earningSum;
		for (Node child : this.childs) {
			double value = child.earningSum;
			if (value > bestValue) {
				returnNode = child;
				bestValue = value;
			}
		}
		return returnNode;
	}
}
