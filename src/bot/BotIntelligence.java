package bot;

import java.util.ArrayList;
import java.util.Arrays;

import shared.Card;
import shared.CardList;
import shared.Player;
import shared.Trump;
import ch.ntb.jass.common.proto.*;

public abstract class BotIntelligence {
	
	protected ArrayList<Card> cardsInHand;
	protected ArrayList<Card> cardsPlayed;							
	protected ArrayList<Card> deck;				//fkaiser: added, but cardsPlayed can be used							
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
		deck = getCardListByIds(currDeck);
		
		//TODO: (fkaiser) add last Card of currDeck to cardsPlayed so no setcardPlayed-function is needed
		//example:
		//cardsPlayed.add(currDeck(IndexOfLastCard))
		
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
	
	// methods depending on strategy
	public abstract Card getNextCard();
	public abstract Trump selectTrump(boolean canSwitch);
	
}
