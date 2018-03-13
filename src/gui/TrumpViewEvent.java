package gui;

import java.awt.AWTEvent;
import java.awt.Event;
import shared.Trump;

public class TrumpViewEvent extends AWTEvent{
	private Trump trump;
	
	public TrumpViewEvent(Event event, Trump trump) {
		super(event);
		this.trump = trump;
	}

	public Trump getTrumpf() {
		return this.trump;
	}
}
