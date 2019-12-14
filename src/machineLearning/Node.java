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
	
	// from the current node go to a leef
	public void selectAction() {
		this.terminal = true;
		Node node = this;
		while (!node.leef) {
			node = node.select();
		}
		this.rollOut(node);
	}
	
	
	public Node select() {
		Node choosenNode = null;
		double bestValue = Double.MIN_VALUE;
		double c = Math.sqrt(2);
		for (Node child : this.childs) {
			double value;
			if(child.nbVisits == 0) {
				value = Math.random() + c * Math.sqrt(Math.log(this.nbVisits+1));
			}
			else {
				value = (child.earningSum / child.nbVisits) + (c * Math.sqrt(Math.log(this.nbVisits+1)/child.nbVisits));
			}
			
			if (value > bestValue) {
				choosenNode = child;
				bestValue = value;
			}
		}
		choosenNode.parent = this;
		return choosenNode;
	}
	
	public void rollOut(Node node) {
		childs = null;
		if(!node.terminal) {
			node.expand();
			Node child = winningNode(node.playedByIA);
			if (child == null) {
				child = node.childs.get((int) Math.random() * node.childs.size());
			}
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
		if(parent != null) {
			parent.updateValue(value*0.9);
		}
	}
	
	public void value() {
		if(this.playedByIA) {
			if(this.board.gameOver() && this.board.getLicorneNState()) this.earningSum=100;
			if(this.board.gameOver() && this.board.getLicorneBState()) this.earningSum=-100;
		} else {
			if(this.board.gameOver() && this.board.getLicorneBState()) this.earningSum=100;
			if(this.board.gameOver() && this.board.getLicorneNState()) this.earningSum=-100;
		}
	}
	
	public Node winningNode(boolean isIA) {
		for (Node node : childs) {
			if(isIA && node.board.gameOver() && node.board.getLicorneNState()) {
				return node;
			}
			if(!isIA && node.board.gameOver() && node.board.getLicorneBState()) {
				return node;
			}
		}
		return null;
	}
	
	// Create the childs of current node and 
	public ArrayList<Node> findChilds() {
		ArrayList<Node> childsOfNode = new ArrayList<Node>();
		String player = this.playedByIA? "noir":"blanc";
		for (String move : this.board.possiblesMoves(player)) {
			EscampeBoard boardCP = this.board.clone();
			boardCP.play(move, player);
			Node n = new Node(!this.playedByIA,boardCP);
			childsOfNode.add(n);
		}
		return childsOfNode;
	}
}
