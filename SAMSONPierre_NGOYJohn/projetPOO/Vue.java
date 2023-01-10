package projetPOO;


import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Classe qui gère l'interface graphique.
public class Vue extends JFrame{
	
	// Variables pour l'enchaînement des niveaux.
	private static int nbNiveau = 6;
	private boolean[] niveauFait = new boolean[nbNiveau];
	private static int niveauCourant = 1;
	
	// plateau et position x y du clic du joueur
	Plateau p;
	int[] posclick = new int[2];
	
	// Variables pour l'editeur de niveau.
	Plateau editeur;
	String fonction = "rouge";
	
	// Les images 
	Icon rouge = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/blocrouge2.png");
	Icon bleu = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/blocbleu.png");
	Icon jaune = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/blocjaune.png");
	Icon vert = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/blocvert.png");
	Icon violet = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/blocviolet.png");
	Icon cochon = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/cochon.png");
	Icon planche = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/planche.png");
	Icon contour = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/blocnoir.png");
	Icon fond = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/map.png");
	Icon fond2 = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/fond.png");
	Icon buttonIcon = new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/button.png");
	
	// Les boutons de la page d'accueil 
	CustomJButton button = new CustomJButton(buttonIcon,"Jouer");	
	CustomJButton button_1 = new CustomJButton(buttonIcon,"Informations");
	CustomJButton button_2 = new CustomJButton(buttonIcon,"Quitter");
	CustomJButton button_3 = new CustomJButton(buttonIcon,"Jouer sur la console");
	CustomJButton button_4 = new CustomJButton(buttonIcon,"Editeur de niveau");

