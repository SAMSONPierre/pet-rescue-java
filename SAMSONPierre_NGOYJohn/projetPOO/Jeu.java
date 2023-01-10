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
				System.out.println("Vous pouvez utiliser une fus�e");
			}
			else if(!plateau.getFusee()) {
				System.out.println("Il vous reste " + (20 - plateau.getPointFusee()) + " blocs � d�truire avant d'avoir une fus�e");
			}
			char reponse = joueur.demanderAction();
			if(reponse == 'd') {
				int[] coord = new int[2];
				coord[0] = joueur.demanderX("Quelle est la ligne du bloc que vous voulez d�truire ?");
				coord[1] = joueur.demanderX("Quelle est la colonne du bloc que vous voulez d�truire ?");
				plateau.jouerTour(coord[0], coord[1]);
			}
			if(reponse == 'f') {
				if(plateau.getFusee()) {
					int colonne = joueur.demanderX("Quelle est la colonne de la ligne que vous voulez d�truire ?");
					plateau.jouerFusee(colonne);
				}
				else {
					System.out.println("Vous n'avez pas encore de fus�e");
				}
				
			}
		}
		if (plateau.jeuGagne()) {
            System.out.println("Vous avez gagn� !");
            System.out.println(plateau.getScore());
            Vue.setNiveauCourant(Vue.getNiveauCourant()+1);
            joueur.demandeJouer();
        }

        if (plateau.jeuPerdu()) {
            System.out.println("Vous avez perdu !");
            joueur.demandeJouer();
        }
	}
	
	public void jouerRobot() { // Le joueur robot joue syst�matiquent le "meilleur coup" (= celui qui d�truit le plus de blocs possibles).
		while(!plateau.jeuGagne() && !plateau.jeuPerdu()) {
			plateau.afficher();
			int[] coup = plateau.meilleurCoup();
			plateau.resetVisited();
			System.out.println(coup[0]+" " +coup[1]);
			plateau.jouerTour(coup[0], coup[1]);
		}
		if (plateau.jeuGagne()) {
			plateau.afficher();
            System.out.println("Vous avez gagn� !");
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
