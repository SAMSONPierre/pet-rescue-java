package projetPOO;


import java.io.Serializable;

// Classe du Plateau où il y a toutes les methodes servant a modifier le plateau.
public class Plateau implements Serializable{
	
	private static final long serialVersionUID = -6603685157036025816L;
	private int AnimauxRestants;
	private Bloc[][]tableau;
	private boolean[][]visited;
	private int score;
	private boolean fusee;
	private int pointPourFusee;
	private boolean vaJouerFusee;
	
	public Plateau(int hauteur,int largeur) {
		this.tableau = new Bloc[hauteur][largeur];
		this.visited = new boolean[hauteur][largeur];
	}
	
	public void afficher() {
		
		System.out.print("*************************\r\n" + 
						 "*    Pet Rescue Saga    *\r\n" +
						 "*       "+"Niveau:"+Vue.getNiveauCourant()+"        *\r\n" + 
						 "*   Animaux restants    *\r\n" + 
						 "*          "+AnimauxRestants+"            *\r\n" + 
						 "*        Score          *\r\n"+
						 "*          "+ score +"            *\r\n"+
						 "*************************\n");
		System.out.print("   ");
		for(int i = 1;i<tableau.length-1;i++) {
			System.out.print(" "+i+" ");
		}
		System.out.println();
		for(int i = 1;i<tableau.length-1;i++) {
			System.out.print(" " + i + " ");
			for(int j = 1;j<tableau[i].length-1;j++) {
				
				if(tableau[i][j] == null) {
					System.out.print(" X ");
				}
				else {
					System.out.print(" "+tableau[i][j]+ " ");
				}
			}
			System.out.println();
		}
	}
	
	public int compterCouleur(int x, int y) {
		int res = 1;
		visited[x][y] = true;
		if(tableau[x][y] == null || tableau[x][y] instanceof BlocAnimal || tableau[x][y] instanceof BlocFixe) {
			return -1;
		}
		else {
			if( tableau[x-1][y] != null && tableau[x-1][y] instanceof BlocCouleur && visited[x-1][y] == false && ((BlocCouleur)tableau[x][y]).getColor().equals(((BlocCouleur)tableau[x-1][y]).getColor())) {
				res += compterCouleur(x-1,y);
			}
			if( tableau[x][y-1] != null && tableau[x][y-1] instanceof BlocCouleur && visited[x][y-1] == false && ((BlocCouleur)tableau[x][y]).getColor().equals(((BlocCouleur)tableau[x][y-1]).getColor())) {
				res += compterCouleur(x,y-1);
			}
			if( tableau[x+1][y] != null && tableau[x+1][y] instanceof BlocCouleur && visited[x+1][y] == false && ((BlocCouleur)tableau[x][y]).getColor().equals(((BlocCouleur)tableau[x+1][y]).getColor())) {
				res += compterCouleur(x+1,y);
			}
			if( tableau[x][y+1] != null && tableau[x][y+1] instanceof BlocCouleur && visited[x][y+1] == false  && ((BlocCouleur)tableau[x][y]).getColor().equals(((BlocCouleur)tableau[x][y+1]).getColor())) {
				res += compterCouleur(x,y+1);
			}
		}
		
		return res;
	}
	
	public int[] meilleurCoup() {
		int[] res = new int[2];
		res[0] = 1;
		res[1] = 1;
		for(int i = 1;i<tableau.length -1;i++) {
			for(int j= 1; j<tableau[i].length-1;j++) {
				int res1 = compterCouleur(res[0],res[1]);
				resetVisited();
				int res2 = compterCouleur(i, j);
				if(res1<res2) {
					res[0] = i;
					res[1] = j;
				}
			}
		}
		return res;		
	}
	
	public void detruireBloc(int x,int y) { // Pour l'instant on va partir du principe que ce que veut detruire le joueur est "detruisable"
		// "detruisable" = au moins 2 blocs de la même couleur a côte , pas un animal pas un bloc vide
		visited[x][y] = true;
		String c = ((BlocCouleur)tableau[x][y]).getColor();
		tableau[x][y] = null;
		
		if( tableau[x-1][y] != null && tableau[x-1][y] instanceof BlocCouleur && visited[x-1][y] == false && c.equals(((BlocCouleur)tableau[x-1][y]).getColor())) {
				detruireBloc(x-1,y);
		}
		if( tableau[x][y-1] != null && tableau[x][y-1] instanceof BlocCouleur && visited[x][y-1] == false && c.equals(((BlocCouleur)tableau[x][y-1]).getColor())) {
				detruireBloc(x,y-1);
		}
		if( tableau[x+1][y] != null && tableau[x+1][y] instanceof BlocCouleur && visited[x+1][y] == false && c.equals(((BlocCouleur)tableau[x+1][y]).getColor())) {
				detruireBloc(x+1,y);
		}
		if( tableau[x][y+1] != null && tableau[x][y+1] instanceof BlocCouleur && visited[x][y+1] == false  && c.equals(((BlocCouleur)tableau[x][y+1]).getColor())) {
				detruireBloc(x,y+1);
		}
	}
	
	public boolean plusGrand(int colonne,int i,int j) {
		if(tableau[i][colonne] != null && tableau[j][colonne] == null) {
			return true;
		}
		return false;
	}
	
