package game;

public class Square implements Cloneable {
	byte value;
	
	/* Réprésentation sous forme d'octet
	 * 
	 * case simple : XX100XXX
	 * case double : XX010XXX
	 * case triple : XX001XXX
	 * 
	 * Type de pièce
	 * aucune      : XXXXX00X
	 * paladin 	   : XXXXX10X
	 * licorne 	   : XXXXX01X
	 * 
	 * Pièce alliée ou ennemie ? 
	 * blanc (alliée) : XXXXXXX0
	 * noir (ennemie) : XXXXXXX1
	 * 
	 * */
	
	public Square() {}
	
	public Square(int lis) {
		this.setLisere(lis);
	}
	

	@Override
	public Square clone() throws CloneNotSupportedException{
		return (Square) super.clone();
	}
	
	public int lisere() {
		switch (this.value >> 3) {
			case (byte) 1:
				return 3;
			case (byte) 2:
				return 2;
			case (byte) 4:
				return 1;
			default:
				throw new Error("Invalid value of square");
		}
	}
	
	public void setLisere(int lis) {
		switch (lis) {
			case 1:
				this.value = (byte) 32; // 00100000
				break;
			case 2:
				this.value = (byte) 16; // 00010000
				break;
			case 3:
				this.value = (byte) 8; // 00001000
				break;
			default:
				throw new Error("Invalid value of lis");
		}
	}
	
	public String type() {
		int color = this.value & (byte) 1;
		int type = (this.value & (byte) 6) >> 1;
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
	
	public void resetSquare() {
		this.value &= (byte) (32 + 16 + 8);
	}
	
	public void setSquare(String player,SquareType type) { // 1 licorne 2 paladin
		this.resetSquare();
		this.value |= ((byte) type.getValue()) <<1;
		if(player.equals("noir")){
			this.value |= (byte) 1;
		}
		else if (!player.equals("blanc")) {
			throw new Error("wrong argument : player");
		}
	}
}
