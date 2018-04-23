package gui.playingView;

import java.awt.BorderLayout;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

import ch.ntb.jass.common.entities.SeatEntity;
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
		super();
		this.data = new ClientModel();
		
		
		CardList cards = new CardList();
		for(int i = 0; i < 6; i++) {			
			cards.add(this.createRandomCard());
		}
		ArrayList<ViewObserver> obs = new ArrayList<>();
		obs.add(new ViewObserver() {

			@Override
			public void btnConnectClick(InetSocketAddress serverAddress, String username) {
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
		
		Map<Integer,Player> players = new HashMap();
		Seat s = Seat.SEAT1;
		SeatEntity se = SeatEntity.SEAT1;
		Seat.setClientSeat(se);
		players.put(0, new Player(null, "This is a lenght test for carpet", Seat.SEAT1, false, false, 0));
		players.put(1, new Player(null, "Sau", Seat.SEAT2, false, false, 0));
		players.put(2, new Player(null, "Arsch", Seat.SEAT3, false, false, 0));
		players.put(3, new Player(null, "Spinner", Seat.SEAT4, false, false, 0));
		data.setPlayers(players);
		
		this.pfv = new PlayingFieldView(data, obs);
		pfv.update();
		setLayout(new BorderLayout());
		add(this.pfv.getContent(), BorderLayout.CENTER);
		for(int i = 0; i < 100; i++) {
			pfv.publish("Go fuck yourself " + i);
		}
		setSize(500, 500);
		pack();
		setVisible(true);
	}
	private Card createRandomCard() {
		Random r = new Random();
		return new Card(CardColor.getById(r.nextInt(4) + 1), CardValue.getById(r.nextInt(8) + 1));
	}
}
