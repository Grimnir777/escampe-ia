package game;

public class BasicHeuristique implements Heuristique {
	String player;
	public int eval(EscampeBoard board, String player) {
		this.player = player;

		if(this.player.equals("blanc")) {
			if(board.getLicorneBState() == false) {
				return Integer.MIN_VALUE;
			}
			if(board.getLicorneNState() == false) {
				return Integer.MAX_VALUE;
			}
		}
		else {
			if(board.getLicorneNState() == false) {
				return Integer.MIN_VALUE;
			}
			if(board.getLicorneBState() == false) {
				return Integer.MAX_VALUE;
			}
		}
		return 0;
	}
}
