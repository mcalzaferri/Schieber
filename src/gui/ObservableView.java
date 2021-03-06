package gui;

import java.util.List;

import javax.swing.JPanel;

import client.ViewObserver;
import client.shared.ClientModel;

public abstract class ObservableView extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7573079388144240625L;
	protected List<ViewObserver> observers;
	protected ClientModel data;
	
	public ObservableView(ClientModel data, List<ViewObserver> observers) {
		this.data = data;	//Data has to be checked in subclass
		this.observers = observers;
		
		//Set up JPanel
		this.setDoubleBuffered(true);
	}
}
