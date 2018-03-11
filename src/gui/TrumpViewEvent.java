package gui;

import java.awt.AWTEvent;
import java.awt.Event;
import shared.Trumpf;

public class TrumpViewEvent extends AWTEvent{
	private Trumpf trumpf;
	
	public TrumpViewEvent(Event event, Trumpf trumpf) {
		super(event);
		this.trumpf = trumpf;
	}

	public Trumpf getTrumpf() {
		return this.trumpf;
	}
}
