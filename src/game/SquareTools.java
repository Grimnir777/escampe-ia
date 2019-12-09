package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

public class SquareTools {
	
	private Map<Integer, Character> colHashMap  = new HashMap<Integer, Character>() {
		private static final long serialVersionUID = -3708238127665825076L;
	{
	    put(0,'A');
	    put(1,'B');
	    put(2,'C');
	    put(3,'D');
	    put(4,'E');
	    put(5,'F');
	}};
	
	private int getIndexOfCol(char charToFind) {
		int r = -1;
		for (Entry<Integer, Character> entry : this.colHashMap.entrySet()) {
	        if (Objects.equals(charToFind, entry.getValue())) {
	        	r = entry.getKey();
	        }
	    }
		return r;
	}
	
	public int getCol(String square) {
		return this.getIndexOfCol(square.charAt(0));
	}
	
	public int getLigne(String square) {
		return Integer.parseInt(square.substring(1,2)) - 1;
	}
	
	public String getStringValue(int col, int ligne) {
		return this.colHashMap.get(col) + Integer.toString(ligne+1);
	}
}
