package shared;

import java.util.ArrayList;
import java.util.Comparator;

import ch.ntb.jass.common.entities.CardEntity;

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

	public void updateData(Card[] cards) {
		//Remove all non existent cards
		boolean exists;
		ArrayList<Card> cardsToDelete = new ArrayList<>();
		for(Card oldCard : this) {
			exists = false;
			for(Card newCard : cards) {
				if(oldCard.equals(newCard)) {
					exists = true;
					continue;
				}
			}
			if(!exists)
				cardsToDelete.add(oldCard);
		}
		super.removeAll(cardsToDelete);
		//Add all new cards
		for(Card card : cards) {
			if(!contains(card)) {
				this.add(card);
			}
		}
	}
	public void updateData(CardEntity[] cards) {
		Card[] array = new Card[cards.length];
		for(int i = 0; i  < cards.length; i++) {
			array[i] = new Card(cards[i]);
		}
		updateData(array);
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
			super.add(new Card(cardId));
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
	
	@Override
	public CardList clone() {
		return (CardList)super.clone();
	}
	
	public Card[] toArray() {
		Card[] array = new Card[size()];
		int i = 0;
		for(Card card : this) {
			array[i] = card;
			i++;
		}
		return array;
	}
	
	/** Searches for possible wiis in this set of cards (This is only practicable if this CardList represents a hand of cards)
	 * @param trump Current trump of the game. Necessary to find Stoeck and return the correct originCard
	 * @return
	 */
	public Weis[] getPossibleWiis(Trump trump) {
		ArrayList<Weis> wiis = new ArrayList<>();
		CardList tempList = this.clone();
		tempList.sort(); //First sort this set of cards for easy handling
		
		int id = 0;
		int sequenceCount = 0;
		int[] sameValueCount = new int[9];
		for(int i = 0; i<tempList.size(); i++) {
			//Remember cards of same Value
			sameValueCount[tempList.get(i).getValue().getId() - 1]++;
			
			//Find card in a sequence
			//If seqenceCount is 0 or id is 0 reinitial with 1
			if(id == 0 || sequenceCount == 0) {
				id = tempList.get(i).getId();
				sequenceCount = 1;
			}else {
				//Otherwise check if the id of this card is the next in a sequence
				if(id + 1 == tempList.get(i).getId()) {
					id = tempList.get(i).getId();
					sequenceCount++;
					//Check for STOECK
					if(sequenceCount == 2 && trump.getGameMode() == GameMode.TRUMPF && trump.getTrumpfColor() == tempList.get(i).getColor() && tempList.get(i).getValue() == CardValue.KOENIG) {
						//STOECK
						wiis.add(new Weis(WeisType.STOECK, tempList.getCardById(id)));
					}
				}else {
					//If the sequence has ended check if the sequence is longer then 3 and therefore is a possible weis
					if(sequenceCount >= 3) {
						if(trump != Trump.UNEUFE) {
							//Return the highest card in case of anything other then uneufe
							wiis.add(new Weis(WeisType.getByCount(sequenceCount), tempList.getCardById(id)));
						}else {
							//Return the lowest card in case of Uneufe
							wiis.add(new Weis(WeisType.getByCount(sequenceCount), tempList.getCardById(id - (sequenceCount -1))));
						}
					}
					sequenceCount = 0;
					id = tempList.get(i).getId();
				}
			}
		}
		//Now check for 4 cards of same value
		for(int i = 0; i < sameValueCount.length; i++) {
			if(sameValueCount[i] == 4) {
				WeisType type = null;
				switch(CardValue.getById(i + 1)) {
				case NEUN:
					type = WeisType.VIERNELL;
					break;
				case UNDER:
					type = WeisType.VIERBAUERN;
					break;
				default:
					type = WeisType.VIERGLEICHE;
					break;
				}
				wiis.add(new Weis(type, tempList.getCardById(i + 10 + 1))); //+ 1 to change from 0 based i to 1 based id and + 10 to get a valid cardId
			}
		}
		
		//Return array of wiis
		Weis[] wiisArray = new Weis[wiis.size()];
		return wiis.toArray(wiisArray);
	}

	//Getters and setters
	/** Returns the card in this CardList with the given id. If there is no such card, null is returned.
	 * @param id The id of the card that shall be returned.
	 * @return The card with the id id or null if there is no card with the given id.
	 */
	public Card getCardById(int id) {
		for(Card card : this) {
			if(card.getId() == id) {
				return card;
			}
		}
		return null; //Return null if card is not in this list
	}
}
