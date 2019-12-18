package algos;

import game.EscampeBoard;

public class AlphaBeta {
	private final static int PROFMAXDEFAUT = 2;

	private int profMax = PROFMAXDEFAUT;
	private Heuristique h;
	private String playerMin;
	private String playerMax;
	
	private int nbnoeuds;
	private int nbfeuilles;

	public AlphaBeta(AdvancedHeuristique h, String playerMax, String playerMin) {
	    this(h,playerMax,playerMin,PROFMAXDEFAUT);
	}

    public AlphaBeta(Heuristique h, String playerMax, String playerMin, int profMaxi) {
        this.h = h;
        this.playerMin = playerMin;
        this.playerMax = playerMax;
        this.profMax = profMaxi;
    }
    
    public String toString() {
        return "AlphaBeta(ProfMax="+profMax+")";
    }
    
    public void setProfMax(int profMax) {
		this.profMax = profMax;
    }

    public String meilleurCoup(EscampeBoard board) {
	   this.nbfeuilles = 0;
	   this.nbnoeuds = 0;
	   String coupaJouer= null;
	   int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
	   int previousAlpha = alpha;
	   String[] coups = board.possiblesMoves(this.playerMax);
	   if(coups.length == 0 ) return "E";
	   for (String coupPossible : coups) {
		   EscampeBoard boardCopy = board.clone();
		   boardCopy.play(coupPossible, this.playerMax);
		   
		   int alphaCourant = MinValue(boardCopy, alpha, beta, 1);
		   if(alphaCourant >= previousAlpha) {
			   coupaJouer = coupPossible;
			   previousAlpha = alphaCourant;
		   }
		}
	   System.out.println("\n\nheuristique du noeud choisi : " + previousAlpha );
	   System.out.println("NB de noeuds :" + this.nbnoeuds); 
	   System.out.println("NB de feuilles :" + this.nbfeuilles);
	   return coupaJouer;
    }
    
    int MaxValue(EscampeBoard board, int alpha, int beta, int level) {
    	
    	if(board.gameOver()) {
    		this.nbfeuilles++;
    		return this.h.eval(board, this.playerMax,level);
    	}
    	if(level == this.profMax) {
    		this.nbnoeuds++;
    		return this.h.eval(board, this.playerMax,level);
    	}
    	this.nbnoeuds++;
    	String[] coups = board.possiblesMoves(this.playerMax);
    	if(coups.length == 0) {
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.pass();
    		alpha = Math.max(alpha, MinValue(boardCopy, alpha, beta, level+1));
    		if(alpha> beta) {
    			return beta;
    		}
    	}
    	int v = Integer.MIN_VALUE;
    	for (String coupPossible : coups) {
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.play(coupPossible, this.playerMax);
    		v = Math.max(alpha, MinValue(boardCopy, alpha, beta, level+1));
    		if(v> beta) {
    			return v;
    		}
    		alpha = Math.max(alpha, v);
		}
    	return alpha;
    }
    
    int MinValue(EscampeBoard board, int alpha, int beta, int level) {
    	if(board.gameOver()) {
    		this.nbfeuilles++;
    		return this.h.eval(board, this.playerMax,level);
    	}
    	if(level == this.profMax) {
    		this.nbnoeuds++;
    		return this.h.eval(board, this.playerMax,level);
    	}
    	this.nbnoeuds++;
    	String[] coups = board.possiblesMoves(this.playerMin);
    	if(coups.length == 0) {
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.pass();
    		beta = Math.min(beta, MaxValue(boardCopy, alpha, beta, level+1));
    		if(alpha> beta) {
    			return alpha;
    		}
    	}
    	int v = Integer.MAX_VALUE;
    	for (String coupPossible : coups) {
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.play(coupPossible, this.playerMin);
    		v = Math.min(beta, MaxValue(boardCopy, alpha, beta, level+1));
    		if(alpha> v) {
    			return v;
    		}
    		beta = Math.min(beta, v);
		}
    	return beta;
    }
}
