package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;
import gui.PictureFactory.Pictures;

public class LobbyPanel extends JPanel{
	
	private BufferedImage imgLobby;
	
	public static final Dimension minLobbySize = new Dimension(500,500);
	
	public LobbyPanel()
	{
		super();

		this.imgLobby = null;
		try {
			this.imgLobby = Gui.pictureFactory.getPicture(Pictures.Lobby);
			//Prescale picture to fasten rendering process
			this.imgLobby = Gui.pictureFactory.toBufferedImage(Gui.pictureFactory.getScaledPicture(imgLobby,
					new Dimension(imgLobby.getWidth()/2, imgLobby.getHeight()/2)));
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
		//super.paint(g);
		//Calculate image size
		Dimension lobbySize = new Dimension((int)(this.getSize().getWidth()),
				(int)(this.getSize().getHeight()));
		
		//Draw content
		g.drawImage(Gui.pictureFactory.getScaledPicture(imgLobby, lobbySize), 0, 0, null);
		
	}
}
