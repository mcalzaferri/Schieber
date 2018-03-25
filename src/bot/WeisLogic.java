package bot;

import java.util.ArrayList;
import java.util.Arrays;

import javax.print.attribute.IntegerSyntax;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.Trump;
import shared.Weis;
import shared.WeisType;

public class WeisLogic {
	
	private ArrayList<Card> cardsInHand;
	private int[] cardIds; // advantage: easily sortable
	private Trump trump;
	private ArrayList<Weis> weise;
	
	public WeisLogic(ArrayList<Card> cardsInHand, Trump trump) {
		this.cardsInHand = cardsInHand;
		cardIds = cardsToIds(cardsInHand);
		Arrays.sort(cardIds);
		this.trump = trump;
		weise = new ArrayList<>();
	}
	
	public ArrayList<Weis> getWeise(){
		
		// St�ck?
		if(checkStoeck()) {weise.add(new Weis(WeisType.STOECK,new Card(trump.getTrumpfColor(),CardValue.OBER),trump));}
		
		// 4 Gliche?
		for(CardValue cv : check4Gliche()) {
			if(cv == CardValue.UNDER) {
				weise.add(new Weis(WeisType.VIERBAUERN, new Card(CardColor.EICHEL,CardValue.UNDER),trump));
			} else if(cv == CardValue.NEUN) {
				weise.add(new Weis(WeisType.VIERNELL, new Card(CardColor.EICHEL,CardValue.NEUN),trump));
			} else {
				weise.add(new Weis(WeisType.VIERGLEICHE, new Card(CardColor.EICHEL,cv),trump));
			}
		}
		
		// Blattfolgen?
		int noOfCards = 1;
		boolean last = false;
		for(int i = 1; i<10; i++) {
			if(i==9) {
				last = true;	// final round for checking
			}
			if(!last && (cardIds[i]-cardIds[i-1])==1) { //consecutive cards, suppress check of non-existent index
				noOfCards++;
			} else {
				Card card;
				card = Card.getCardById(cardIds[i-noOfCards]);
				WeisType weisType;
				switch(noOfCards) {
				case 3: 
					weisType = WeisType.DREIBLATT;
					weise.add(new Weis(weisType, card, trump));
					break;
				case 4:
					weisType = WeisType.VIERBLATT;
					weise.add(new Weis(weisType, card, trump));
					break;
				case 5:
					weisType = WeisType.FUENFBLATT;
					weise.add(new Weis(weisType, card, trump));
					break;
				case 6:
					weisType = WeisType.SECHSBLATT;
					weise.add(new Weis(weisType, card, trump));
					break;
				case 7:
					weisType = WeisType.SIEBENBLATT;
					weise.add(new Weis(weisType, card, trump));
					break;
				case 8:
					weisType = WeisType.ACHTBLATT;
					weise.add(new Weis(weisType, card, trump));
					break;
				case 9:
					weisType = WeisType.NEUNBLATT;
					weise.add(new Weis(weisType, card, trump));
					break;
				default:
				}
				noOfCards = 1;
			}
		}
		
		return weise;
		
	}
	
	/**
	 * Function returns if we have the St�ck
	 * @return true if St�ck are available
	 */
	private boolean checkStoeck() {
		int stoeckCards = 0;
		for(Card c : cardsInHand) {
			if(c.getColor().equals(trump.getTrumpfColor())) {
				if(c.getValue().equals(CardValue.OBER) || c.getValue().equals(CardValue.KOENIG)) {
					stoeckCards++;
				}
			}
		}
		return(stoeckCards == 2);
	}
	
	/**
	 * Function returns all 4 Gliche on Hand
	 * @return List of CardValue where we have all 4
	 */
	private ArrayList<CardValue> check4Gliche() {
		int[] noOfCards = new int[9];
		ArrayList<CardValue> vierGliche = new ArrayList<>();
		for(int i = 0; i < 9; i++) {
			noOfCards[cardsInHand.get(i).getId()%10]++;
		}
		for(int i = 3; i < 9; i++) { // ignore 6,7,8 Card values
			if(noOfCards[i] == 4){
				vierGliche.add(Card.getCardById(i).getValue());
			}
		}
		return vierGliche;	
	}
	
	/**
	 * auxiliary function to convert Card list to Array of IDs
	 * @param List of Card
	 * @return Integer Array of IDs
	 */
	private int[] cardsToIds(ArrayList<Card> cards) {
		int[] ids = new int[cards.size()];
		for(int i=0; i<cards.size();i++) {
			ids[i] = cards.get(i).getId();
		}
		return ids;
	}

}
