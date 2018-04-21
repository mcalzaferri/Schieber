package gui.playingView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import client.ViewEnumeration;
import client.ViewObserver;
import gui.Gui;
import gui.ObservableView;
import gui.Viewable;
import gui.PictureFactory.Pictures;
import shared.client.ClientModel;

/**
 * This class represents a playing field with a hand panel showing the
 * players hand, a black board panel showing the team's score and a carpet
 * panel showing the current deck.
 * 
 * @author mstieger
 *
 */
public class PlayingFieldView extends ObservableView implements Viewable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9149324107653292469L;
	private HandPane hand;
	private BlackBoardPane blackBoard;
	private CarpetPane carpet;
	private MessageBoard msgBoard;
	private JPanel content;
	private BufferedImage img;
	
	public PlayingFieldView(ClientModel data, ArrayList<ViewObserver> observers) {
		super(data, observers);
		if(data == null) {
			throw new IllegalArgumentException("Fatal Error: Card must not be null");
		}
		this.hand = new HandPane(data, observers);
		this.blackBoard = new BlackBoardPane(data, observers);
		this.carpet = new CarpetPane(data, observers);
		this.msgBoard = new MessageBoard();
		this.content = new JPanel();
		try {
			this.img = Gui.pictureFactory.getPicture(Pictures.Table);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeComponents();
	}
	
	private void initializeComponents() {
		PlayingFieldLayout l = new PlayingFieldLayout();
		this.setLayout(l);
		this.add(this.hand, PlayingFieldLayout.HAND);
		this.add(this.blackBoard, PlayingFieldLayout.BLACKBOARD);
		this.add(this.carpet, PlayingFieldLayout.CARPET);
		this.add(this.msgBoard, PlayingFieldLayout.INFO);
		this.setMinimumSize(l.minimumLayoutSize(this.getParent()));
		
		this.setDoubleBuffered(true);
		this.setOpaque(false);

		//Set up content
		content = new JPanel() {
			@Override
    		public void paintComponent(Graphics g) {
    			g.drawImage(Gui.pictureFactory.getScaledPicture(img, getSize()), 0, 0, null);
    		}
		};
		content.setLayout(new BorderLayout());
		content.add(this, BorderLayout.CENTER);
		content.setDoubleBuffered(true);
	}
	
	public void publish(String text) {
		this.msgBoard.publisch(20, text);
	}
	@Override
	public void update() {
		this.hand.update();
		this.blackBoard.repaint();
		this.carpet.repaint();	
		this.repaint();
	}

	@Override
	public JPanel getContent() {
		return this.content;
	}

	@Override
	public ViewEnumeration getType() {
		return ViewEnumeration.PLAYVIEW;
	}
	
}
