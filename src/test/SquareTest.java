package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import game.SquareType;

class SquareTest {

	@Test
	void newSquareLisereTest() {
		game.Square square1 = new game.Square(1);
		assertEquals(square1.lisere(),1);
		game.Square square2 = new game.Square(2);
		assertEquals(square2.lisere(),2);
		game.Square square3 = new game.Square(3);
		assertEquals(square3.lisere(),3);
	}
	
	@Test
	void newSquareSetLisereTest() {
		game.Square square1 = new game.Square();
		square1.setLisere(1);
		assertEquals(square1.lisere(),1);
		game.Square square2 = new game.Square();
		square2.setLisere(2);
		assertEquals(square2.lisere(),2);
		game.Square square3 = new game.Square();
		square3.setLisere(3);
		assertEquals(square3.lisere(),3);
	}
	
	@Test
	void newSquareTypeTest() {
		game.Square square1 = new game.Square();
		assertEquals(square1.type(),"-");
	}
	
	
	@Test
	void setSquareTypeTest() {
		//1 pour licorne - 2 pour paladin
		game.Square square1 = new game.Square(1);
		square1.setSquare("blanc", SquareType.licorne);
		assertEquals(square1.type(),"B");
		square1.setSquare("blanc", SquareType.paladin);
		assertEquals(square1.type(),"b");
		square1.setSquare("noir", SquareType.licorne);
		assertEquals(square1.type(),"N");
		square1.setSquare("noir", SquareType.paladin);
		assertEquals(square1.type(),"n");
		square1.resetSquare();
		assertEquals(square1.type(),"-");
	}
	

}