	public Vue(Plateau p) {
		super("Pet Rescue Saga");
		this.p = p;
		
		setResizable(false);

		getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setSize(800,800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		clickDetection();
		setContentPane(fenetreMenu());
	}
	
	public class CustomJButton extends JButton{
		private static final long serialVersionUID = 5433756291343306660L;
		private int posx;
		private int posy;
		Icon i;
		private Image image;
		private String str;
		
		public CustomJButton(Icon icon,int x,int y) {
			//super(icon);
			this.i = icon;
			Image img = ((ImageIcon) icon).getImage() ; 
			this.image = img;
			this.posx = x;
			this.posy = y;
			str = null;
			
			
		}
		
		public CustomJButton(Icon icon,String str) {
			Image img = ((ImageIcon) icon).getImage();
			this.i = icon;
			this.image = img;
			this.str = str;
		}
		
		public int getPosX() {
			return posx;
		}
		public int getPosY() {
			return posy;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        if (image == null) return;
	        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	        if(str != null) {
		        Font fonte = new Font("Minecraft",Font.TRUETYPE_FONT,20);
		        g.setFont(fonte);
		        g.setColor(Color.white);
		        FontMetrics metric = g.getFontMetrics();
	            int width = metric.stringWidth(str);
	            g.drawString(str, (getWidth()-width)/2, (getHeight()+10)/2);
	        }
		}
			
		public void actionBouton() {
			this.setOpaque(false);
			this.setContentAreaFilled(true);
			this.setBorderPainted(false);	
			this.addActionListener(( event ) -> { posclick[1] = this.getPosX();  posclick[0] =  this.getPosY();jouerTour(posclick[1],posclick[0]); });						
		}
		
		public void editeur() {
			this.addActionListener((event) -> { posclick[1] = this.getPosX();  posclick[0] =  this.getPosY(); 
			if(fonction.equals("planche")) {
				editeur.getTableau()[posclick[1]][posclick[0]] = new BlocFixe(fonction);	
			}
			else if(fonction.equals("cochon")) {
				editeur.getTableau()[posclick[1]][posclick[0]] = new BlocAnimal(fonction);
			}
			else {
				editeur.getTableau()[posclick[1]][posclick[0]] = new BlocCouleur(fonction); 
			}
			setContentPane(fenetreEditeur());
			getContentPane().revalidate();
			getContentPane().repaint();	 });
		}
		

		
	}

	private static final long serialVersionUID = 2324315447302073311L;
	public JPanel fenetreMenu() {
		JPanel resF = new JPanel();
		
		getContentPane().add(resF);
		resF.setLayout(null);
		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/logo.png"));
		logo.setBounds(147, 30, 496, 114);
		
		button.setBounds(200, 200, 400, 55);
		button_3.setBounds(200,300,400,55);
		
		button_4.setBounds(200,400,400,55);
		
		button_1.setBounds(200, 500, 400, 55);
		
		button_2.setBounds(200, 600, 400, 55);

		resF.add(logo);
		resF.add(button);
		resF.add(button_1);
		resF.add(button_2);
		resF.add(button_3);
		resF.add(button_4);
		
		return resF;
		
	}

	
	public JPanel fenetreMap() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(fond);
		panel.add(label);
		label.setLayout(null);
		JButton button1 = new JButton("1");
		
		button1.addActionListener(( event ) -> { niveauCourant = 1;this.p = this.getNiveau();this.setContentPane(fenetreJeu()); getContentPane().revalidate();
		getContentPane().repaint(); });
		button1.setBounds(40,80,40,40);
		label.add(button1);
		
		
		JButton button2 = new JButton("2");
		button2.setBounds(140,310,40,40);
		button2.setEnabled(niveauFait[1]);
		button2.addActionListener(( event ) -> { niveauCourant = 2;this.p = this.getNiveau();this.setContentPane(fenetreJeu()); getContentPane().revalidate();
		getContentPane().repaint(); });
		label.add(button2);
		
		JButton button3 = new JButton("3");
		button3.setBounds(400,190,40,40);
		button3.setEnabled(niveauFait[2]);
		button3.addActionListener(( event ) -> { niveauCourant = 3;this.p = this.getNiveau();this.setContentPane(fenetreJeu()); getContentPane().revalidate();
		getContentPane().repaint(); });
		label.add(button3);
		
		JButton button4 = new JButton("4");
		button4.setBounds(620,350,40,40);
		button4.setEnabled(niveauFait[3]);
		button4.addActionListener(( event ) -> { niveauCourant = 4;this.p = this.getNiveau();this.setContentPane(fenetreJeu()); getContentPane().revalidate();
		getContentPane().repaint(); });
		label.add(button4);
		
		JButton button5 = new JButton("5");
		button5.setBounds(620,170,40,40);
		button5.setEnabled(niveauFait[4]);
		button5.addActionListener(( event ) -> { niveauCourant = 5;this.p = this.getNiveau();this.setContentPane(fenetreJeu()); getContentPane().revalidate();
		getContentPane().repaint(); });
		label.add(button5);
		
		JButton button6 = new JButton("6");
		button6.setBounds(750,320,40,40);
		button6.setEnabled(niveauFait[5]);
		button6.addActionListener(( event ) -> { niveauCourant = 6;this.p = this.getNiveau();this.setContentPane(fenetreJeu()); getContentPane().revalidate();
		getContentPane().repaint(); });
		label.add(button6);
		
		JButton custom = new JButton("Custom");
		custom.setBounds(260,700,100,50);
		custom.addActionListener(( event ) -> { this.p = this.getNiveauCustom(); this.setContentPane(fenetreJeu()); getContentPane().revalidate();
		getContentPane().repaint(); });
		label.add(custom);
		
		JButton retour = new JButton("Retour");
		retour.setBounds(50,700,70,30);
		retour.addActionListener((event)-> {this.setContentPane(fenetreMenu()); getContentPane().revalidate(); getContentPane().repaint();});
		label.add(retour);
		
		return panel;
	}
	
