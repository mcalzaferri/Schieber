package gui.playingView;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.Card;
import shared.CardColor;
import shared.CardList;
import shared.CardValue;
import shared.Player;
import shared.Seat;
import shared.ServerAddress;
import shared.Trump;
import shared.client.ClientModel;

public class PlayingFieldViewTest extends JFrame{
	
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
			public void btnConnectClick(InetSocketAddress serverAddress) {
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
				cards.remove(card);
				cards.add(createRandomCard());
				pfv.update();
			}

			@Override
			public void btnCloseWindowClick(ViewEnumeration view) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnWeisActionChosen(boolean allowBroadcast) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnJoinTableClick(Seat preferedSeat) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnChangeStateClick() {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.data.setHand(cards);
		data.setTrump(Trump.EICHEL);
		data.setDeck(cards);

		this.pfv = new PlayingFieldView(data, obs);
		add(this.pfv.getContent());
		setVisible(true);
		setSize(500, 500);
	}
	private Card createRandomCard() {
		Random r = new Random();
		return new Card(CardColor.getById(r.nextInt(4) + 1), CardValue.getById(r.nextInt(8) + 1));
	}
}
