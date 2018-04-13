package gui.playingView;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.Seat;
import shared.ServerAddress;
import shared.Trump;

public class ViewableCardTest extends JFrame{
	public static void main(String[] args) {
		ViewableCardTest t = new ViewableCardTest();
	}
	
	public ViewableCardTest() {
		Card c = new Card(CardColor.ROSE, CardValue.ACHT);
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
				System.out.println("Clicked");
				
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
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ViewableCard vc = null;
		try {
			vc = new ViewableCard(c, obs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setSize(800, 800);
		this.setLayout(new BorderLayout());
		this.add(vc, BorderLayout.CENTER);
		this.setVisible(true);
	}
}
