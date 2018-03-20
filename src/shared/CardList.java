package shared;

import java.util.ArrayList;
import java.util.Comparator;

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
		ArrayList<Card> cardsToDelete = new ArrayList<>();
		for(Card card : this) {
			exists = false;
			for(int id : cardIds) {
				if(id == card.getId()) {
					exists = true;
					continue;
				}
			}
			if(!exists)
				cardsToDelete.add(card);
		}
		super.removeAll(cardsToDelete);
		//Add all new cards
		for(int id : cardIds) {
			if(!contains(id)) {
				this.add(id);
			}
		}
	}
	
	public void sort() {
		super.sort(new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				return o1.compareTo(o2, Trump.OBENABE);
			}
		});
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
