package escampe;

import algos.AlphaBeta;
import algos.OptimusHeuristique;
import game.EscampeBoard;

public class Messi implements IJoueur {
	  // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;
    
    private int myColor;
    private EscampeBoard board;
    private boolean first;
    private AlphaBeta alphaBeta;
    private String firstMove;
    private long currentTime;
    /**
     * L'arbitre vient de lancer votre joueur. Il lui informe par cette méthode que vous devez jouer
     * dans cette couleur. Vous pouvez utiliser cette m?thode abstraite, ou la méthode constructeur
     * de votre classe, pour initialiser vos structures.
     * 
     * @param mycolour
     *            La couleur dans laquelle vous allez jouer (-1=BLANC, 1=NOIR)
     */
    public void initJoueur(int mycolour) {
    	this.first = true;
    	this.myColor = mycolour;
    	this.board = new EscampeBoard();
    	if(this.myColor == BLANC) {
    		this.alphaBeta = new AlphaBeta(new OptimusHeuristique() , "blanc", "noir",6);
    	}
    	else {
    		this.alphaBeta = new AlphaBeta(new OptimusHeuristique() , "noir", "blanc",6);
    	}
    }

    // Doit retourner l'argument passé par la fonction ci-dessus (constantes BLANC ou NOIR)
    public int getNumJoueur() {
    	return this.myColor;
    }

    /**
     * C'est ici que vous devez faire appel à votre IA pour trouver le meilleur coup à jouer sur le
     * plateau courant.
     * 
     * @return une chaine décrivant le mouvement. Cette chaine doit être décrite exactement comme
     *         sur l'exemple : String msg = "" + positionInitiale + "-" +positionFinale + ""; ou "PASSE";
     *          Chaque position contient une lettre et un num?ro, par exemple:A1,B2 (coup "A1-B2")
     */
    public String choixMouvement() {
    	long beginTime = System.currentTimeMillis();
    	String move;
    	if(!this.first) {
    		//Coup classique
    		move = this.alphaBeta.meilleurCoup(this.board);
    		System.out.println("meilleur coup " + move);
    		if(this.myColor == BLANC) {
    			this.board.play(move, "blanc");
    		}
    		else {
    			this.board.play(move, "noir");
    		}
    	}
    	else {
    		this.first=false;
    		if(this.myColor == NOIR) { //si noir on joue direct
    			move= "B6/A6/C5/D5/E5/F6";
    			board.play(move, "noir");
    			
    		}
    		else {
    			String licorneAdverse = this.firstMove.substring(0,2);
    			System.out.println(this.board.getFirstChooseDown());
    			if(this.board.getFirstChooseDown()) { // adversaire en bas ligne 5 et 6
    				if(licorneAdverse.equals("A5") || licorneAdverse.equals("E5") || licorneAdverse.equals("A6") || licorneAdverse.equals("A6")) {
    					move="C1/A2/B2/D2/E2/F2";
    				}
    				else { //permet de gagner si licorne adverse est en C5
    					move="D1/F2/A2/C2/E1/B2";
    				}
    				
    			}
    			else { // adversaire en haut ligne 1 et 2
    				//Si licorne adverse est en B2 ou en D2 on peut l'avoir en un coup par la suite avec cette config
    				//Sinon elle est �quilibr�e
    				move="B6/A6/C5/D5/E5/F6";    				
    			}
    			board.play(move, "blanc");
    		}
    	}
    	long endTime = System.currentTimeMillis() - beginTime;
		System.out.println("Le calcul a pris : " + endTime + " ms");
		this.currentTime += endTime;
		System.out.println("Total time: " + this.currentTime + " ms" );
		if(this.currentTime/1000>200) {
			System.out.println("i've change prof");
			this.alphaBeta.setProfMax(4);
			if(this.currentTime/1000>300) {
				this.alphaBeta.setProfMax(2);
			}
		}
    	return move;    	
    }

