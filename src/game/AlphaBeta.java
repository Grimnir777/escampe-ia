package game;

public class AlphaBeta {
	private final static int PROFMAXDEFAUT = 4;

	private int profMax = PROFMAXDEFAUT;
	private Heuristique h;
	private String playerMin;
	private String playerMax;
	
	private int nbnoeuds;
	private int nbfeuilles;

	public AlphaBeta(Heuristique h, String playerMax, String playerMin) {
	    this(h,playerMax,playerMin,PROFMAXDEFAUT);
	}

    public AlphaBeta(Heuristique h, String playerMax, String playerMin, int profMaxi) {
        this.h = h;
        this.playerMin = playerMin;
        this.playerMax = playerMax;
        this.profMax = profMaxi;
    }
    
   public String meilleurCoup(EscampeBoard board) {
	   this.nbfeuilles = 0;
	   this.nbnoeuds = 0;
	   String coupaJouer= null;
	   int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
	   int previousAlpha = alpha;
	   for (String coupPossible : board.possiblesMoves(this.playerMax)) {
		   System.out.println("play :" + coupPossible);
		   EscampeBoard boardCopy = board.clone();
		   
		   boardCopy.play(coupPossible, this.playerMax);
		   
		   System.out.println("before");
		   System.out.println(board.toString());
		   System.out.println("after");
		   System.out.println(boardCopy.toString());
		   
		   
		   int alphaCourant = MaxValue(boardCopy, alpha, beta, 1);
		   if(alphaCourant >= previousAlpha) {
			   coupaJouer = coupPossible;
			   previousAlpha = alphaCourant;
		   }
		}
	   System.out.println("heuristique du noeud choisi : " + previousAlpha );
	   System.out.println("NB de noeuds :" + this.nbnoeuds); 
	   System.out.println("NB de feuilles :" + this.nbfeuilles);
	   return coupaJouer;
    }

    public String toString() {
        return "AlphaBeta(ProfMax="+profMax+")";
    }

    
    int MaxValue(EscampeBoard board, int alpha, int beta, int level) {
    	String[] coups = board.possiblesMoves(this.playerMax);
    	if(board.gameOver()) {
    		this.nbfeuilles++;
    		return this.h.eval(board, this.playerMax, board.getPreviousLisere());
    	}
    	if(level == this.profMax) {
    		this.nbnoeuds++;
    		return this.h.eval(board, this.playerMax, board.getPreviousLisere());
    	}
    	this.nbnoeuds++;
    	
    	for (String coupPossible : coups) {
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.play(coupPossible, this.playerMax);
    		alpha = Math.max(alpha, MinValue(boardCopy, alpha, beta, level+1));
    		if(alpha>= beta) {
    			return beta;
    		}
		}
    	return alpha;
    }
    
    int MinValue(EscampeBoard board, int alpha, int beta, int level) {
    	String[] coups = board.possiblesMoves(this.playerMin);
    	if(board.gameOver()) {
    		this.nbfeuilles++;
    		return this.h.eval(board, this.playerMin,board.getPreviousLisere());
    	}
    	if(level == this.profMax) {
    		this.nbnoeuds++;
    		return this.h.eval(board, this.playerMin,board.getPreviousLisere());
    	}
    	this.nbnoeuds++;
    	
    	for (String coupPossible : coups) {
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.play(coupPossible, this.playerMin);
    		beta = Math.min(beta, MaxValue(boardCopy, alpha, beta, level+1));
    		if(alpha>= beta) {
    			return alpha;
    		}
		}
    	return beta;
    }
}
