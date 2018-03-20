package gui.playingView;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.Card;
import shared.CardColor;
import shared.CardValue;
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
				System.out.println("Clicked");
				
			}

			@Override
			public void btnCloseWindowClick(ViewEnumeration view) {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ViewableCard vc = new ViewableCard(c, obs);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(vc);
		this.setSize(800, 800);
		this.add(panel, BorderLayout.CENTER);
		this.setVisible(true);
	}
}
