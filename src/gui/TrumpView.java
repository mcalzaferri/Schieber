package gui;

import java.util.ArrayList;
import client.ViewEnumeration;
import client.ViewObserver;
import shared.Trump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class TrumpView extends ObservableView implements Viewable{
	private JButton eichelButton;
	private JButton roseButton;
	private JButton schilteButton;
	private JButton schelleButton;
	private JButton obenabeButton;
	private JButton undenufeButton;
	private JButton schiebenButton;
	
	
    private static final int width = 300;
    private static final int height = 500;
    private static final int top = 
    Toolkit.getDefaultToolkit().getScreenSize().height/2-height/2;
    private static final int left = 
    Toolkit.getDefaultToolkit().getScreenSize().width/2-width/2;
    
    
	public TrumpView(ArrayList<ViewObserver> observers) {
		super(null, observers);
		
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill=GridBagConstraints.BOTH;
	    c.insets = new Insets(0,0,10,0);
	    
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWeights = new double[] {0.1,0.4,0.4,0.1};
		gbl.rowWeights = new double[] {0.1,0.8/7,0.8/7,0.8/7,0.8/7,0.8/7,0.8/7,0.8/7,0.1};
		this.setLayout(gbl);
		
		try{
		    
		    c.gridx = 0;
		    c.gridy = 0;
		    c.gridwidth = 4;
		    this.add(Box.createGlue(),c);
		    
		    c.gridx = 0;
		    c.gridy = 8;
		    this.add(Box.createGlue(),c);
		    
		    
		    
		    //EICHEL
		    c.gridx = 1;
		    c.gridy = 1;
		    c.gridwidth = 1;    
			this.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.EICHEL))),c);
			
		    c.gridx = 2;
		    c.gridy = 1; 
			eichelButton = new JButton("Eichel");
			this.add(eichelButton,c);
			
			
			
			//ROSE
		    c.gridx = 1;
		    c.gridy = 2;
			this.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.ROSE))),c);
			
		    c.gridx = 2;
		    c.gridy = 2;
			roseButton = new JButton("Rose");
			this.add(roseButton,c);
			
			
			
			//SCHILTE
		    c.gridx = 1;
		    c.gridy = 3;
			this.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.SCHILTE))),c);
			
		    c.gridx = 2;
		    c.gridy = 3;
			schilteButton = new JButton("Schilte");
			this.add(schilteButton,c);
			
			
			
			//SCHELLE
		    c.gridx = 1;
		    c.gridy = 4;
			this.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.SCHELLE))),c);
			
		    c.gridx = 2;
		    c.gridy = 4;
			schelleButton = new JButton("Schelle");
			this.add(schelleButton,c);
			
			
			
			//OBNEABE
		    c.gridx = 1;
		    c.gridy = 5;
			this.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.OBENABE))),c);
			
		    c.gridx = 2;
		    c.gridy = 5;
			obenabeButton = new JButton("Obenabe");
			this.add(obenabeButton,c);
			
			
			
			//UNEUFE
		    c.gridx = 1;
		    c.gridy = 6;
			this.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.UNEUFE))),c);
			
		    c.gridx = 2;
		    c.gridy = 6;
			undenufeButton = new JButton("Undenufe");
			this.add(undenufeButton,c);
			
			
			
			//SCHIEBEN
		    c.gridx = 1;
		    c.gridy = 7;
			this.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.SCHIEBEN))),c);
			
		    c.gridx = 2;
		    c.gridy = 7;
			schiebenButton = new JButton("Schieben");
			this.add(schiebenButton,c);
		
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
		setPreferredSize(new Dimension(width,height));
		setLocation(left,top);
		
		
		eichelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnTrumpClick(Trump.EICHEL);
				}
			}
		});
		
		roseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnTrumpClick(Trump.ROSE);
				}
			}
		});
		
		schilteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnTrumpClick(Trump.SCHILTE);
				}
			}
		});
		
		schelleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnTrumpClick(Trump.SCHELLE);
				}
			}
		});
		
		obenabeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnTrumpClick(Trump.OBENABE);
				}
			}
		});
		
		undenufeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnTrumpClick(Trump.UNEUFE);
				}
			}
		});
		
		schiebenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(ViewObserver observer: observers)
				{
					observer.btnTrumpClick(Trump.SCHIEBEN);
				}
			}
		});
		

		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {

	}

	@Override
	public JPanel getContent() {
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		return ViewEnumeration.SELECTTRUMPVIEW;
	}

}
