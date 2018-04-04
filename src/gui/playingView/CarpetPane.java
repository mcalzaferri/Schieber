package gui.playingView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import gui.BufferedDrawer;
import gui.Gui;
import gui.PictureFactory.Pictures;
import shared.Card;
import shared.client.ClientModel;

public class CarpetPane extends JPanel{
	private ClientModel data;
	private ArrayList<ViewObserver> observers;
	private BufferedImage imgCarpet;
	public final Dimension minCardSize = new Dimension(60, 96);
	public final Dimension minCarpetSize = new Dimension(500, 500);
	
	public CarpetPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super();
		this.imgCarpet = null;
		try {
			this.imgCarpet = Gui.pictureFactory.getPicture(Pictures.Carpet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.data = data;
		this.observers = observers;
		this.setPreferredSize(this.minCarpetSize);
		this.setMinimumSize(this.minCarpetSize);
		this.update();
	}
	
	public void update() {
		repaint();
	}
	
	private BufferedDrawer bd = new BufferedDrawer();
	private Graphics bg;
	@Override
	public void paint(Graphics g) {	
			try {
				bg = bd.getGraphics(getSize());
				bg.setColor(getBackground());
				bg.fillRect(0, 0, getWidth(), getHeight());
				//Calculate image sizes
				double scale = this.getSize().getWidth()/this.minCarpetSize.getWidth();
				Dimension carpetSize = new Dimension((int)(scale * this.minCarpetSize.getWidth()),
						(int)(scale * this.minCarpetSize.getHeight()));
				Dimension cardSize = new Dimension((int)(scale * this.minCardSize.getWidth()),
						(int)(scale * this.minCardSize.getHeight()));
				
				//Draw carpet image
				bg.drawImage(Gui.pictureFactory.getScaledPicture(imgCarpet, carpetSize), 0, 0, null);
				
				//Draw deck on carpet (centered in carpet)
				Image img;
				int index = 0; int x = 0; int y = 0;
				int xOffset = -cardSize.width/2;
				int yOffset = -cardSize.height/2;
				for(Card c : this.data.getDeck()) {				
					img = Gui.pictureFactory.getPicture(c, cardSize);
					switch(index) {
					case 0:
						x = carpetSize.getSize().width/2 + xOffset;
						y = carpetSize.getSize().height*2/3 + yOffset;
						bg.drawImage(img, x, y, null);
						break;
					case 1:
						x = carpetSize.getSize().width*2/3 + xOffset;
						y = carpetSize.getSize().height/2 + yOffset;
						bg.drawImage(img, x, y, null);
						break;
					case 2:
						x = carpetSize.getSize().width/2 + xOffset;
						y = carpetSize.getSize().height*1/3 + yOffset;
						bg.drawImage(img, x, y, null);
						break;
					case 3:
						x = carpetSize.getSize().width*1/3 + xOffset;
						y = carpetSize.getSize().height/2 + yOffset;
						bg.drawImage(img, x, y, null);
						break;
					}
					index++;
					bd.drawOnGraphics(g);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
	}
}
