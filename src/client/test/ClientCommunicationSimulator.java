package client.test;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.util.ArrayList;

import javax.swing.*;

import client.ClientCommunication;
import client.ClientController;
import shared.Card;
import shared.ServerAddress;
import shared.Trump;
import shared.Weis;
import shared.client.AbstractClient;

public class ClientCommunicationSimulator extends ClientCommunication {
	private AbstractClient client;
	private JFrame fenster;
	private JPanel mainFrame;
	
	private SimulationRow chosenGameModeInfo;
	private SimulationRow endOfRound;
	private SimulationRow gameOver;
	private SimulationRow gameStartedInfo;
	private SimulationRow newRoundInfo;
	private SimulationRow playerJoinedInfo;
	private SimulationRow playerLeftInfo;
	private SimulationRow playerLeftTableInfo;
	private SimulationRow turnInfo;
	private SimulationRow chooseGameMode;
	private SimulationRow handOutCards;
	private SimulationRow wrongCard;
	private SimulationRow yourTurn;
	
	private TextArea outputArea;
	
	public ClientCommunicationSimulator() {
		initialComponents();
		addListeners();
	}
	
	//Methods
	public void initialComponents() {
		fenster = new JFrame();
		fenster.setMinimumSize(new Dimension(1000, 1000));
		fenster.setPreferredSize(new Dimension(1000, 10000));
		int rows = 14;
		int columns = 1;
		mainFrame = new JPanel(new GridLayout(rows, columns));
		
		chosenGameModeInfo = new SimulationRow("Recieve ChosenGameModeInfoMessage");
		endOfRound = new SimulationRow("Recieve EndOfRoundMessage");
		gameOver = new SimulationRow("Recieve GameOverMessage");
		gameStartedInfo = new SimulationRow("Recieve GameStartedInfoMessage");
		newRoundInfo = new SimulationRow("Recieve NewRoundInfoMessage");
		playerJoinedInfo = new SimulationRow("Recieve PlayerJoinedInfoMessage");
		playerLeftInfo = new SimulationRow("Recieve PlayerLeftInfoMessage");
		playerLeftTableInfo = new SimulationRow("Recieve PlayerLeftTableInfoMessage");
		turnInfo = new SimulationRow("Recieve TurnInfoMessage");
		chooseGameMode = new SimulationRow("Recieve ChooseGameModeMessage");
		handOutCards = new SimulationRow("Recieve HandOutCardsMessage");
		wrongCard = new SimulationRow("Recieve WrongCardMessage");
		yourTurn = new SimulationRow("Recieve YourTurnMessage");
		
		outputArea = new TextArea(10, 100);
		outputArea.setEditable(false);
		
		mainFrame.add(chosenGameModeInfo);
		mainFrame.add(endOfRound);
		mainFrame.add(gameOver);
		mainFrame.add(gameStartedInfo);
		mainFrame.add(newRoundInfo);
		mainFrame.add(playerJoinedInfo);
		mainFrame.add(playerLeftInfo);
		mainFrame.add(playerLeftTableInfo);
		mainFrame.add(turnInfo);
		mainFrame.add(chooseGameMode);
		mainFrame.add(handOutCards);
		mainFrame.add(wrongCard);
		mainFrame.add(yourTurn);
		mainFrame.add(outputArea);
		
	
		fenster.add(mainFrame);
		fenster.pack();
		fenster.setVisible(true);
	}

	public void addListeners() {
		chosenGameModeInfo.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				Trump trump = Trump.SCHIEBEN;
				try {
					int id = Integer.parseInt(labelText);
					trump = Trump.getById(id);
				}catch(Exception e) {}
				client.doSetTrump(trump);
			}
		});
		endOfRound.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				client.doEndRound();
			}
		});
		gameOver.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				client.doEndGame(null);//TODO
			}
		});
		gameStartedInfo.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				client.doUpdateActiveSeat(1);//TODO Redundant with newRound?!
			}
		});
		newRoundInfo.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				client.doSetSeat(0);
				client.doUpdateActiveSeat(1);//TODO What is it good for?
			}
		});
		playerJoinedInfo.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				// TODO Implent something in AbstractClient to handle this.
			}
		});
		playerLeftInfo.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				// TODO Implent something in AbstractClient to handle this.
			}
		});
		playerLeftTableInfo.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				// TODO Implent something in AbstractClient to handle this.
			}
		});
		turnInfo.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				// TODO Implent something in AbstractClient to handle this.
			}
		});
		chooseGameMode.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				boolean canSwitch = false;
				try {
					canSwitch = Boolean.parseBoolean(labelText);
				}catch(Exception e) {}
				client.doRequestTrump(canSwitch);
			}
		});
		handOutCards.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				ArrayList<Card> cardList = new ArrayList<>();
				String[] params = labelText.split(",");
				for(String param : params) {
					try {
						cardList.add(Card.getCardById(Integer.parseInt(param)));
					}catch(Exception e) {}
				}
				Card[] cardArray = new Card[cardList.size()];
				cardArray = cardList.toArray(cardArray);
				client.doUpdateHand(cardArray);
			}
		});
		wrongCard.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				// TODO Implent something in AbstractClient to handle this.
			}
		});
		yourTurn.setSimulationRowListener(new SimulationRowListener() {
			@Override
			public void actionPerformed(String labelText) {
				// TODO Implent something in AbstractClient to handle this better.
				client.doSetSeat(1);
				client.doUpdateActiveSeat(1);
			}
		});
	}
	
	//Override methods from ClientCommunication
	@Override
	public void publishChosenCard(Card card) {
		outputArea.append("publishChosenCard() with: " + card + "\n");
	}
	@Override
	public void publishChosenTrump(Trump trump) {
		outputArea.append("publishChosenTrump() with: " + trump + "\n");
	}
	@Override
	public void publishChosenWiis(Weis[] wiis) {
		StringBuilder sb = new StringBuilder();
		for(Weis w : wiis) {
			sb.append(w + ", ");
		}
		outputArea.append("publishChosenTrump() with: " + sb + "\n");
	}
	@Override
	public void disconnect() {
		outputArea.append("disconnect()" + "\n");
	}
	@Override
	public void connect(ServerAddress serverAddress) {
		outputArea.append("connect() with: " + serverAddress + "\n");
	}

	//Getters and Setter
	public void setClient(AbstractClient client) {
		this.client = client;
	}
}
