package gui.playingView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BoxLayout;
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
		this.setLayout(new FlowLayout());	
		this.setPreferredSize(new Dimension(500, 100));
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
