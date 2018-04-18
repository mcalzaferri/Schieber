package gui;

import java.util.List;

import javax.swing.JPanel;

import client.ViewObserver;
import shared.client.ClientModel;

public abstract class ObservableView extends JPanel{
	protected List<ViewObserver> observers;
	protected ClientModel data;
	
	public ObservableView(ClientModel data, List<ViewObserver> observers) {
		this.data = data;	//Data has to be checked in subclass
		this.observers = observers;
		
		//Set up JPanel
		this.setDoubleBuffered(true);
	}
}
