package gui;

import java.awt.AWTEvent;
import java.awt.Event;
import shared.Card;

public class PlayingFieldViewEvent extends AWTEvent{
	private Card selectedCard;

	public PlayingFieldViewEvent(Event event, Card selectedCard) {
		super(event);
		this.selectedCard = selectedCard;
	}
	
	public Card getSelectedCard() {
		return this.selectedCard;
	}
}
