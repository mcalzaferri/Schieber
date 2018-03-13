package shared.client;

import shared.*;

public abstract class AbstractClient {
	
	
	//Methoden
	public abstract void setTrumpf(Trumpf trumpf);
	
	public abstract void moveCardToDeck(Player source, Card card);
	
	//Should be called after moveCardToDeck to make sure that the Deck is correct
	public abstract void updateDeck(CardList deck);
	
	public abstract void updateHand(CardList hand);
	
	protected void publishWeis(Weis weis) {
		
	}
	
	protected void publishStich(Player winner) {
		
	}
	
	public abstract void updateScore(Score score);
	
	public abstract void endRound();
	
	public abstract void endGame(Team winner);
}
