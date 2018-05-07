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
	 * to the hand and only modified if necessary.
	 */
	public void update() {
		if(data.getHand() != null) {
			Card c;
			ViewableCard vc;
			
			//Remove components if component array is bigger than hand
			if(getComponentCount() > data.getHand().size()) {
				for(int i = 0; i < getComponentCount() - data.getHand().size(); i++) {
					this.remove(getComponentCount() - 1 - i);
				}
			}
			
			//Update components (Viewable cards) and add new ones if necessary
			for(int i = 0; i < data.getHand().size(); i++) {
				c = data.getHand().get(i);
				
				if(i >= getComponentCount()) {
					//Not enough positions in component array => add new ones
					vc = new ViewableCard(c);
					vc.addActionListener(this);
					doEnableCard(vc);
					add(vc);
				}
				else if(getComponent(i) instanceof ViewableCard){
					vc = (ViewableCard) getComponent(i);
					doEnableCard(vc);
					
					if(!vc.getCard().equals(c)) {
						//Component on position i is not the same as in hand => change card
						vc.setCard(c);
					}
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
