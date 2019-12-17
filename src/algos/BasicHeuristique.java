package algos;

import game.EscampeBoard;

public class BasicHeuristique implements Heuristique {
	String player;
	public int eval(EscampeBoard board, String player, int level) {
		this.player = player;

		if(this.player.equals("blanc")) {
			if(board.getLicorneBState() == false) {
				return Integer.MIN_VALUE/level;
			}
			if(board.getLicorneNState() == false) {
				return Integer.MAX_VALUE/level;
			}
		}
		else {
			if(board.getLicorneNState() == false) {
				return Integer.MIN_VALUE/level;
			}
			if(board.getLicorneBState() == false) {
				return Integer.MAX_VALUE/level;
			}
		}
		return 0;
	}
}
