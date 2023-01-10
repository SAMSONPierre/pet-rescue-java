package projetPOO;


public class Jeu {
	
	Joueur joueur;
	Plateau plateau;
	
	Jeu(Joueur joueur,Plateau plateau){
		this.joueur = joueur;
		this.plateau = plateau;
	}
	
	public void jouer() {
		while(!plateau.jeuGagne() && !plateau.jeuPerdu()) {
			plateau.afficher();
			if(plateau.getFusee()) {
				System.out.println("Vous pouvez utiliser une fusée");
			}
			else if(!plateau.getFusee()) {
				System.out.println("Il vous reste " + (20 - plateau.getPointFusee()) + " blocs à détruire avant d'avoir une fusée");
			}
			char reponse = joueur.demanderAction();
			if(reponse == 'd') {
				int[] coord = new int[2];
				coord[0] = joueur.demanderX("Quelle est la ligne du bloc que vous voulez détruire ?");
				coord[1] = joueur.demanderX("Quelle est la colonne du bloc que vous voulez détruire ?");
				plateau.jouerTour(coord[0], coord[1]);
			}
			if(reponse == 'f') {
				if(plateau.getFusee()) {
					int colonne = joueur.demanderX("Quelle est la colonne de la ligne que vous voulez détruire ?");
					plateau.jouerFusee(colonne);
				}
				else {
					System.out.println("Vous n'avez pas encore de fusée");
				}
				
			}
		}
		if (plateau.jeuGagne()) {
            System.out.println("Vous avez gagné !");
            System.out.println(plateau.getScore());
            Vue.setNiveauCourant(Vue.getNiveauCourant()+1);
            joueur.demandeJouer();
        }

        if (plateau.jeuPerdu()) {
            System.out.println("Vous avez perdu !");
            joueur.demandeJouer();
        }
	}
	
	public void jouerRobot() { // Le joueur robot joue systématiquent le "meilleur coup" (= celui qui détruit le plus de blocs possibles).
		while(!plateau.jeuGagne() && !plateau.jeuPerdu()) {
			plateau.afficher();
			int[] coup = plateau.meilleurCoup();
			plateau.resetVisited();
			System.out.println(coup[0]+" " +coup[1]);
			plateau.jouerTour(coup[0], coup[1]);
		}
		if (plateau.jeuGagne()) {
			plateau.afficher();
            System.out.println("Vous avez gagné !");
            Vue.setNiveauCourant(Vue.getNiveauCourant()+1);
            joueur.demandeJouer();
        }

        if (plateau.jeuPerdu()) {
        	plateau.afficher();
            System.out.println("Vous avez perdu !");
            joueur.demandeJouer();
        }
	}

}
