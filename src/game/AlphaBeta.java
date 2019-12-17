package game;

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
    
   public String meilleurCoup(EscampeBoard board) {
	   this.nbfeuilles = 0;
	   this.nbnoeuds = 0;
	   String coupaJouer= null;
	   int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
	   int previousAlpha = alpha;
	   for (String coupPossible : board.possiblesMoves(this.playerMax)) {
		   System.out.print("\nplay :" + coupPossible);
		   
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
    	System.out.print("\n");
    	for (int i = 0; i < level; i++) {
			System.out.print("--");
		}
    	String[] coups = board.possiblesMoves(this.playerMax);
    	if(board.gameOver()) {
    		System.out.print(" end of game / H :: " + this.h.eval(board, this.playerMax, board.getPreviousLisere()));
    		this.nbfeuilles++;
    		return this.h.eval(board, this.playerMax);
    	}
    	if(level == this.profMax) {
    		System.out.print(" Level max reached / H :: " + this.h.eval(board, this.playerMax, board.getPreviousLisere()));
    		this.nbnoeuds++;
    		return this.h.eval(board, this.playerMax);
    	}
    	this.nbnoeuds++;
    	if(coups.length == 0) {
    		System.out.print(" play : Passe son tour");
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.pass();
    		alpha = Math.max(alpha, MinValue(boardCopy, alpha, beta, level+1));
    		if(alpha> beta) {
    			return beta;
    		}
    	}
    	
    	for (String coupPossible : coups) {
    		System.out.print("\n");
        	for (int i = 0; i < level; i++) {
    			System.out.print("--");
    		}
    		System.out.print("play : "+ coupPossible);
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.play(coupPossible, this.playerMax);
    		alpha = Math.max(alpha, MinValue(boardCopy, alpha, beta, level+1));
    		if(alpha> beta) {
    			return beta;
    		}
		}
    	return alpha;
    }
    
    int MinValue(EscampeBoard board, int alpha, int beta, int level) {
    	System.out.print("\n");
    	for (int i = 0; i < level; i++) {
			System.out.print("--");
		}
    	
    	String[] coups = board.possiblesMoves(this.playerMin);
    	if(board.gameOver()) {
    		System.out.print(" end of game / H :: " + this.h.eval(board, this.playerMax, board.getPreviousLisere()));
    		this.nbfeuilles++;
    		return this.h.eval(board, this.playerMax);
    	}
    	if(level == this.profMax) {
    		System.out.print(" Level max reached / H :: " + this.h.eval(board, this.playerMax, board.getPreviousLisere()));
    		this.nbnoeuds++;
    		return this.h.eval(board, this.playerMax);
    	}
    	this.nbnoeuds++;
    	if(coups.length == 0) {
    		System.out.print(" play : Passe son tour");
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.pass();
    		beta = Math.min(beta, MaxValue(boardCopy, alpha, beta, level+1));
    		if(alpha> beta) {
    			return alpha;
    		}
    	}
    	
    	for (String coupPossible : coups) {
    		System.out.print("\n");
        	for (int i = 0; i < level; i++) {
    			System.out.print("--");
    		}
    		System.out.print("play : "+ coupPossible);
    		EscampeBoard boardCopy = board.clone();
    		boardCopy.play(coupPossible, this.playerMin);
    		beta = Math.min(beta, MaxValue(boardCopy, alpha, beta, level+1));
    		if(alpha> beta) {
    			return alpha;
    		}
		}
    	return beta;
    }
}
