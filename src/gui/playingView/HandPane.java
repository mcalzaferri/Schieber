package gui.playingView;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		if(data == null) {
			throw new IllegalArgumentException("Fatal Error: Card must not be null");
		}
		initializeComponents();
	}
	
	/**
	 * Initialize the class' graphics
	 */
	private void initializeComponents() {
		setOpaque(false);
		this.setLayout(new HandLayout());
		this.setMinimumSize(new Dimension(CarpetPane.minCarpetSize.width, ViewableCard.minCardSize.height));
	}
	
	/**Creates a graphical representation of the clients hand.
	 * In order to prevent the cards from flickering due to repeatedly
	 * calling this method, the cards in the components array are compared
	 * to the hand. Only if data has changed, the buttons will be repainted.
	 * TODO Find a better solution if there is time, e.g. draw cards directly
	 * without using buttons
	 */
	public void update() {
		ViewableCard vc;
		boolean changed = false;
		
		//Check if cards have changed
		if(data.getHand() == null || getComponentCount() != data.getHand().size()) {
			//If array size differs, the hand has surely changed
			changed = true;
		}
		else {
			/*
			 * TODO Compare hand to components if size is the same
			 * During regular game this should not occur. When a card is
			 * played, the hand becomes smaller automatically.
			 */
		}

		if(changed) {
			//Create new components from hand
			this.removeAll();
			
			if(data.getHand() != null) {
				for(Card c : data.getHand()) {
					vc = new ViewableCard(c);
					vc.addActionListener(this);
					doEnableCard(vc);
					add(vc);
				}
			}	
			validate();	//Redo the layout
			
		}else {
			//Just do enabling of cards
			if(data.getHand() != null) {
				for(Component c : getComponents()) {
					if(c instanceof ViewableCard) {
						vc = (ViewableCard) c;
						doEnableCard(vc);
					}
				}
			}	
		}
	
		repaint();	//Let panel draw its components
	}

	private void doEnableCard(ViewableCard vc) {
		if(data.getDeck().toArray().length > 0) {
			//isAllowed method can handle null values!!!
			vc.setEnabled(vc.getCard().isAllowed(data.getHand().toArray(), 
					data.getDeck().get(0), 
					data.getTrump()));
		}
		else {
			vc.setEnabled(vc.getCard().isAllowed(data.getHand().toArray(), 
					null, 
					data.getTrump()));
		}
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
