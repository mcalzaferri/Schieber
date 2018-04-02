package shared;

import ch.ntb.jass.common.entities.CardColorEntity;
import ch.ntb.jass.common.entities.CardEntity;
import ch.ntb.jass.common.entities.CardValueEntity;

public class Card extends CardEntity{
	//Fields
	private final CardColor color;
	private final  CardValue value;

	//Constructors
	public Card(CardColor c, CardValue v) {
		this.color = c;
		this.value = v;
		super.color = CardColorEntity.getById(getColorId(getId()));
		super.value = CardValueEntity.getById(getValueId(getId()));
	}
	
	public Card(int cardId) {
		this(CardColor.getColorById(getColorId(cardId)),CardValue.getValueById(getValueId(cardId)));
	}
	
	public Card(CardEntity entity) {
		this(CardEntity.getId(entity));
	}

	//Methods
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Card) {
			Card card = (Card)obj;
			return (this.color == card.color && this.value == card.value);
		}else if(obj instanceof CardEntity) {
			return this.getId() == CardEntity.getId((CardEntity)obj);
		}
		return super.equals(obj);
	}

	/** @deprecated Use constructor instead
	 */
	@Deprecated
	public static Card getCardById(int cardId) {
		return new Card(cardId);
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
