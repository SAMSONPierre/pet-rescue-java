package projetPOO;


import java.io.Serializable;

public class BlocAnimal extends Bloc implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6330028681337709L;
	String animal;
	
	public BlocAnimal(String animal) {
		this.animal = animal;
	}
	
	public String toString() {
		if(animal.equals("cochon")) {
			return "C";
		}
		return "";
	}
	
	public String getAnimal() {
		return this.animal;
	}
}
