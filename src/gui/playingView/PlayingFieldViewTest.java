package gui.playingView;

import java.util.ArrayList;
import java.util.Random;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.Card;
import shared.CardColor;
import shared.CardList;
import shared.CardValue;
import shared.ServerAddress;
import shared.Trump;
import shared.client.ClientModel;

public class PlayingFieldViewTest {
	
	private PlayingFieldView pfv;
	private ClientModel data;
	
	public static void main(String[] args) {
		PlayingFieldViewTest t = new PlayingFieldViewTest();
	}
	
	public PlayingFieldViewTest() {
		this.data = new ClientModel();
		
		
		CardList cards = new CardList();
		for(int i = 0; i < 6; i++) {			
			cards.add(this.createRandomCard());
		}
		ArrayList<ViewObserver> obs = new ArrayList<>();
		obs.add(new ViewObserver() {

			@Override
			public void btnConnectClick(ServerAddress serverAddress) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnRestartClick() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnDisconnectClick() {
				// TODO Auto-generated method stub				
			}

			@Override
			public void btnTrumpClick(Trump trump) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnCardClick(Card card) {
				System.out.println(card.getColor().toString() + card.getValue());			
				cards.remove(card.getId());
				cards.add(createRandomCard());
				pfv.update();
			}
			
		});
		this.data.setHand(cards);
		data.setDeck(cards);
		this.pfv = new PlayingFieldView(ViewEnumeration.PLAYVIEW, obs, data);
	}
	private Card createRandomCard() {
		Random r = new Random();
		return new Card(CardColor.getColorById(r.nextInt(4) + 1), CardValue.getValueById(r.nextInt(8) + 1));
	}
}