package bot;

import java.util.ArrayList;
import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.Player;
import shared.Trump;
import shared.Weis;

public abstract class BotIntelligence {
	
	protected ArrayList<Card> cardsInHand;
	protected ArrayList<Card> cardsPlayed = new ArrayList<>();							
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
	
	public ArrayList<Weis> getWeise() {
		WeisLogic wl = new WeisLogic(cardsInHand,trump);
		return wl.getWeise();
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
	 * This function calculates the value of all colors on hand, e.g. useful for trump selection
	 * @return value for each colour (Eichel, Rose, Schilte, Schelle)
	 */
	public int[] getValueOnHand() {
		
		int[] values = new int[4];
		int modifier;
		for(Card c: cardsInHand) {
			switch(c.getValue()) {
			case SECHS:
				modifier = 5;
				break;
			case SIEBEN:
				modifier = 6;
				break;
			case ACHT:
				modifier = 7;
				break;
			case NEUN:
				modifier = 14; // Nell
				break;
			case ZEHN:
				modifier = 8;
				break;
			case UNDER:
				modifier = 20; //Bauer
				break;
			case OBER:
				modifier = 9;
				break;
			case KOENIG:
				modifier = 10;
				break;
			case ASS:
				modifier = 11;
				break;
			default:
				modifier = 0; // shouldn't happen
				break;
				
			}
			values[c.getColor().getId()-1] += modifier;
		}
			
		return values;
	}
	
	/**
	 * 
	 * @return Array with number of cards from each colour
	 */
	public int[] getAmountOnHand() {
		int[] values = new int[4];
		for(Card c: cardsInHand) {
			values[c.getColor().getId()-1]++;
		}
		return values;
	}
	
	/**
	 * This function returns the subset of allowed cards for this turn
	 * @return allowed cards
	 */
	public ArrayList<Card> getAllowedCards() {
		
		ArrayList<Card> allowedCards = new ArrayList<>();
		int sequence = deck.size();
		if(sequence == 0) { //first turn, everything allowed
			return cardsInHand;
		} else {
			Card firstPlayedCard = deck.get(0);
			CardColor colorInPlay = firstPlayedCard.getColor();
			for(Card c: cardsInHand) {
				if(c.getColor().equals(trump.getTrumpfColor())) { // Trump always allowed
					allowedCards.add(c);
				}
				else if(c.getColor().equals(colorInPlay)) { // similar colour to first card allowed
					allowedCards.add(c);
				}
			}
			if(allowedCards.isEmpty()) { //no valid cards in hand -> play anything
				return cardsInHand;
			}
			return allowedCards;
			
		}
	}
	
	/**
	 * auxiliary function to convert Card list to Array of IDs
	 * @param List of Card
	 * @return Integer Array of IDs
	 */
	protected int[] cardsToIds(ArrayList<Card> cards) {
		int[] ids = new int[cards.size()];
		for(int i=0; i<cards.size();i++) {
			ids[i] = cards.get(i).getId();
		}
		return ids;
	}
	
	// methods depending on strategy
	public abstract Card getNextCard();
	public abstract Trump selectTrump(boolean canSwitch);

	
	
}
