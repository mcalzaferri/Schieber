package gui.playingView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import gui.Gui;
import gui.PictureFactory.Pictures;
import shared.client.ClientModel;

/**
 * This class represents a blackboard and displays the score of all
 * teams and the rounds current trump.
 * 
 * @author mstieger
 *
 */
public class BlackBoardPane extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7805022564116303797L;
	private ClientModel data;
	private ArrayList<ViewObserver> observers;
	private BlackBoardDrawer drawer;
	private BufferedImage img;
	
	public static final Font font = new Font("MV Boli" ,Font.PLAIN, 28);
	public static final Dimension minSize = new Dimension(350, 500);
	public static final Dimension minTrumpSize = new Dimension(50,70);
	public static final Rectangle minInnerBounds = new Rectangle(50, 50, minSize.width - 100, minSize.height -100);
	
	public BlackBoardPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super();
		this.drawer = new BlackBoardDrawer();
		try {
			this.img = Gui.pictureFactory.getPicture(Pictures.BlackBoard);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(data == null) {
			throw new IllegalArgumentException("Data can not be null");
		}
		this.data = data;
		this.observers = observers;	
		this.setMinimumSize(minSize);
		this.update();
	}
	
	/**
	 * Updates the blackboard. This means that the scores and the current trump are
	 * drawn anew.
	 */
	public void update() {
		//TODO
		repaint();
	}
	
	Rectangle r = new Rectangle();
	Dimension dim = new Dimension();
	Font f = font;
	@Override
	public void paint(Graphics g) {
		double scale = this.getSize().getHeight()/minSize.getHeight();
		r.setRect(scale*minInnerBounds.x, scale*minInnerBounds.y,
				scale*minInnerBounds.width, scale*minInnerBounds.height);
		dim.setSize(scale*minTrumpSize.width, scale*minTrumpSize.height);
		
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(Gui.pictureFactory.getScaledPicture(img, this.getSize()), 0, 0, null);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font(font.getFontName(), font.getStyle(), (int)(scale*font.getSize())));
		drawer.setBounds(r);
		drawer.drawTitle(g, "Schieber");
		//TODO get score
		drawer.drawText(g, new String[]{"Score Team 1", "Score Team 2"});
		drawer.drawTrump(g, data.getTrump(), dim);
	}
}
