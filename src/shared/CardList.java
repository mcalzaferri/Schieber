package shared;

import java.util.ArrayList;

public class CardList extends ArrayList<Card>{
	//Fields
	private static final long serialVersionUID = 6000012260383619668L;
	//Constructors
	
	//Methods
	public void setData(int[] cardIds) {
		super.clear();
		for(int id : cardIds) {
			this.add(id);
		}
	}

	public void updateData(int[] cardIds) {
		//Remove all non existent cards
		boolean exists;
		for(Card card : this) {
			exists = false;
			for(int id : cardIds) {
				if(id == card.getId()) {
					exists = true;
					continue;
				}
			}
			if(!exists)
				super.remove(card);
		}
		//Add all new cards
		for(int id : cardIds) {
			if(!contains(id)) {
				this.add(id);
			}
		}
	}
	
	@Override
	public Card remove(int cardId) {
		for(Card card : this) {
			if(cardId == card.getId()) {
				super.remove(card);
				return card;
			}
		}
		return null;
	}

	public boolean contains(int cardId) {
		for(Card card : this) {
			if(cardId == card.getId())
				return true;
		}
		return false;
	}
	
	public void add(int cardId) {
		if(!contains(cardId))
			super.add(Card.getCardById(cardId));
	}
	
	public Card highest(Card firstCard, Trump trump) {
		Card highest = firstCard;
		for(Card card : this) {
			if(highest.compareTo(card, trump) > 0) {
				highest = card;
			}
		}
		return highest;
	}
	//Getters and setters
}
