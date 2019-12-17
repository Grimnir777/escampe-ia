package algos;

import game.EscampeBoard;

public interface Heuristique {
	public int eval(EscampeBoard board, String player,int level);
}
