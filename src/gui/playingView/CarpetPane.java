package gui.playingView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import gui.Gui;
import gui.PictureFactory.Pictures;
import shared.Card;
import shared.CardList;
import shared.client.ClientModel;

public class CarpetPane extends JPanel{
	private ClientModel data;
	private ArrayList<ViewObserver> observers;
	
	public CarpetPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super();
		this.data = data;
		this.observers = observers;
		this.setBackground(Color.green);
		this.setPreferredSize(new Dimension(500, 500));
		this.setMinimumSize(this.getPreferredSize());
		this.update();
	}
	
	public void update() {
		repaint();

	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
			try {
				int index = 0;
				BufferedImage img;
				
				img = Gui.pictureFactory.getPicture(Pictures.Carpet,this.getSize());
				g.drawImage(img, 0, 0, null);
				for(Card c : this.data.getDeck()) {				
				img = Gui.pictureFactory.getPicture(c, new Dimension(50, 80));
					switch(index) {
					case 0:
						g.drawImage(img, 225, 335, null);
						break;
					case 1:
						g.drawImage(img, 300, 210, null);
						break;
					case 2:
						g.drawImage(img, 225, 85, null);
						break;
					case 3:
						g.drawImage(img, 150, 210, null);
						break;
					}
					index++;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
	}
}
