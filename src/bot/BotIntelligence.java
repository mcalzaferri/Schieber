package bot;

import java.util.ArrayList;
import shared.Card;
import shared.Player;
import shared.Trump;

public abstract class BotIntelligence {
	
	protected ArrayList<Card> cardsInHand;
	protected ArrayList<Card> cardsPlayed;							
	protected ArrayList<Card> deck;										
	protected Player partner;
	protected int turn;
	protected Trump trump;
	
	// common methods for any intelligence
	/**
	 * updates the cards in Hand
	 * @param currHand, Array of card IDs
	 */
	public void setHand(int[] currHand) {
		cardsInHand = getCardListByIds(currHand);
	}
	
	/**
	 * updates the trump color, meaning someone chose the trump
	 * @param trump
	 */
	public void setTrump(Trump trump) {
		this.trump = trump;	
	}
	
	/**
	 * updates the Deck, meaning the already played cards
	 * @param currDeck
	 */
	public void setDeck(int[] currDeck)
	{
		//refresh current deck "on table"
		deck = getCardListByIds(currDeck);
		
		//add last played card to cardsPlayed
		cardsPlayed.add(deck.get(deck.size() - 1));
	}
	
	/**
	 * auxiliary function to convert the integer arrays into ArrayLists which are easier to handle
	 * @param cardIds
	 * @return ArrayList<Card>
	 */
	private ArrayList<Card> getCardListByIds(int[] cardIds){
		ArrayList<Card> list = new ArrayList<>();
		for(int i : cardIds) {
			list.add(Card.getCardById(i));
		}
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	private int[] getValueOnHand() {
		
		int[] values = new int[4];
			
		return values;
	}
	
	// methods depending on strategy
	public abstract Card getNextCard();
	public abstract Trump selectTrump(boolean canSwitch);
	
}
