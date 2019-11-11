package game;

public class EscampeBoard implements Partie1{
	
	private int [][] liseres = new int[][] {{1,2,2,3,1,2},
											{3,1,3,1,3,2},
											{2,3,1,2,1,3},
											{2,1,3,2,3,1},
											{1,3,1,3,1,2},
											{3,2,2,1,3,2}};
	
	
	/** initialise un plateau à partir d'un fichier texte
	* @param fileName le nom du fichier à lire
	*/
	public void setFromFile(String fileName) {
		
	}
	
	
	/** sauve la configuration de l'état courant (plateau et pièces restantes) dans un fichier
	* @param fileName le nom du fichier à sauvegarder
	* Le format doit être compatible avec celui utilisé pour la lecture.
	*/
	public void saveToFile(String fileName) {
		
	}
	
	
	/** indique si le coup <move> est valide pour le joueur <player> sur le plateau courant
	* @param move le coup à jouer,
	* sous la forme "B1-D1" en général,
	* sous la forme "C6/A6/B5/D5/E6/F5" pour le coup qui place les pièces
	* @param player le joueur qui joue, représenté par "noir" ou "blanc"
	*/
	public boolean isValidMove(String move, String player) {
		return true;
	}
	
	
	/** calcule les coups possibles pour le joueur <player> sur le plateau courant
	* @param player le joueur qui joue, représenté par "noir" ou "blanc"
	*/
	public String[] possiblesMoves(String player) {
		return new String[1];
	}
	
	
	/** modifie le plateau en jouant le coup move avec la piece choose
	* @param move le coup à jouer, sous la forme "C1-D1" ou "C6/A6/B5/D5/E6/F5"
	* @param player le joueur qui joue, représenté par "noir" ou "blanc"
	*/
	public void play(String move, String player) {
		
	}
	
	
	/** vrai lorsque le plateau corespond à une fin de partie
	*/
	public boolean gameOver() {
		return true;
	}
	
	
	public static void main(String[] args) {
		//test
		Square s = new Square(1);
		System.out.println(s.lisere());
		
		Square s2 = new Square(2);
		System.out.println(s2.lisere());
	
		Square s3 = new Square(3);
		System.out.println(s3.lisere());
		System.out.println(s3.type()); // expect Empty -
		
		
		s3.setSquare("noir", 1); // expect Licorne noire N
		System.out.println(s3.type());
		
		s3.setSquare("noir", 2); // expect paladin noire n
		System.out.println(s3.type());
		
		
		s3.setSquare("blanc", 1); // expect Licorne blanche B
		System.out.println(s3.type());
		
	
		s3.setSquare("blanc", 2); // expect Paladin blanc b
		System.out.println(s3.type());
		
	}
}
