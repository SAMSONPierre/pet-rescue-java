package projetPOO;

import java.util.Scanner;

public class Joueur {
	
	private Scanner scanReponse;
	private boolean veutJouer = true;
	
	Joueur(){
		this.scanReponse = new Scanner(System.in);
		this.veutJouer = true;
	}
	public boolean getVeutJouer() {
		return this.veutJouer;
	}
	
	public void demandeJouer() {
		String reponse = demanderStr("Voulez vous continuer de jouer ? (oui/non)");
		if(reponse.equals("non")) {
			veutJouer = false;
		}
		return;
	}
	public void finish() {
		this.scanReponse.close();
	}
	
	public String demanderStr(String question) {
        System.out.print(question + " ");
        return scanReponse.next();
    }
	
	public char demanderAction() {
        return demanderStr("Voulez-vous detruire une case (d) ou jouer une fusee (f) ?").charAt(0);
    }
	
	public int demanderX(String question) {
		System.out.print(question + " ");
		return scanReponse.nextInt();
	}
}
