package projetPOO;


import java.io.Serializable;

public class BlocFixe extends Bloc implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6408805633449971225L;
	String materiel;
	
	public BlocFixe(String matos) {
		this.materiel = matos;
	}
	
	public String toString() {
		return "P";
	}
	
	public String getMateriel() {
		return this.materiel;
	}
}
