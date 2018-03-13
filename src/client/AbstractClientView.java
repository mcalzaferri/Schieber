package client;

import shared.*;

public abstract class AbstractClientView {
	protected CardList hand;
	protected CardList deck;
	protected Trumpf trump;
	protected Score score;
	
	public AbstractClientView(CardList hand, CardList deck, Trumpf trump, Score score) {
		this.hand = hand;
		this.deck = deck;
		this.trump = trump;
		this.score = score;
	}
	
	public abstract void addObserver(ViewObserver observer);
	
	public abstract void moveCardToDeck(Player source,Card card);
	public abstract void moveDeckToPlayer(Player player);

	public abstract void changeView(ViewEnumeration view);
	public abstract void updateView();
	
	public abstract void updateScore(Score score);
}