package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import gui.PictureFactory.Pictures;
import gui.playingView.CarpetDrawer;
import shared.Player;
import shared.client.ClientModel;

public class LobbyPanel extends JPanel{
	
	private BufferedImage imgLobby;
	
	public static final Dimension minLobbySize = new Dimension(500,500);
	
	public LobbyPanel()
	{
		super();
		
		this.imgLobby = null;
		try {
			this.imgLobby = Gui.pictureFactory.getPicture(Pictures.Lobby);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void update()
	{
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {	
		super.paint(g);
		//Calculate image size
		Dimension lobbySize = new Dimension((int)(this.getSize().getWidth()),
				(int)(this.getSize().getHeight()));
		
		//Draw content
		g.drawImage(Gui.pictureFactory.getScaledPicture(imgLobby, lobbySize), 0, 0, null);
	}
}