	@SuppressWarnings("static-access")
	public JPanel fenetreJeu() {
		JPanel resF = new JPanel();
		
		@SuppressWarnings("unused")
		LayoutManager layout = new BoxLayout(resF, BoxLayout.Y_AXIS);
		Box boxes[] = new Box[3];
		boxes[0] = Box.createHorizontalBox();
		boxes[1] = Box.createHorizontalBox();
		boxes[2] = Box.createHorizontalBox();

		boxes[0].createGlue();
		boxes[1].createGlue();
		boxes[2].createGlue();

		resF.add(boxes[0]);
		resF.add(boxes[1]);  
		resF.add(boxes[2]);

		JPanel scoreBar = new JPanel();
		
		scoreBar.add(new JLabel("Niveau:" + niveauCourant + "    Score : " + p.getScore()));
		JPanel res = new JPanel();
		res.setSize(new Dimension());

		GridLayout Gl = new GridLayout(p.getTableau().length-2, p.getTableau()[0].length-2,0,0);
		res.setLayout(Gl);
		
		JPanel jaugeFusee = new JPanel();
		jaugeFusee.setLayout(new GridLayout(3,0));
		JProgressBar jpb = new JProgressBar(0,20);
		jpb.setValue(p.getPointFusee());
		JButton fusee = new JButton("Fusee");
		if(!p.getFusee()) {
			fusee.setEnabled(false);
		}
		fusee.addActionListener(( event ) -> { p.setVaJouerFusee(true); });

		jaugeFusee.add(new JLabel("Coups avant d'avoir une fusee :"));
		jaugeFusee.add(jpb);
		jaugeFusee.add(fusee);

		scoreBar.setPreferredSize(new Dimension(800,20));
		res.setPreferredSize(new Dimension(800,620));
		jpb.setPreferredSize(new Dimension(800,20));


		boxes[0].add(scoreBar);
		boxes[1].add(res);
		boxes[2].add(jaugeFusee);

		Bloc[][] tab = p.getTableau();
		
		for(int i = 1;i<p.getTableau().length-1;i++) {
			for(int j = 1;j<p.getTableau()[i].length-1;j++) {
				CustomJButton jb = null;
				if(tab[i][j] instanceof BlocCouleur) {
					switch(((BlocCouleur) tab[i][j]).getColor()) {
						case("rouge"):
							jb = new CustomJButton(rouge,i,j);	
							break;
						case("bleu"):
							jb = new CustomJButton(bleu,i,j);	
							break;
						case("vert"):
							jb = new CustomJButton(vert,i,j);	
							break;
						case("jaune"):
							jb = new CustomJButton(jaune,i,j);	
							break;
						case("violet"):
							jb = new CustomJButton(violet,i,j);	
							break;
					}
				}
				if(tab[i][j] instanceof BlocAnimal) {
					if(((BlocAnimal) tab[i][j]).getAnimal().equals("cochon")) {
						jb = new CustomJButton(cochon,i,j);				
					}
				}
				if(tab[i][j] instanceof BlocFixe) {
					if(((BlocFixe) tab[i][j]).getMateriel().equals("planche")) {
						 jb = new CustomJButton(planche,i,j);
					}
				}
				if(tab[i][j] == null) {
					 jb = new CustomJButton(contour,i,j);
					 //jb.setEnabled(false);
				}
				jb.actionBouton();	
				res.add(jb);
			}
		}
		resF.add(scoreBar);
		resF.add(res);
		return resF;
	}
	
	public JPanel fenetreInfo() {
		JPanel fenetreInfo = new JPanel();
		fenetreInfo.setLayout(null);
		JLabel info = new JLabel(new ImageIcon(System.getProperty("java.class.path")+"/projetPOO/images/regle.png"));
		info.setBounds(0,0,800,800);
		JButton jb = new JButton("Retour");
		jb.setBounds(50,700,70,30);
		
		info.add(jb);
		fenetreInfo.add(info);
		
		
		jb.addActionListener(( event ) -> { this.setContentPane(fenetreMenu());
		getContentPane().revalidate();
		getContentPane().repaint(); });
		
		
		
		return fenetreInfo;
		
		
	}
	