	public void graviteC(int colonne) {
		// tab[colonne][i] a trier avec null en haut et bloc en bas
		
		boolean change = false;
		do {
			change = false;
			for (int i=1; i< tableau.length - 2; i++) {
				if(tableau[i][colonne] instanceof BlocFixe) {
					break;
				}
				if (plusGrand(colonne,i,i+1)) { 
					Bloc tmp = tableau[i+1][colonne];
					tableau[i+1][colonne] = tableau[i][colonne];
					tableau[i][colonne] = tmp;
					change = true;
				}
			}
		} 
		while (change);
		
	}
	
	public void graviteAll() {
		for(int i = 1;i<tableau[0].length-1;i++) {
			graviteC(i);
			if(isEmptyC(i)) {
				decaleC(i+1);
			}
			graviteC(i);
		}
	}
	
	public boolean isEmptyC(int colonne) {
		for(int i = 1;i<tableau.length - 1;i++) {
			if((tableau[i][colonne] != null) && !(tableau[i][colonne] instanceof BlocFixe)) {
				return false;
			}
		}
		return true;
	}
	
	public void decaleC(int colonne) {
		for(int i=1;i<tableau.length -1;i++) {
			if(!(tableau[i][colonne] instanceof BlocFixe)) {
				if(tableau[i][colonne-1] instanceof BlocFixe) {
					i++;
				}
				else if(tableau[i][colonne] instanceof BlocCouleur || tableau[i][colonne] instanceof BlocAnimal) {
				tableau[i][colonne-1] = tableau[i][colonne];
				tableau[i][colonne] = null;
				}
			}
		}
	}
	
	public void resetVisited() {
		for(int i = 0;i<visited.length;i++) {
			for(int j = 0;j<visited[i].length;j++) {
				this.visited[i][j] = false;
			}
		}
	}
	
	public boolean coupPossible() {
		for(int i = 1; i<tableau.length - 1;i ++) {
			for(int j = 1;j<tableau[i].length - 1;j++) {
				if(compterCouleur(i,j) >= 2) {
					resetVisited();
					return true;
				}
			}
		}
		resetVisited();
		return false;
	}
	
	public void checkAnimal() { // On check le "plancher" du niveau pour voir si il n'y a pas des animaux. Si oui : on les "sauve" si non : rien
		int sol = tableau.length-2;
		for(int i = 1;i<tableau[0].length-1;i++) {
			if(tableau[sol][i] instanceof BlocAnimal) {
				System.out.println("Un animal a ete sauve");
				tableau[sol][i] = null;
				AnimauxRestants -= 1;
				score += 1000;
			}
		}
		graviteAll();
	}
	
	public void jouerTour(int x,int y) {
		if(vaJouerFusee) {
			vaJouerFusee = false;
			jouerFusee(y);
		}
		else {
			if(tableau[x][y] == null) {
				System.out.println("On ne peut pas detruire du vide");
			}
			else if(tableau[x][y] instanceof BlocAnimal) {
				System.out.println("On ne peut pas detruire un animal, vous devez les sauver !");
			}
			else if(tableau[x][y] instanceof BlocFixe) {
				System.out.println("On ne peut pas detruire un bloc fixe.");
			}
			else if(compterCouleur(x, y) <2){
				System.out.println("Il faut au moins 2 blocs pour les detruire");
				resetVisited();
			}
			else {
				resetVisited();
				pointPourFusee += compterCouleur(x,y);
				if(pointPourFusee >= 20) {
					fusee = true;
				}
				resetVisited();
				this.score += Math.pow(compterCouleur(x,y),2)*10;
				resetVisited();
				detruireBloc(x, y);
				graviteAll();
				checkAnimal();
				graviteAll();
				checkAnimal();
				resetVisited();
			}
		}
		
	}
	
	public void jouerFusee(int colonne) {
		if(fusee) {
			for(int i = 1;i<tableau.length;i++) {
				if(tableau[i][colonne] instanceof BlocCouleur) {
					tableau[i][colonne] = null;
					fusee = false;
					pointPourFusee = 0;
					resetVisited();
					graviteAll();
					checkAnimal();
					graviteAll();
					resetVisited();
				}
			}
		}
		else {
			System.out.println("Vous n'avez pas encore de fusee.");
		}
	}
	
	public boolean getFusee() {
		return fusee;
	}
	
	public int getPointFusee() {
		return pointPourFusee;
	}
	
	public boolean jeuGagne() { // Le jeu est gagne quand tous les animaux sont sauves.
		for(int i = 0;i<tableau.length;i++) {
			for(int j = 0;j<tableau[i].length;j++) {
				if(tableau[i][j] instanceof BlocAnimal) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean jeuPerdu() { // Le jeu est perdu quand il n'y a plus de coup possible et que tous les animaux ne sont pas sauves.
		if(AnimauxRestants != 0 && this.coupPossible() == false && this.fusee == false) {
			return true;
		}
		return false;
	}
	
	public Bloc[][] getTableau(){
		return this.tableau;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void setVaJouerFusee(boolean b) {
		this.vaJouerFusee = b;
	}
	
	public void setCase(int x,int y,Bloc b) {
		tableau[x][y] = b;
	}
}