package gui;

import java.awt.AWTEvent;
import java.awt.Event;

import client.ServerAdress;

public class SelectHostViewEvent extends AWTEvent{
	private ServerAdress serverAddress;
	
	public SelectHostViewEvent(Event event, ServerAdress serverAddress) {
		super(event);
		this.serverAddress = serverAddress;
	}
	
	public ServerAdress getServerAddress() {
		return this.serverAddress;
	}
}