	@SuppressWarnings("static-access")
	public JPanel fenetreEditeur() {
		JPanel resF = new JPanel();
		
		@SuppressWarnings("unused")
		LayoutManager layout = new BoxLayout(resF, BoxLayout.Y_AXIS);
		Box boxes[] = new Box[3];
		boxes[0] = Box.createHorizontalBox();
		boxes[1] = Box.createHorizontalBox();
		boxes[2] = Box.createHorizontalBox();

		boxes[0].createGlue();
		boxes[1].createGlue();
		boxes[2].createGlue();

		resF.add(boxes[0]);
		resF.add(boxes[1]);  
		resF.add(boxes[2]);
		
		// Boxe 0
		JPanel bouton = new JPanel();
		bouton.setLayout(new GridLayout(0,7,10,10));
		CustomJButton brouge = new CustomJButton(rouge,"rouge");
		CustomJButton bbleu = new CustomJButton(bleu,"bleu");
		CustomJButton bvert = new CustomJButton(vert,"vert");
		CustomJButton bviolet = new CustomJButton(violet,"violet");
		CustomJButton bjaune = new CustomJButton(jaune,"jaune");
		CustomJButton bplanche = new CustomJButton(planche,"planche");
		CustomJButton bcochon = new CustomJButton(cochon,"cochon");
		bouton.add(brouge);
		bouton.add(bbleu);
		bouton.add(bvert);
		bouton.add(bviolet);
		bouton.add(bjaune);
		bouton.add(bplanche);
		bouton.add(bcochon);
		bouton.setPreferredSize(new Dimension(800,100));
		boxes[0].add(bouton);
		
		brouge.addActionListener((event)->{ fonction = "rouge" ;});
		bbleu.addActionListener((event)->{ fonction = "bleu" ;});
		bvert.addActionListener((event)->{ fonction = "vert" ;});
		bviolet.addActionListener((event)->{ fonction = "violet" ;});
		bjaune.addActionListener((event)->{ fonction = "jaune" ;});
		bplanche.addActionListener((event)->{ fonction = "planche" ;});
		bcochon.addActionListener((event)->{ fonction = "cochon" ;});
		
		// Boxe 1
		JPanel res = new JPanel();
		GridLayout Gl = new GridLayout(editeur.getTableau().length-2, editeur.getTableau()[0].length-2,2,2);
		res.setLayout(Gl);
		Bloc[][] tab = editeur.getTableau();
		boxes[1].add(res);
		boxes[1].setPreferredSize(new Dimension(800,600));
		
		for(int i = 1;i<editeur.getTableau().length-1;i++) {
			for(int j = 1;j<editeur.getTableau()[i].length-1;j++) {
				CustomJButton jb = null;
				if(tab[i][j] instanceof BlocCouleur) {
					switch(((BlocCouleur) tab[i][j]).getColor()) {
						case("rouge"):
							jb = new CustomJButton(rouge,i,j);	
							break;
						case("bleu"):
							jb = new CustomJButton(bleu,i,j);	
							break;
						case("vert"):
							jb = new CustomJButton(vert,i,j);	
							break;
						case("jaune"):
							jb = new CustomJButton(jaune,i,j);	
							break;
						case("violet"):
							jb = new CustomJButton(violet,i,j);	
							break;
					}
				}
				if(tab[i][j] instanceof BlocAnimal) {
					if(((BlocAnimal) tab[i][j]).getAnimal().equals("cochon")) {
						jb = new CustomJButton(cochon,i,j);				
					}
				}
				if(tab[i][j] instanceof BlocFixe) {
					if(((BlocFixe) tab[i][j]).getMateriel().equals("planche")) {
						 jb = new CustomJButton(planche,i,j);
					}
				}
				if(tab[i][j] == null) {
					 jb = new CustomJButton(contour,i,j);
					 //jb.setEnabled(false);
				}
				
				jb.editeur();				
				res.add(jb);
			}
		}
		
		// Boxe 3
		boxes[2].setPreferredSize(new Dimension(800,50));
		JPanel boxe3 = new JPanel();
		boxe3.setLayout(new GridLayout(0,3,10,10));
		CustomJButton valider = new CustomJButton(buttonIcon,"Valider");
		valider.addActionListener((event) -> {
			try {
				FileOutputStream fos = new FileOutputStream(System.getProperty("java.class.path")+"/projetPOO/niveau/niveau_X.data");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(editeur);
				oos.close();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.setContentPane(fenetreMenu()); getContentPane().revalidate(); getContentPane().repaint();
		});
		
		CustomJButton retour = new CustomJButton(buttonIcon,"Retour");
		retour.addActionListener((event) -> {this.setContentPane(fenetreMenu()); getContentPane().revalidate(); getContentPane().repaint();});

		CustomJButton clear = new CustomJButton(buttonIcon,"Clear");
		clear.addActionListener((event) -> { editeur = new Plateau(10,10); this.setContentPane(fenetreEditeur()); getContentPane().revalidate(); getContentPane().repaint();});
		boxe3.add(valider);
		boxe3.add(retour);
		boxe3.add(clear);
		boxes[2].add(boxe3);
		
		
		return resF;
	}
	
	public void jouer() {
		//this.setContentPane(fenetreJeu());
		this.setContentPane(fenetreMap());
		getContentPane().revalidate();
		getContentPane().repaint();
	}
	
	public void jouerConsole() {
		this.dispose();
		Joueur j = new Joueur();
		while(j.getVeutJouer()) {
			
			if(niveauCourant>nbNiveau) {
				System.out.println("Vous avez fini tous les niveaux, bravo !");
				break;
			}
				try {
					FileInputStream fis = new FileInputStream(System.getProperty("java.class.path")+"/niveau/niveau_"+niveauCourant+".data");
					ObjectInputStream ois = new ObjectInputStream(fis);
					Plateau p = (Plateau)ois.readObject();
					
					Jeu game = new Jeu(j,p);
					if(j.demanderStr("Voulez vous qu'un joueur robot joue a votre place ? (oui/non)").equals("oui")) {
						game.jouerRobot();
					}
					else {
						game.jouer();
					}
					ois.close();
					fis.close();
	
					} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				}
			
		}
	}
	
