package gui.playingView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import client.ViewObserver;
import gui.Gui;
import shared.Card;

/**
 * This class is a graphical representation of a card. By inheriting from JButton class
 * it allows to detect clicks onto the card. An action listener forwards the event to 
 * the registered observers.
 * @author mstieger
 *
 */
public class ViewableCard extends JButton{
	private Card card;
	private ArrayList<ViewObserver> observers;
	private BufferedImage img;
	public final Dimension minCardSize = new Dimension(100, 160);
	
	/**
	 * Creates a new viewable card and loads a corresponding image from file system.
	 * 
	 * @param card	Card to be represented by the class
	 * @param observers	Observers to be informed on an occurring event
	 * @throws IOException 
	 */
	public ViewableCard(Card card, ArrayList<ViewObserver> observers) throws IOException {
		super();
		if(card == null) {
			throw new IllegalArgumentException("Card can not be null");
		}
		this.card = card;	
		this.observers = observers;
		this.img  = Gui.pictureFactory.getPicture(card);
		this.initializeComponents();
	}

	/**
	 * Initializes this component
	 * 
	 */
	private void initializeComponents() {
		this.setMinimumSize(minCardSize);	
		this.setOpaque(false);	//Ensures that the underlying pixels show through
		
		this.addActionListener(
		/**
		 * Informs observers if the button has been clicked.
		 * @author mstieger
		 *
		 */
		new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(observers != null) {
					for(ViewObserver vo : observers) {
						vo.btnCardClick(card);
					}
				}
			}		
		});
	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 * 
	 * Draws an image of a card onto the button. The clipping area is reduced to a round rectangle
	 * to ensure, that overlaying cards can show through.
	 */
	@Override
	public void paint(Graphics g) {
		try {
			g.setClip(new RoundRectangle2D.Double(0, 0, getSize().getWidth(), getSize().getHeight(), 25, 25));
			g.drawImage(Gui.pictureFactory.getScaledPicture(img, this.getSize()), 0, 0, null);
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
