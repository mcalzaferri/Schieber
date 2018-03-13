package gui;

import java.awt.AWTEvent;
import java.awt.Event;

import shared.ServerAddress;

public class SelectHostViewEvent extends AWTEvent{
	private ServerAddress serverAddress;
	
	public SelectHostViewEvent(Event event, ServerAddress serverAddress) {
		super(event);
		this.serverAddress = serverAddress;
	}
	
	public ServerAddress getServerAddress() {
		return this.serverAddress;
	}
}
