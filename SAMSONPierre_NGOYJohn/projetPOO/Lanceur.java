package projetPOO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Lanceur {

	public static void main(String[]args) {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			
			e.printStackTrace();
		}		
			try {
				FileInputStream fis = new FileInputStream(System.getProperty("java.class.path")+"/projetPOO/niveau/niveau_"+Vue.getNiveauCourant()+".data");
				@SuppressWarnings("resource")
				ObjectInputStream ois = new ObjectInputStream(fis);
				Plateau p = (Plateau)ois.readObject();
				Vue view = new Vue(p);
				view.setVisible(true);
			} catch (FileNotFoundException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {	
			e.printStackTrace();
		}
	}
}