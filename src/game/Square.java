package game;

public class Square {
	char value;
	/* Réprésentation sous forme d'octet
	 * 
	 * case simple : 100XXXXX
	 * case double : 010XXXXX
	 * case triple : 001XXXXX
	 * 
	 * Type de pièce
	 * aucune      : XXX00XXX
	 * paladin 	   : XXX10XXX
	 * licorne 	   : XXX01XXX
	 * 
	 * Pièce alliée ou ennemie ?
	 * alliée 	   : XXXXX0XX
	 * ennemie     : XXXXX1XX
	 * 
	 * */
	
	public int lisere() {
		switch (this.value >> 5) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 4:
				return 3;
			default:
				throw new Error("Invalid value of square");
		}
	}
	
	public String type() {
		int color = (this.value << 5) >> 7;
		int type = (this.value << 3) >> 6;
		if(color == 0) { // allié
			switch (type) {
				case 0:
					return "-";
				case 1:
					return "B";
				case 2:
					return "b";
				default:
					throw new Error("Invalid value of square");
			}
		}
		else {
			switch (type) {
			case 0:
				return "-";
			case 1:
				return "N";
			case 2:
				return "n";
			default:
				throw new Error("Invalid value of square");
		}
		}
		
	}
	
}
