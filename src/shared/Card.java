package shared;

public class Card {
	//Fields
	private CardColor color;
	private CardValue value;

	//Constructors
	public Card(CardColor c, CardValue v) {
		this.color = c;
		this.value = v;
	}

	//Methods
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Card) {
			Card card = (Card)obj;
			return (this.color == card.color && this.value == card.value);
		}
		return super.equals(obj);
	}

	public static Card getCardById(int cardId) {
		int colorId = cardId/10;
		int valueId = cardId%10;
		return new Card(CardColor.getColorById(colorId),CardValue.getValueById(valueId));
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
