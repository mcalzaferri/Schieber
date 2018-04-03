package gui;

import java.util.Random;

import shared.Card;
import shared.CardColor;
import shared.CardList;
import shared.CardValue;
import shared.client.ClientModel;

public class ClientModelTest extends ClientModel{
	public ClientModelTest() {
		CardList cards = new CardList();
		for(int i = 0; i < 6; i++) {			
			cards.add(this.createRandomCard());
		}
		this.setDeck(cards);
		cards = new CardList();
		for(int i = 0; i < 6; i++) {			
			cards.add(this.createRandomCard());
		}
		this.setHand(cards);
	}
	
	private Card createRandomCard() {
		Random r = new Random();
		return new Card(CardColor.getById(r.nextInt(4) + 1), CardValue.getById(r.nextInt(8) + 1));
	}
}
