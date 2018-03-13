package shared.client;

import shared.*;

public abstract class AbstractClient {
	
	//Methoden	
	public abstract void setTrump(Trump trump);
	
	public abstract void moveCardToDeck(Player source, Card card);
	
	//Should be called after moveCardToDeck to make sure that the Deck is correct
	public abstract void updateDeck(CardList deck);
	
	public abstract void updateHand(CardList hand);
	
	protected void publishWeis(Weis weis) {
		
	}
	
	protected void publishStich(Player winner) {
		
	}
	
	public abstract void updateScore(Score score);
	
	public abstract Card chooseCard();	//fkaiser: needed for bots
	
	public abstract void endRound();
	
	public abstract void endGame(Team winner);

}
