package game;

public enum SquareType {
	licorne(1),
	paladin(2);
	
	private final int value;
	
	SquareType(int value){
		this.value = value;
	}
	public int getValue(){ return this.value; }
}
