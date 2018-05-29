package gui.playingView;

import java.awt.BorderLayout;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import javax.swing.JFrame;

import client.ViewEnumeration;
import client.ViewObserver;
import gui.ClientModelTest;
import shared.Card;
import shared.Seat;
import shared.Trump;

public class HandPaneTest extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5521072659054537868L;
	private HandPane handPane; 
	private ClientModelTest data;
	public static void main(String[] args) {
		new HandPaneTest();
	}
	
	public HandPaneTest() {
		data = new ClientModelTest();
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
				data.getHand().remove(card.getId());
				handPane.update();
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

			@Override
			public void playViewClick() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnFillWithBots() {
				// TODO Auto-generated method stub
				
			}
			
		});

		this.handPane = new HandPane(data, obs);
		handPane.update();
		this.setSize(800,200);
		this.add(handPane, BorderLayout.CENTER);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
