package client;

import shared.*;

public interface ClientViewInterface {
	
	public abstract void addObserver(ViewObserver observer);
	
	public abstract void moveCardToDeck(Player source,Card card);
	public abstract void moveDeckToPlayer(Player player);

	public abstract void changeView(ViewEnumeration view);
	public abstract void updateView();
	
	public abstract void updateScore(Score score);
}