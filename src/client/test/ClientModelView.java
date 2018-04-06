package client.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import shared.Card;
import shared.Player;
import shared.client.ClientModel;

public class ClientModelView extends JFrame{
	private static final long serialVersionUID = -7761420473524430597L;
	ClientModel model;
	JPanel mainFrame;
	Player[] players;
	Timer updateTimer;
	public ClientModelView(ClientModel model, Player[] players) {
		super();
		this.model = model;
		this.players = players;
		initialComponents();
		this.add(mainFrame);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
		updateTimer.start();
	}
	
	private void initialComponents() {
		mainFrame = new JPanel();
		mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
		updateTimer = new Timer(500, null); //will fire events every 500ms
		
		initialThisPlayer();
		initialHand();
	}
	
	private void initialThisPlayer() {
		PlayerPanel thisPlayerPanel = new PlayerPanel("thisPlayer", players);
		thisPlayerPanel.setEditable(false);
		mainFrame.add(thisPlayerPanel);
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				thisPlayerPanel.setPlayer(model.getThisPlayer());
			}
		});
	}
	private void initialHand() {
		CardPanel[] cardPanels = new CardPanel[9];
		for(int i = 0; i < 9; i++) {
			cardPanels[i] = new CardPanel("Card#" + i);
			cardPanels[i].setEditable(false);
			mainFrame.add(cardPanels[i]);
		}
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getHand() != null) {
					for(int i = 0; i < 9; i++) {
						cardPanels[i].setCard(model.getHand().get(i));
					}
				}else {
					for(int i = 0; i < 9; i++) {
						cardPanels[i].setCard(null);
					}
				}
				
			}
		});
	}
	
	private void initialDeck() {
		CardPanel[] cardPanels = new CardPanel[4];
		for(int i = 0; i < 4; i++) {
			cardPanels[i] = new CardPanel("Deck#" + i);
			cardPanels[i].setEditable(false);
			mainFrame.add(cardPanels[i]);
		}
		//TODO
	}
	
	
}
