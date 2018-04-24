package shared;

import ch.ntb.jass.common.entities.CardEntity;

public class Card{
	//Fields
	private final CardColor color;
	private final CardValue value;
	private final CardEntity entity;

	//Constructors
	public Card(CardColor color, CardValue value) {
		this.color = color;
		this.value = value;
		entity = CardEntity.getById(getId());
	}
	
	public Card(int cardId) {
		this(CardColor.getById(getColorId(cardId)),CardValue.getById(getValueId(cardId)));
	}
	
	/** Use this constructor to cast a CardEntity into a Card
	 * @param entity The entity to be cast
	 */
	public Card(CardEntity entity) {
		this(entity.calcId());
	}
	
	//Static methods
	/** Use this method to cast an Array of CardEntities into an Array of Cards
	 * @param cards the Array to be cast
	 * @return The newly created Card Array
	 */
	public static CardEntity[] getEntities(Card[] cards) {
		CardEntity[] ce;
		if(cards != null) {
			ce = new CardEntity[cards.length];
			for(int i = 0; i < cards.length; i++){
				ce[i] = cards[i].getEntity();
			}
		}else {
			ce = null;
		}
		return ce;
	}
	
	//Methods
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Card) {
			Card card = (Card)obj;
			return (this.color == card.color && this.value == card.value);
		}else if(obj instanceof CardEntity) {
			return this.getId() == ((CardEntity)obj).calcId();
		}
		return super.equals(obj);
	}

	/** @deprecated Use constructor instead
	 */
	@Deprecated
	public static Card getCardById(int cardId) {
		return new Card(cardId);
	}
	
	/** Use this method to get the underlying entity of this class
	 * @return An entity representing this class
	 */
	public CardEntity getEntity() {
		return entity;
	}

	/**Compares this card to another. 
	 * The method will always return 1 if the card is from another color unless that color is trump
	 * @param card The card which this is compared to
	 * @return <0 if less, =0 if equal >0 if greater then card
	 */
	public int compareTo(Card card, Trump trump) {
		switch(trump) {
		case SCHILTE:
		case ROSE:
		case EICHEL:
		case SCHELLE:
			if(this.color == trump.getTrumpfColor()) {
				if(card.color == trump.getTrumpfColor() && card.value.getTrumpValue() > this.value.getTrumpValue()) {
					return -1;
				}
				return 1;
			}else if(card.color == trump.getTrumpfColor()) {
				return -1;
			}
			//No trump -> handle the same way as Obenabe
		case OBENABE:
			if(this.color == card.color)
				return this.value.compareTo(card.value);
			else
				return 1;
		case UNEUFE:
			if(this.color == card.color)
				return this.value.compareTo(card.value) * -1;
			else
				return 1;
		default:
				return 0;
		}
	}
	
    /**
     * @param Decomposes a card ID into the ID of the color.
     * @return The ID representative of the color.
     */
    private static int getColorId(int cardId) {return cardId/10;}

    /**
     * @param Decomposes a card ID into the ID of the value.
     * @return The ID representative of the value of this card.
     */
    private static int getValueId(int cardId) {return cardId%10;}

    /** Checks if this card is allowed to be played in the current configuration of the players hand, the first card that was played and the current trump
     * @param hand The current hand the player is holding. The card which is checked must be one of these cards.
     * @param firstCardOnDeck The first card which was played onto the deck
     * @param trump Current trump
     * @return True if this card is allowed to be played.
     */
    public boolean isAllowed(Card[] hand, Card firstCardOnDeck, Trump trump) {
    	//First handle null values
    	if(hand == null || trump == null)
    		return false; //Should not occur maybe throw error here
    	else if(firstCardOnDeck == null)
    		return true; //If there is no card on the deck all cards are allowed
    	//Now handle all other cases
    	
    	if(firstCardOnDeck.getColor() == this.getColor())
    		return true; //Same colored cards are always allowed. Nothing to explain here
    	if(trump.getGameMode() == GameMode.TRUMPF && trump.getTrumpfColor() == this.getColor())
    		return true; //Trump is always allowed. Nothing to explain here

    	//Now the cases where this card is of different color and is only allowed if there is no card of the same color in the players hand (Except for buur)
    	for(Card card : hand) {
    		if(card.getColor() == firstCardOnDeck.getColor() && !card.isBuur(trump)) {
    			return false; //There are other cards in the players hand which he has to play first.
    		}
    	}
    	//Default case if the player has no other card he is forced to play before this one.
    	return true;
    }

	/** Checks if this card is the buur (This is usefull as the buur must be handled differently as any other card)
	 * @param trump The trump which is active
	 * @return True if this card is of value UNDER and the same color as the trumpf
	 */
	public boolean isBuur(Trump trump) {
		if(trump != null && trump.getGameMode() == GameMode.TRUMPF && trump.getTrumpfColor() == this.getColor() && this.getValue() == CardValue.UNDER)
			return true;
		return false;
	}
    
	//Getter and Setter
	public CardValue getValue() {
		return value;
	}
	
	public CardColor getColor() {
		return color;
	}

	public int getId() {
		return color.getId() * 10 + value.getId();
	}

	@Override
	public String toString() {
		return color + " " + value;
	}
}
