package bot;

import java.util.ArrayList;

import shared.Card;
import shared.CardValue;
import shared.Trump;
import shared.Weis;
import shared.WeisType;

public class WeisLogic {
	
	private ArrayList<Card> cardsInHand;
	private Trump trump;
	private ArrayList<Weis> weise;
	
	public WeisLogic(ArrayList<Card> cardsInHand, Trump trump) {
		this.cardsInHand = cardsInHand;
		this.trump = trump;
		weise = new ArrayList<>();
	}
	
	public ArrayList<Weis> getWeise(){
		if(checkStoeck()) {weise.add(new Weis(WeisType.STOECK,new Card(trump.getTrumpfColor(),CardValue.OBER),trump));}
		return weise;
		
	}
	
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

}
