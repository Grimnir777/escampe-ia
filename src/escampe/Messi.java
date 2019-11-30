package escampe;

import game.EscampeBoard;

public class Messi implements IJoueur {
	  // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;
    
    private int myColor;
    private EscampeBoard board;
    private boolean first;

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
    		
    	}
    	else {
    		if(this.myColor == -1) { //si noir on joue direct
    			//TODO Choix du premier coup pour le blanc
    			board.play("C6/A6/B5/D5/E6/F5", "noir");
    		}
    		else {
    			//TODO analyse du placement du noir
    			board.play("C1/A1/B1/D2/E2/F2", "blanc");
    		}
    	}
    	return "";
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
    		System.out.println("I'm the boss bro");
    	}
    	else {
    		System.out.println("Good game bro");
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
    	if(this.myColor == 1) {
    		this.board.play(coup, "blanc");
    	}
    	else {
    		this.board.play(coup, "noir");
    	}
    }

    public String binoName() {
    	return "Messi";
    }
}
