package gui.playingView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Timer;

import client.ViewObserver;
import gui.Gui;
import gui.ObservableView;
import gui.PictureFactory;
import gui.PictureFactory.Pictures;
import gui.animation.Animation;
import gui.animation.AnimationProperty;
import gui.animation.MovePictureAnimation;
import gui.animation.TestAnimation;
import shared.Player;
import shared.Trump;
import shared.client.ClientModel;

public class CarpetPane extends ObservableView{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6681250484175442412L;
	private BufferedImage imgCarpet;
	private Image imgScaledCarpet;
	private Dimension oldCarpetSize;
	public static final Dimension minCardSize = new Dimension(60, 96);
	public static final Dimension minCoverSize = new Dimension(30, 48);
	public static final Dimension minCarpetSize = new Dimension(500, 500);
	
	public CarpetPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super(data, observers);
		if(data == null) {
			throw new IllegalArgumentException("Fatal Error: Card must not be null");
		}
		this.imgCarpet = null;
		try {
			this.imgCarpet = Gui.pictureFactory.getPicture(Pictures.Carpet);
		} catch (IOException e) {
			// Fatal error => Should not happen
			e.printStackTrace();
		}	
		this.drawer = new CarpetDrawer(15);
		setOpaque(true);
		this.setMinimumSize(this.minCarpetSize);
		oldCarpetSize = new  Dimension(0, 0);
	}
	
	CarpetDrawer drawer;
	Font font;
	@Override
	public void paint(Graphics g) {
		//Calculate image sizes
		double scale = this.getSize().getWidth()/this.minCarpetSize.getWidth();
		Dimension carpetSize = new Dimension((int)(scale * this.minCarpetSize.getWidth()),
				(int)(scale * this.minCarpetSize.getHeight()));
		Dimension cardSize = new Dimension((int)(scale * this.minCardSize.getWidth()),
				(int)(scale * this.minCardSize.getHeight()));
		Dimension coverSize = new Dimension((int)(scale * this.minCoverSize.getWidth()),
				(int)(scale * this.minCoverSize.getHeight()));
		//Draw carpet as background
		if(oldCarpetSize.width != carpetSize.width || oldCarpetSize.height != carpetSize.height) {
			//If carpet size changed get new scaled picture
			oldCarpetSize = carpetSize;
			imgScaledCarpet = Gui.pictureFactory.getScaledPicture(imgCarpet, carpetSize);
		}
		g.drawImage(imgScaledCarpet, 0, 0, null);
		//Draw players
		g.setColor(Color.WHITE);
		font = BlackBoardPane.font;
		g.setFont(new Font(font.getFontName(), font.getStyle(), (int)(scale*font.getSize())));
		if(data.getPlayers() != null) {
			for(Player p : data.getPlayers().values()) {
				drawer.drawPlayer(g, p, data.getThisPlayer(), carpetSize, coverSize);
			}
		}
		
		//Draw deck (must be drawn over players so that the cards are always on top)
		drawer.drawDeck(g,data, carpetSize, cardSize);
	}
}
