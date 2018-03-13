package shared;

public class Card {
	//Fields
	private CardColor color;
	private CardValue value;
	
	//Constructors
	public Card(CardColor c, CardValue v) {
		color = c;
		value = v;
	}
	
	//Methods
	public Card getCardById(int cardId) {
		int colorId = cardId/10;
		int valueId = cardId%10;
		return new Card(CardColor.getColorById(colorId),CardValue.getValueById(valueId));
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
}
