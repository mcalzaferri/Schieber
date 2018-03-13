package client;

import shared.*;
import shared.client.ClientModel;

public abstract class AbstractClientView {
	protected ClientModel data;
	
	public AbstractClientView(ClientModel data) {
		this.data = data;
	}
	
	public abstract void addObserver(ViewObserver observer);
	public abstract void changeView(ViewEnumeration view);
	public abstract ViewEnumeration getCurrentView();
	public abstract void updateView();
	public abstract void updateScore(Score score);
}