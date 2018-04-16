package gui;

import java.util.ArrayList;
import client.ViewEnumeration;
import client.ViewObserver;
import shared.ServerAddress;
import shared.Trump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class TrumpView extends AbstractView{
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
    
    
	public TrumpView(ViewEnumeration viewType, ArrayList<ViewObserver> observers) {
		super(viewType, observers);
		setTitle("Trumpfauswahl");
		
		JPanel trumpViewPanel = new JPanel();
		trumpViewPanel.setLayout(new GridLayout(7,2));
		
		try{
			
			trumpViewPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.EICHEL))));
			eichelButton = new JButton("Eichel");
			trumpViewPanel.add(eichelButton);
			
			trumpViewPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.ROSE))));
			roseButton = new JButton("Rose");
			trumpViewPanel.add(roseButton);
			
			trumpViewPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.SCHILTE))));
			schilteButton = new JButton("Schilte");
			trumpViewPanel.add(schilteButton);
			
			trumpViewPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.SCHELLE))));
			schelleButton = new JButton("Schelle");
			trumpViewPanel.add(schelleButton);
			
			trumpViewPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.OBENABE))));
			obenabeButton = new JButton("Obenabe");
			trumpViewPanel.add(obenabeButton);
			
			trumpViewPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.UNEUFE))));
			undenufeButton = new JButton("Undenufe");
			trumpViewPanel.add(undenufeButton);
			
			trumpViewPanel.add(new JLabel(new ImageIcon(Gui.pictureFactory.getPicture(Trump.SCHIEBEN))));
			schiebenButton = new JButton("Schieben");
			trumpViewPanel.add(schiebenButton);
		
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		add(trumpViewPanel);
		setSize(width,height);
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

}