	public void informations() {
		this.setContentPane(fenetreInfo());
		getContentPane().revalidate();
		getContentPane().repaint();
	}
	
	public void quitter() {
		Object[] options = {"Oui",
        "Non"};
		int n = JOptionPane.showOptionDialog(this,
				"Est-ce que vous en êtes sûr ?",
				"Quitter",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title
		if(n == JOptionPane.YES_OPTION) {
			this.dispose();
		}
	}
	
	public void editeurNiveau() {
		this.setContentPane(fenetreEditeur());
		getContentPane().revalidate();
		getContentPane().repaint();
	}
	
	public void clickDetection() {
		this.button.addActionListener(( event ) -> { jouer(); });
		this.button_1.addActionListener(( event ) -> { informations(); });
		this.button_2.addActionListener(( event ) -> { quitter(); });
		this.button_3.addActionListener((event)->{ jouerConsole();});
		this.button_4.addActionListener((event)->{ editeur = new Plateau(10,10); editeurNiveau();});
	}
	
	public void jouerTour(int x,int y) {
		p.jouerTour(x, y);
		p.checkAnimal();
		this.setContentPane(fenetreJeu());
		getContentPane().revalidate();
		getContentPane().repaint();
		
		if (p.jeuGagne()) {
			//p.afficher();
			if(niveauCourant != niveauFait.length) {
				niveauFait[niveauCourant] = true;
			}
			if(nbNiveau == niveauCourant) {
				JOptionPane.showMessageDialog(this,
					    "Vous avez fini tous les niveaux",
					    "bravo !",
					    JOptionPane.PLAIN_MESSAGE);
				this.jouer();
			}
			else {
				Object[] options = {"Oui",
	            "Non"};
				int n = JOptionPane.showOptionDialog(this,
						"Souhaitez vous continuer ?",
						"Vous avez gagne !",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,     //do not use a custom Icon
						options,  //the titles of buttons
						options[0]); //default button title
				if(n == JOptionPane.YES_OPTION) {
					niveauCourant+= 1;
					this.jouer();
				}
				else {
					this.dispose();
				}
			}
        }

        if (p.jeuPerdu()) {
        	Object[] options = {"Oui",
            "Non"};
			int n = JOptionPane.showOptionDialog(this,
					"Vous avez perdu !",
					"Souhaitez vous continuer ?",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,     //do not use a custom Icon
					options,  //the titles of buttons
					options[0]); //default button title
			if(n == JOptionPane.YES_OPTION) {
				this.p = this.getNiveau();
				this.jouer();
			}
			else {
				this.dispose();
			}
        }
	}
	
	public Plateau getNiveau() {
		try {
			FileInputStream fis = new FileInputStream(System.getProperty("java.class.path")+"/projetPOO/niveau/niveau_"+niveauCourant+".data");
			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(fis);
			Plateau res = (Plateau)ois.readObject();
			return res;

		} catch (FileNotFoundException e) {
		
		e.printStackTrace();
		} catch (IOException e) {
		
		e.printStackTrace();
		} catch (ClassNotFoundException e) {
		
		e.printStackTrace();
		}
		return null;
	}
	
	public Plateau getNiveauCustom() {
		try {
			FileInputStream fis = new FileInputStream(System.getProperty("java.class.path")+"/projetPOO/niveau/niveau_X.data");
			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(fis);
			Plateau res = (Plateau)ois.readObject();
			return res;

		} catch (FileNotFoundException e) {
		
		e.printStackTrace();
		} catch (IOException e) {
		
		e.printStackTrace();
		} catch (ClassNotFoundException e) {
		
		e.printStackTrace();
		}
		return null;
	}
	
	public static int getNiveauCourant() {
		return niveauCourant;
	}
	
	public int getNbNiveau() {
		return nbNiveau;
	}
	
	public static void setNiveauCourant(int nouveau) {
		niveauCourant = nouveau;
	}
	
}
