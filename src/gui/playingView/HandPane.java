package gui.playingView;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

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
	private HashMap<Card, ViewableCard> cards;
	
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
		cards = new HashMap<>(9);
		setOpaque(false);
		this.setLayout(new HandLayout());
		this.setMinimumSize(new Dimension(CarpetPane.minCarpetSize.width, ViewableCard.minCardSize.height));
	}
	
	/**Creates a graphical representation of the clients hand.
	 * In order to prevent the cards from flickering due to repeatedly
	 * calling this method, the cards in the components array are compared
	 * to the hand and only modified if necessary.
	 */
	public void update() {
		if(data.getHand() != null) {
			if(cards.isEmpty()) {
				ArrayList<ViewableCard> newCards = new ArrayList<>(9);
				//Initialize cards if cards is currently empty
				for(Card c : data.getHand()) {
					ViewableCard vc = new ViewableCard(c);
					vc.addActionListener(this);
					doEnableCard(vc);
					cards.put(c, vc);
					newCards.add(vc);
					// Do not add until all cards are created add(vc);
				}
				for(ViewableCard vc : newCards) {
					//Now add all cards together for smoother display
					add(vc);
				}
			}else {
				//Update components
				//Add non existing cards
				for(Card c : data.getHand()) {
					if(!cards.containsKey(c)) {
						//Create new Card only if it is non existing
						ViewableCard vc = new ViewableCard(c);
						vc.addActionListener(this);
						doEnableCard(vc);
						add(vc);
						cards.put(c, vc);
					}else {
						//Otherwise just update Card
						doEnableCard(cards.get(c));
					}
				}
				//Search for deletedCards
				ArrayList<Card> deletedCards = new ArrayList<>();
				for(Card c : cards.keySet()) {
					if(!data.getHand().contains(c)) {
						deletedCards.add(c);
					}
				}
				//Remove deletedCards
				for(Card c : deletedCards) {
					this.remove(cards.get(c));
					cards.remove(c);
				}
			}
			if(!isValid()) {
				validate(); //can be time consuming => only call if necessary
			}
		}
		repaint();
	}

	private void doEnableCard(ViewableCard vc) {
		if(data.getDeck().toArray().length > 0) {
			//isAllowed method can handle null values!!! (But should not /Maurus)
			if(data.getTrump() != null) {
				vc.setEnabled(vc.getCard().isAllowed(data.getHand().toArray(), 
						data.getDeck().get(0), 
						data.getTrump()));
			}else {
				vc.setEnabled(true);
			}
			
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
