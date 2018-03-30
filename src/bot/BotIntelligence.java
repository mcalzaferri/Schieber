package bot;

import java.util.ArrayList;
import java.util.Arrays;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.GameMode;
import shared.Trump;
import shared.Weis;

public abstract class BotIntelligence {
	
	protected ArrayList<Card> cardsInHand;
	protected ArrayList<Card> cardsPlayed = new ArrayList<>();							
	protected ArrayList<Card> deck;
	 // store the current highest cards, EICHEL, ROSE, SCHILTE, SCHELLE, generally highest card
	protected Card[] maxCardsInPlay = {Card.getCardById(19),Card.getCardById(29),Card.getCardById(39),Card.getCardById(49),null};
	protected ArrayList<KnownCard> knownCards = new ArrayList<>();
	protected ArrayList<Card> partnerCards = new ArrayList<>(); // known cards of our partner
	protected ArrayList<Card> enemyCards = new ArrayList<>(); // known cards of our enemies
	protected int partnerID, selfID, activePlayerID;
	protected int turn;
	protected Trump trump;
	
	// common methods for any intelligence
	/**
	 * updates the cards in Hand
	 * @param currHand, Array of card IDs
	 */
	public void setHand(int[] currHand) {
		cardsInHand = getCardListByIds(currHand);
		// add cards in Hand to knownCards
		for(Card c : cardsInHand) {
			knownCards.add(new KnownCard(c,selfID,false));
		}
	}
	
	/**
	 * updates the trump colour, meaning someone chose the trump
	 * @param trump
	 */
	public void setTrump(Trump trump) {
		this.trump = trump;	
		if (trump.getGameMode() == GameMode.TRUMPF) {
			maxCardsInPlay[trump.getTrumpfColor().getId()-1] = new Card(trump.getTrumpfColor(),CardValue.UNDER);
			maxCardsInPlay[4] = new Card(trump.getTrumpfColor(),CardValue.UNDER);
		} else if (trump.getGameMode() == GameMode.UNEUFE) {
			maxCardsInPlay[0] = new Card(CardColor.EICHEL, CardValue.SECHS);
			maxCardsInPlay[1] = new Card(CardColor.ROSE, CardValue.SECHS);
			maxCardsInPlay[2] = new Card(CardColor.SCHILTE, CardValue.SECHS);
			maxCardsInPlay[3] = new Card(CardColor.SCHELLE, CardValue.SECHS);
		}
	}
	
	/**
	 * updates the Deck, meaning the cards on the table
	 * @param currDeck
	 */
	public void setDeck(int[] currDeck)
	{
		//refresh current deck "on table"
		deck = getCardListByIds(currDeck);
		
		//add last played card to cardsPlayed and knownCards, update partner cards and enemy cards
		if(!deck.isEmpty()) {
			cardsPlayed.add(deck.get(deck.size() - 1));
			knownCards.add(new KnownCard(deck.get(deck.size() - 1), activePlayerID, true));
			partnerCards.remove(deck.get(deck.size() - 1));
			enemyCards.remove(deck.get(deck.size() - 1));
		}
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
		Arrays.sort(cardIds); //sort the Cards on Hand
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
	 * This function adds "gewiesene" Karten to our pool of known cards
	 * @param wiis
	 * @param player
	 */
	public void showWeis(Weis[] wiis, int playerID) {
		int noOfCards;
		boolean folge;
		int originCardID;
		for(Weis w : wiis) {
			noOfCards = 0;
			folge = true;
			originCardID = w.getOriginCard().getId();
			switch(w.getType()) {
			case STOECK:
				noOfCards = 2;
				break;
			case ACHTBLATT:
				noOfCards = 8;
				break;
			case DREIBLATT:
				noOfCards = 3;
				break;
			case FUENFBLATT:
				noOfCards = 5;
				break;
			case NEUNBLATT:
				noOfCards = 9;
				break;
			case SECHSBLATT:
				noOfCards = 6;
				break;
			case SIEBENBLATT:
				noOfCards = 7;
				break;
			case VIERBLATT:
				noOfCards = 4;
				break;
			case VIERBAUERN:
			case VIERGLEICHE:
			case VIERNELL:
				noOfCards = 4;
				folge = false;				
				break;
			default:
				break; 
			}
			Card c = Card.getCardById(originCardID);
			knownCards.add(new KnownCard(c, playerID, false));
			boolean partner = (partnerID == playerID);
			if(partner) { partnerCards.add(c); }
			else { enemyCards.add(c); }
			if(folge) {
				for(int i = 1; i<noOfCards; i++) {
					c = Card.getCardById(originCardID+i);
					knownCards.add(new KnownCard(c, playerID, false));
					if(partner) { partnerCards.add(c); }
					else { enemyCards.add(c); }
				}
			} else {
				for(int i = 1; i<noOfCards; i++) {
					c = Card.getCardById(originCardID+i*10);
					knownCards.add(new KnownCard(c, playerID, false));
					if(partner) { partnerCards.add(c); }
					else { enemyCards.add(c); }
				}
			}
		}
		
	}
	
	public void setActivePlayerID(int activeSeatId) {
		activePlayerID = activeSeatId;
	}
	
	public void setSelfID(int seatId) {
		selfID = seatId;
	}
	
	public void setPartnerID(int partnerID) {
		this.partnerID = partnerID;
	}
	
	/**
	 * This function checks the deck and updates the current list of highest cards
	 */
	public void updateMaxCards() {
		for(Card c : deck) {
			int index = c.getColor().getId()-1;
			if(c.equals(maxCardsInPlay[index])) {
				if(c.getValue() == CardValue.SECHS) {
					maxCardsInPlay[index] = null;
				} else {
					if(c.getColor() == trump.getTrumpfColor()) { // different logic for Trumpf
						if(maxCardsInPlay[index].getValue() == CardValue.UNDER) { // Bauer
							maxCardsInPlay[index] = Card.getCardById(c.getId()-2); // set to Nell
						} else if (maxCardsInPlay[index].getValue() == CardValue.NEUN){ // Nell
							maxCardsInPlay[index] = Card.getCardById(c.getId()+5); // set to Ass
						} else {
							maxCardsInPlay[index] = Card.getCardById(c.getId()-1);
							if(maxCardsInPlay[index].getValue() == CardValue.UNDER || maxCardsInPlay[index].getValue() == CardValue.NEUN) {
								maxCardsInPlay[index] = Card.getCardById(c.getId()-1);
							}
						}
					} else {
						maxCardsInPlay[index] = Card.getCardById(c.getId()-1);
					}
				}
			}
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
