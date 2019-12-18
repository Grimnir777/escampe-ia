package escampe;

import algos.AdvancedHeuristique;
import algos.AlphaBeta;
import algos.BasicHeuristique;
import game.EscampeBoard;

public class Ronaldo implements IJoueur {
	  // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;
    
    private int myColor;
    private EscampeBoard board;
    private boolean first;
    private AlphaBeta alphaBeta;

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
    		this.alphaBeta = new AlphaBeta(new AdvancedHeuristique() , "blanc", "noir",6);
    	}
    	else {
    		this.alphaBeta = new AlphaBeta(new AdvancedHeuristique() , "noir", "blanc",6);
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
    	if(!this.first) {
    		//Coup classique
    		String mc = this.alphaBeta.meilleurCoup(this.board);
    		System.out.println("meilleur coup " + mc);
    		if(this.myColor == BLANC) {
    			this.board.play(mc, "blanc");
    		}
    		else {
    			this.board.play(mc, "noir");
    		}
    		return mc;
    	}
    	else {
    		this.first=false;
    		if(this.myColor == 1) { //si noir on joue direct
    			board.play("C6/A5/B5/D5/E5/F6", "noir");
    			return "C6/A5/B5/D5/E5/F6";
    		}
    		else {
    			if(this.board.getFirstChooseDown()) {
    				board.play("D1/F2/A2/C2/E1/B2", "blanc");
        			return "D1/F2/A2/C2/E1/B2";
    			}
    			else {
    				board.play("C6/A5/B5/D5/E5/F6", "blanc");
        			return "C6/A5/B5/D5/E5/F6";
    			}
    		}
    	}
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
    	if(this.myColor == NOIR) {
    		this.board.play(coup, "blanc");
    	}
    	else {
    		this.board.play(coup, "noir");
    	}
    	System.out.println("state");
    	System.out.println(this.board);
    }

    public String binoName() {
    	return "Ronaldo";
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
