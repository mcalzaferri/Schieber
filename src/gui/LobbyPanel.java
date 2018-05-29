package gui;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;
import gui.PictureFactory.Pictures;

public class LobbyPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1493026166806795278L;

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
		String titleString = "Sitzplatz auswählen";
		Font font = new Font("Monotype Corsiva", Font.BOLD, 30);
		
		g.setFont(font); 
		g.setColor(Color.WHITE);
	    
		int stringWidth = g.getFontMetrics(font).stringWidth(titleString);
		
		g.drawImage(Gui.pictureFactory.getScaledPicture(imgLobby, lobbySize), 0, 0, null);
		
		g.drawString(titleString, (this.getWidth()-stringWidth)/2, 2*this.getHeight()/7);
	}
}
