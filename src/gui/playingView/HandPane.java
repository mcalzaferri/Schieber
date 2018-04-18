package gui.playingView;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import client.ViewObserver;
import gui.ObservableView;
import shared.Card;
import shared.client.ClientModel;

/**
 * Holds the clients hand and shows all the cards in a comfortable layout.
 * 
 * @author mstieger
 *
 */
public class HandPane extends ObservableView implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8159517916094656762L;
	
	public HandPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super(data, observers);
		initializeComponents();
	}
	
	/**
	 * Initialize the class' graphics
	 */
	private void initializeComponents() {
		this.setDoubleBuffered(true);
		this.setLayout(new HandLayout());
		this.setMinimumSize(new Dimension(CarpetPane.minCarpetSize.width, ViewableCard.minCardSize.height));
		this.update();
	}
	
	/**
	 * Creates a graphical representation of the clients hand.
	 * If the hand is null, all the existing components are just
	 * being removed and repainted.
	 */
	public void update() {
		this.removeAll();
		if(data.getHand() != null) {
			for(Card c : data.getHand()) {
				ViewableCard vc = null;
				try {
					vc = new ViewableCard(c);
					vc.addActionListener(this);
					vc.isAllowed(c.isAllowed(data.getHand().toArray(), data.getDeck().get(0), data.getTrump()));
					add(vc);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
		validate();	//Redo the layout
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(ViewObserver vo : observers) {
			try {
				ViewableCard vc = (ViewableCard) e.getSource();
				vo.btnCardClick(vc.getCard());
			}catch(ClassCastException ex) {
				//Source was not a viewable card => do nothing
			}
		}
		
	}
}