    /**
     * Méthode appelée par l'arbitre pour désigner le vainqueur. Vous pouvez en profiter pour
     * imprimer une bannière de joie... Si vous gagnez...
     * 
     * @param colour
     *            La couleur du gagnant (BLANC=-1, NOIR=1).
     */
    public void declareLeVainqueur(int colour) {
    	if(colour == this.myColor) {
    		System.out.print(victory);
    	}
    	else {
    		System.out.print(defeat);
    	}
    	
    }

    /**
     * On suppose que l'arbitre a vérifié que le mouvement ennemi était bien légal. Il vous informe
     * du mouvement ennemi. A vous de répercuter ce mouvement dans vos structures. Comme par exemple
     * éliminer les pions que ennemi vient de vous prendre par ce mouvement. Il n'est pas nécessaire
     * de réfléchir déjà à votre prochain coup à jouer : pour cela l'arbitre appelera ensuite
     * choixMouvement().
     * 
     * @param coup
     * 			une chaine décrivant le mouvement:  par exemple: "A1-B2"
     */
    public void mouvementEnnemi(String coup) {
    	System.out.println("coup ennemi enregistr� : " + coup);
    	if(coup.equals("E")) {
    		this.board.pass();
    		return;
    	}
    	this.firstMove = coup;
    	if(this.myColor == NOIR) {
    		this.board.play(coup, "blanc");
    	}
    	else {
    		this.board.play(coup, "noir");
    	}
    }

    public String binoName() {
    	return "Messi";
    }
    
	String victory = 
	"                         ______                     \r\n" + 
	" _________        .---\"\"\"      \"\"\"---.              \r\n" + 
	":______.-':      :  .--------------.  :             \r\n" + 
	"| ______  |      | :                : |             \r\n" + 
	"|:______B:|      | |  Veni,         | |             \r\n" + 
	"|:______B:|      | |                | |             \r\n" + 
	"|:______B:|      | |  Vidi,         | |             \r\n" + 
	"|         |      | |                | |             \r\n" + 
	"|:_____:  |      | |  Vici          | |             \r\n" + 
	"|    ==   |      | :                : |             \r\n" + 
	"|       O |      :  '--------------'  :             \r\n" + 
	"|       o |      :'---...______...---'              \r\n" + 
	"|       o |-._.-i___/'             \\._              \r\n" + 
	"|'-.____o_|   '-.   '-...______...-'  `-._          \r\n" + 
	":_________:      `.____________________   `-.___.-. \r\n" + 
	"                 .'.eeeeeeeeeeeeeeeeee.'.      :___:\r\n" + 
	"    fsc        .'.eeeeeeeeeeeeeeeeeeeeee.'.         \r\n" + 
	"              :____________________________:";

	String defeat = 
	"                         ______                     \r\n" + 
	" _________        .---\"\"\"      \"\"\"---.              \r\n" + 
	":______.-':      :  .--------------.  :             \r\n" + 
	"| ______  |      | :                : |             \r\n" + 
	"|:______B:|      | | Ok             | |             \r\n" + 
	"|:______B:|      | |                | |             \r\n" + 
	"|:______B:|      | | fair enough    | |             \r\n" + 
	"|         |      | |                | |             \r\n" + 
	"|:_____:  |      | | You've won :'( | |             \r\n" + 
	"|    ==   |      | :                : |             \r\n" + 
	"|       O |      :  '--------------'  :             \r\n" + 
	"|       o |      :'---...______...---'              \r\n" + 
	"|       o |-._.-i___/'             \\._              \r\n" + 
	"|'-.____o_|   '-.   '-...______...-'  `-._          \r\n" + 
	":_________:      `.____________________   `-.___.-. \r\n" + 
	"                 .'.eeeeeeeeeeeeeeeeee.'.      :___:\r\n" + 
	"    fsc        .'.eeeeeeeeeeeeeeeeeeeeee.'.         \r\n" + 
	"              :____________________________:";
}
