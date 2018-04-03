package gui.playingView;

import java.awt.BorderLayout;
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
import shared.Seat;
import shared.ServerAddress;
import shared.Trump;

public class HandPaneTest extends JFrame{
	private HandPane handPane; 
	private CardList cards;
	public static void main(String[] args) {
		HandPaneTest t = new HandPaneTest();
	}
	
	public HandPaneTest() {
		this.cards = new CardList();
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
				cards.remove(card.getId());
				cards.add(createRandomCard());
				handPane.update();
				repaint();
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

		this.handPane = new HandPane(cards, obs);
		this.setSize(800,200);
		this.add(handPane, BorderLayout.CENTER);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	private Card createRandomCard() {
		Random r = new Random();
		return new Card(CardColor.getById(r.nextInt(4) + 1), CardValue.getById(r.nextInt(8) + 1));
	}
}
