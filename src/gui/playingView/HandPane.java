package gui.playingView;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import shared.Card;
import shared.CardList;

public class HandPane extends JPanel{
	private CardList hand;
	private ArrayList<ViewObserver> observers;
	
	public HandPane(CardList hand, ArrayList<ViewObserver> observers) {
		super();
		this.hand = hand;
		this.observers = observers;
		this.setLayout(new HandLayout());	
		this.setPreferredSize(new Dimension(500, 200));
		this.setMinimumSize(this.getPreferredSize());
		this.update();
	}
	
	public void update() {
		this.removeAll();	
		for(Card c : this.hand) {
			ViewableCard vc = new ViewableCard(c, this.observers);
			this.add(vc);
		}
		this.validate();

	}
}
