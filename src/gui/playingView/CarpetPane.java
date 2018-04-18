package gui.playingView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import gui.BufferedDrawer;
import gui.Gui;
import gui.ObservableView;
import gui.PictureFactory.Pictures;
import shared.Player;
import shared.Seat;
import shared.client.ClientModel;

public class CarpetPane extends ObservableView{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6681250484175442412L;
	private BufferedImage imgCarpet;
	
	public static final Dimension minCardSize = new Dimension(60, 96);
	public static final Dimension minCoverSize = new Dimension(30, 48);
	public static final Dimension minCarpetSize = new Dimension(500, 500);
	
	public CarpetPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super(data, observers);
		this.imgCarpet = null;
		try {
			this.imgCarpet = Gui.pictureFactory.getPicture(Pictures.Carpet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		this.drawer = new CarpetDrawer(15);
		this.setMinimumSize(this.minCarpetSize);
	}
	
	CarpetDrawer drawer;
	BufferedDrawer bd = new BufferedDrawer();
	Graphics bg;
	@Override
	public void paint(Graphics g) {	
		bg = bd.getGraphics(getSize());
		bg.setColor(getBackground());
		bg.fillRect(0, 0, getWidth(), getHeight());
		
		//Calculate image sizes
		double scale = this.getSize().getWidth()/this.minCarpetSize.getWidth();
		Dimension carpetSize = new Dimension((int)(scale * this.minCarpetSize.getWidth()),
				(int)(scale * this.minCarpetSize.getHeight()));
		Dimension cardSize = new Dimension((int)(scale * this.minCardSize.getWidth()),
				(int)(scale * this.minCardSize.getHeight()));
		Dimension coverSize = new Dimension((int)(scale * this.minCoverSize.getWidth()),
				(int)(scale * this.minCoverSize.getHeight()));
		//Draw content
		bg.drawImage(Gui.pictureFactory.getScaledPicture(imgCarpet, carpetSize), 0, 0, null);
		drawer.drawDeck(bg, data.getDeck(), carpetSize, cardSize);
		//TODO get player data from client model
		Player p = new Player(null, "John Doe", Seat.NOTATTABLE, false, false, false, 0);
		drawer.drawPlayer(bg, "North", p, carpetSize, coverSize);
		drawer.drawPlayer(bg, "East", p, carpetSize, coverSize);
		drawer.drawPlayer(bg, "South", p, carpetSize, coverSize);
		drawer.drawPlayer(bg, "West", p, carpetSize, coverSize);
		bd.drawOnGraphics(g);	
	}
}
