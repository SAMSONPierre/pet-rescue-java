package projetPOO;


import java.io.Serializable;

public class BlocCouleur extends Bloc implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3077515607077275790L;
	private String couleur;
	
	public BlocCouleur(String couleur) {
		super();
		this.couleur = couleur;
	}
	
	public String toString() {
		if(couleur.equals("rouge")) {
			return "r";
		}
		else if(couleur.equals("bleu")) {
			return "b";
		}
		else if(couleur.equals("jaune")) {
			return "j";
		}
		else if(couleur.equals("violet")) {
			return "v";
		}
		else if(couleur.equals("vert")) {
			return "V";
		}
		return "";
	}
	
	public String getColor() {
		return this.couleur;
	}
	
	

}
