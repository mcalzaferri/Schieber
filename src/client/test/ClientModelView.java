package client.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import shared.Player;
import shared.Trump;
import shared.Weis;
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
		initialPlayers();
		initialHand();
		initialDeck();
		initialTrump();
		initialPossibleWiis();
	}
	
	@SuppressWarnings("unused")
	private void initialTemplate() {
		JLabel label = new JLabel("Test");
		label.setVisible(true);
		mainFrame.add(label);
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initialThisPlayer() {
		PlayerPanel thisPlayerPanel = new PlayerPanel("thisPlayer", players);
		thisPlayerPanel.setEnabled(false);
		mainFrame.add(thisPlayerPanel);
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				thisPlayerPanel.setPlayer(model.getThisPlayer());
			}
		});
	}
	
	private void initialPlayers() {
		JPanel panel = new JPanel();
		JPanel playerPanel = new JPanel();
		playerPanel.setVisible(true);
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
		JLabel nullLabel = new JLabel("            null            ");
		nullLabel.setVisible(false);
		JLabel emptyLabel = new JLabel("            empty            ");
		emptyLabel.setVisible(false);
		panel.add(nullLabel);
		panel.add(emptyLabel);
		panel.add(playerPanel);
		panel.setBorder(BorderFactory.createTitledBorder("Players"));
		ArrayList<PlayerPanel> playerPanels = new ArrayList<>();
		mainFrame.add(panel);
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getPlayers() == null) {
					playerPanel.setVisible(false);
					nullLabel.setVisible(true);
					emptyLabel.setVisible(false);
				}else if(model.getPlayers().isEmpty()) {
					playerPanel.setVisible(false);
					nullLabel.setVisible(false);
					emptyLabel.setVisible(true);
				}else{
					//Resize playerPanels array
					int dif = model.getPlayers().size() - playerPanels.size();
					for(int i = 0; i > dif; i--) {
						PlayerPanel panelToRemove = playerPanels.get(playerPanels.size() - 1);
						playerPanels.remove(panelToRemove);
						playerPanel.remove(panelToRemove);
					}
					for(int i = 0; i < dif; i++) {
						PlayerPanel panelToAdd = new PlayerPanel("Player#" + playerPanels.size(), players);
						playerPanels.add(panelToAdd);
						playerPanel.add(panelToAdd);
					}
					//End of resize operation
					//Display all players
					int i = 0;
					for(Player player : model.getPlayers().values()) {
						playerPanels.get(i).setPlayer(player);
						playerPanels.get(i).setEnabled(false);
						i++;
					}
					playerPanel.setVisible(true);
					nullLabel.setVisible(false);
					emptyLabel.setVisible(false);
				}
				
			}
		});
	}
	
	private void initialHand() {
		CardPanel[] cardPanels = new CardPanel[9];
		for(int i = 0; i < cardPanels.length; i++) {
			cardPanels[i] = new CardPanel("Card#" + i);
			cardPanels[i].setEnabled(false);
			mainFrame.add(cardPanels[i]);
		}
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getHand() != null) {
					for(int i = 0; i < cardPanels.length; i++) {
						cardPanels[i].setCard(model.getHand().get(i));
					}
				}else {
					for(int i = 0; i < cardPanels.length; i++) {
						cardPanels[i].setCard(null);
					}
				}
				
			}
		});
	}
	private void initialDeck() {
		CardPanel[] cardPanels = new CardPanel[4];
		for(int i = 0; i < cardPanels.length; i++) {
			cardPanels[i] = new CardPanel("Deck#" + i);
			cardPanels[i].setEnabled(false);
			mainFrame.add(cardPanels[i]);
		}
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getDeck() != null) {
					for(int i = 0; i < cardPanels.length; i++) {
						cardPanels[i].setCard(model.getDeck().get(i));
					}
				}else {
					for(int i = 0; i < cardPanels.length; i++) {
						cardPanels[i].setCard(null);
					}
				}
				
			}
		});
	}
	private void initialTrump() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Trumpf"));
		JComboBox<Trump> trumpComboBox = new JComboBox<>(Trump.values());
		trumpComboBox.setEditable(false);
		trumpComboBox.setVisible(true);
		panel.add(trumpComboBox);
		JLabel nullLabel = new JLabel("            null            ");
		nullLabel.setVisible(false);
		panel.add(nullLabel);
		mainFrame.add(panel);
		
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Trump trump = model.getTrump();
				if(trump != null) {
					nullLabel.setVisible(false);
					trumpComboBox.setVisible(true);
					trumpComboBox.setSelectedItem(trump);
				}else {
					nullLabel.setVisible(true);
					trumpComboBox.setVisible(false);
				}
				
			}
		});
	}
	
	private void initialPossibleWiis() {
		JPanel panel = new JPanel();
		JPanel wiisPanel = new JPanel();
		wiisPanel.setVisible(true);
		wiisPanel.setLayout(new BoxLayout(wiisPanel, BoxLayout.Y_AXIS));
		JLabel nullLabel = new JLabel("            null            ");
		nullLabel.setVisible(false);
		JLabel emptyLabel = new JLabel("            empty            ");
		emptyLabel.setVisible(false);
		panel.add(nullLabel);
		panel.add(emptyLabel);
		panel.add(wiisPanel);
		panel.setBorder(BorderFactory.createTitledBorder("possibleWiis"));
		ArrayList<WeisPanel> wiisPanels = new ArrayList<>();
		mainFrame.add(panel);
		updateTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getPossibleWiis() == null) {
					nullLabel.setVisible(true);
					emptyLabel.setVisible(false);
					wiisPanel.setVisible(false);
				}else if(model.getPossibleWiis().length == 0) {
					nullLabel.setVisible(false);
					emptyLabel.setVisible(true);
					wiisPanel.setVisible(false);
				}else {
					//Resize wiisPanels array
					int dif = model.getPossibleWiis().length - wiisPanels.size();
					for(int i = 0; i > dif; i--) {
						WeisPanel panelToRemove = wiisPanels.get(wiisPanels.size() - 1);
						wiisPanels.remove(panelToRemove);
						wiisPanel.remove(panelToRemove);
					}
					for(int i = 0; i < dif; i++) {
						WeisPanel panelToAdd = new WeisPanel("Weis#" + wiisPanels.size());
						wiisPanels.add(panelToAdd);
						wiisPanel.add(panelToAdd);
					}
					//End of resize operation
					//Display all wiis
					int i = 0;
					for(Weis weis : model.getPossibleWiis()) {
						wiisPanels.get(i).setWeis(weis);
						wiisPanels.get(i).setEnabled(false);
						i++;
					}
					wiisPanel.setVisible(true);
					nullLabel.setVisible(false);
					emptyLabel.setVisible(false);
				}
			}
		});
	}
	
}
