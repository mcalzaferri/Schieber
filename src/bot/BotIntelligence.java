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
	protected Card[] maxCardsInPlay = {new Card(19),new Card(29),new Card(39),new Card(49),null};
	protected ArrayList<KnownCard> knownCards = new ArrayList<>();
	protected ArrayList<Card> partnerCards = new ArrayList<>(); // known cards of our partner
	protected ArrayList<Card> enemyCards = new ArrayList<>(); // known cards of our enemies
	protected int partnerID, selfID, activePlayerID, enemyLeftID, enemyRightID;
	protected boolean[] partnerOutOfColor = {false,false,false,false};
	protected boolean[] enemyLeftOutOfColor = {false,false,false,false};
	protected boolean[] enemyRightOutOfColor = {false,false,false,false};
	protected int turn;
	protected Trump trump;
	protected boolean geschoben = false;
	protected boolean trumpfGemacht = false;
	
	// common methods for any intelligence
	/**
	 * updates the cards in Hand
	 * @param currHand, Array of card IDs
	 */
	public void setHand(int[] currHand) {
		
		if (currHand.length < 1)
		{
			//TODO: FKaiser Errorhandling? What to do?
			System.out.println("Error - CurrHand is empty");
		}
		
		this.cardsInHand = getCardListByIds(currHand, true);
		// add cards in Hand to knownCards
		for(Card c : this.cardsInHand) {
			this.knownCards.add(new KnownCard(c,selfID,false));
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
		deck = getCardListByIds(currDeck, false);
		
		//add last played card to cardsPlayed and knownCards, update partner cards and enemy cards
		if(!deck.isEmpty()) {
			this.cardsPlayed.add(deck.get(deck.size() - 1));
			this.knownCards.add(new KnownCard(deck.get(deck.size() - 1), activePlayerID, true));
			this.partnerCards.remove(deck.get(deck.size() - 1));
			this.enemyCards.remove(deck.get(deck.size() - 1));
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
	private ArrayList<Card> getCardListByIds(int[] cardIds, boolean sort){
		if(sort) {
			Arrays.sort(cardIds); //sort the Cards on Hand
		}
		ArrayList<Card> list = new ArrayList<>();
		for(int i : cardIds) {
			list.add(new Card(i));
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
				modifier = 0; // shouldn't happen //TODO: FKaiser But what if it happens?
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
					
					if(colorInPlay != trump.getTrumpfColor()) { //check for Untertrumpfen
						if(sequence >= 2) {
							Card secondPlayedCard = deck.get(1);
							if(secondPlayedCard.getColor() == trump.getTrumpfColor()) {
								if(secondPlayedCard.getValue().getTrumpValue() > c.getValue().getTrumpValue()) {
									allowedCards.remove(c);
								}
							}
						}
						
						if(sequence >= 3) {
							Card thirdPlayedCard = deck.get(2);
							if(thirdPlayedCard.getColor() == trump.getTrumpfColor()) {
								if(thirdPlayedCard.getValue().getTrumpValue() > c.getValue().getTrumpValue()) {
									allowedCards.remove(c);
								}
							}
						}
					}
				}
				else if(c.getColor().equals(colorInPlay)) { // similar colour to first card allowed
					allowedCards.add(c);
				}
			}
			
			// only trump cards in the list, but Trump not first card -> allow all cards, except "Untertrumpf"
			if(colorInPlay != trump.getTrumpfColor()) { 
				boolean onlyTrump = true;
				for(Card c : allowedCards) {
					if(c.getColor() != trump.getTrumpfColor()) {
						onlyTrump = false;
					}
				}
				if(onlyTrump) {
					for(Card c : cardsInHand) {
						if(c.getColor() != trump.getTrumpfColor()) {
							allowedCards.add(c);
						}
					}
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
		
		if (wiis.length < 1)
		{
			//TODO FKaiser Errorhandling - What to do?
			System.out.println("Error - wiis is empty");
		}
		
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
			Card c = new Card(originCardID);
			this.knownCards.add(new KnownCard(c, playerID, false));
			boolean partner = (partnerID == playerID);
			if(partner) { partnerCards.add(c); }
			else { enemyCards.add(c); }
			if(folge) {
				for(int i = 1; i<noOfCards; i++) {
					c = new Card(originCardID+i);
					knownCards.add(new KnownCard(c, playerID, false));
					if(partner) { partnerCards.add(c); }
					else { enemyCards.add(c); }
				}
			} else {
				for(int i = 1; i<noOfCards; i++) {
					c = new Card(originCardID+i*10);
					this.knownCards.add(new KnownCard(c, playerID, false));
					if(partner) { partnerCards.add(c); }
					else { enemyCards.add(c); }
				}
			}
		}
		
	}
	
	public void setActivePlayerID(int activeSeatId) {
		this.activePlayerID = activeSeatId;
	}
	
	public void setSelfID(int seatId) {
		this.selfID = seatId;
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
					this.maxCardsInPlay[index] = null;
				} else {
					if(c.getColor() == trump.getTrumpfColor()) { // different logic for Trumpf
						if(this.maxCardsInPlay[index].getValue() == CardValue.UNDER) { // Bauer
							this.maxCardsInPlay[index] = new Card(c.getId()-2); // set to Nell
						} else if (this.maxCardsInPlay[index].getValue() == CardValue.NEUN){ // Nell
							this.maxCardsInPlay[index] = new Card(c.getId()+5); // set to Ass
						} else {
							this.maxCardsInPlay[index] = new Card(c.getId()-1);
							if(this.maxCardsInPlay[index].getValue() == CardValue.UNDER || this.maxCardsInPlay[index].getValue() == CardValue.NEUN) {
								this.maxCardsInPlay[index] = new Card(c.getId()-1);
							}
						}
					} else {
						this.maxCardsInPlay[index] = new Card(c.getId()-1);
					}
				}
			}
		}
	}
	
	/**
	 * update lists to keep track of colors not available to partner/enemies
	 * @param deck
	 */
	public void updateOutOfCardLists(int[] deckIDs) {
		if(deckIDs.length>1) { // only makes sense after the first card
			Card firstCard = new Card(deckIDs[0]);
			Card lastCard = new Card(deckIDs[deckIDs.length-1]);
			if(firstCard.getColor() == trump.getTrumpfColor()) { // first card is trump
				if(firstCard.getColor() != lastCard.getColor()) {
					if(this.activePlayerID == partnerID) {
						this.partnerOutOfColor[firstCard.getColor().getId()-1] = true;
					} else if (this.activePlayerID == this.enemyLeftID) {
						this.enemyLeftOutOfColor[firstCard.getColor().getId()-1] = true;
					} else if (this.activePlayerID == this.enemyRightID) {
						this.enemyRightOutOfColor[firstCard.getColor().getId()-1] = true;
					}
				}
			} else { // first card is not trump, but trump is always allowed
				if((firstCard.getColor() != lastCard.getColor()) && (lastCard.getColor() != trump.getTrumpfColor())) {
					if(this.activePlayerID == this.partnerID) {
						this.partnerOutOfColor[firstCard.getColor().getId()-1] = true;
					} else if (this.activePlayerID == this.enemyLeftID) {
						this.enemyLeftOutOfColor[firstCard.getColor().getId()-1] = true;
					} else if (this.activePlayerID == this.enemyRightID) {
						this.enemyRightOutOfColor[firstCard.getColor().getId()-1] = true;
					}
				}
			}
		}
		
	}
	
	public void setEnemyLeftID(int leftEnemyID) {
		this.enemyLeftID = leftEnemyID;
	}
	
	public void setEnemyRightID(int rightEnemyID) {
		this.enemyRightID = rightEnemyID;
		
	}
	
	public void setGeschoben(boolean g) {
		this.geschoben = g;
	}
	
	public void setTrumpfGemacht(boolean t) {
		this.trumpfGemacht = t;
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
