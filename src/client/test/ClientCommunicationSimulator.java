package client.test;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.*;

import ch.ntb.jass.common.entities.*;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import client.ClientCommunication;
import client.shared.AbstractClient;
import client.shared.ClientModel;
import shared.Player;

public class ClientCommunicationSimulator extends ClientCommunication{
	private AbstractClient client;
	private JFrame fenster;
	private JPanel mainFrame;
	private JComboBox<MessageEnumeration> msgComboBox;
	private JPanel msgFrame;
	private JButton btnReceive;
	private JTextArea outputArea;
	
	public ClientCommunicationSimulator() {
		super();
		initialComponents();
	}
	
	//Methods
	public void initialComponents() {
		fenster = new JFrame();
		fenster.setPreferredSize(new Dimension(1000, 1000));
		mainFrame = new JPanel();
		mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
		initialMsgComboBox();
		mainFrame.add(msgComboBox);
		msgFrame = new JPanel(new CardLayout());
		mainFrame.add(msgFrame);
		btnReceive = new JButton("Receive Message");
		initialMsgComponents();
		mainFrame.add(btnReceive);
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		mainFrame.add(outputArea);
		fenster.add(mainFrame);
		fenster.pack();
		fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fenster.setVisible(true);
	}
	
	private void initialMsgComboBox() {
		MessageEnumeration[] msgEnum = MessageEnumeration.values();
		ArrayList<MessageEnumeration> comboBoxItems = new ArrayList<>();
		for(MessageEnumeration me : msgEnum) {
			if(me.getType() == MessageType.ToPlayerMessage || me.getType() == MessageType.InfoMessage) {
				comboBoxItems.add(me);
			}
		}
		MessageEnumeration[] comboBoxItemsArray = new MessageEnumeration[comboBoxItems.size()];
		comboBoxItemsArray = comboBoxItems.toArray(comboBoxItemsArray);
		msgComboBox = new JComboBox<>(comboBoxItemsArray);
		msgComboBox.setEditable(false);
		msgComboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
				CardLayout cl = (CardLayout)msgFrame.getLayout();
				cl.show(msgFrame, ((MessageEnumeration) e.getItem()).toString());
				}catch(ClassCastException ex) {
					System.out.println("This Message doesnt exist");
				}
			}
		});
	}

	private void initialMsgComponents() {
		//server_info_messages
		initialChosenTrumpInfoMsg();
		initialEndOfRoundInfoMessage();
		initialGameStartedInfoMessage();
		initialNewRoundInfoMessage();
		initialNewTurnInfoMessage();
		initialPlayerChangedStateMessage();
		initialPlayerLeftLobbyInfoMessage();
		initialPlayerMovedToLobbyInfoMessage();
		initialPlayerMovedToTableInfoMessage();
		initialStichInfoMessage();
		initialTurnInfoMessage();
		initialWiisInfoMessage();
		//server_messages
		initialChooseTrumpMessage();
		initialGameStateMessage();
		initialHandOutCardsMessage();
		//initialLobbyStateMessage();
		initialWrongCardMessage();
	}
	//server_info_messages
	private void initialChosenTrumpInfoMsg() {
		JComboBox<TrumpEntity> trumpComboBox = new JComboBox<>(TrumpEntity.values());
		trumpComboBox.setEditable(true);
		JPanel panel = new JPanel();
		panel.add(trumpComboBox);
		msgFrame.add(panel,MessageEnumeration.ChosenTrumpInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.ChosenTrumpInfoMessage) {
					ChosenTrumpInfoMessage msg = new ChosenTrumpInfoMessage();
					msg.trump = ((TrumpEntity)trumpComboBox.getSelectedItem());
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialEndOfRoundInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JCheckBox gameOverCheckBox = new JCheckBox("gameOver");
		IntegerPanel scoreTeam1Panel = new IntegerPanel("Score Team1");
		IntegerPanel scoreTeam2Panel = new IntegerPanel("Score Team2");
		panel.add(gameOverCheckBox);
		panel.add(scoreTeam1Panel);
		panel.add(scoreTeam2Panel);
		
		//Add stuff from extended 
		panel.add(new JLabel("-------Fields from StichInfoMessage--------"));
		PlayerEntityPanel playerWhoWonStichPanel = new PlayerEntityPanel("Player who won Stich: ");
		panel.add(playerWhoWonStichPanel);
		CardPanel laidCardPanel = new CardPanel("laidCard:");
		panel.add(laidCardPanel);
		PlayerEntityPanel playerPanel = new PlayerEntityPanel("Player who made Turn:");
		panel.add(playerPanel);
		msgFrame.add(panel,MessageEnumeration.EndOfRoundInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.EndOfRoundInfoMessage) {
					EndOfRoundInfoMessage msg = new EndOfRoundInfoMessage();
					ScoreEntity score = new ScoreEntity();
					score.scores = new Hashtable<>();
					score.scores.put(1, scoreTeam1Panel.getInt());
					score.scores.put(2, scoreTeam2Panel.getInt());
					msg.score = score;
					//msg.gameOver = gameOverCheckBox.isSelected();
					msg.playerWhoWonStich = playerWhoWonStichPanel.getPlayer();
					msg.laidCard = laidCardPanel.getCard();
					msg.player = playerPanel.getPlayer();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialGameStartedInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel description = new JLabel("Teams are generated automatically. Nothing to do here");
		panel.add(description);
		msgFrame.add(panel,MessageEnumeration.GameStartedInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.GameStartedInfoMessage) {
					ClientModel model = client.getModel();
					ArrayList<Player> players = new ArrayList<>(model.getPlayers().values());
					PlayerEntity[] playersTeam1 = new PlayerEntity[2];
					for(int i = 0; i < playersTeam1.length; i++) {
						//Es wird davon ausgegangen dass alle Spieler zugewiesen wurden
						for(Player player : players) {
							if(player.getSeat().getSeatNr() == i) {
								playersTeam1[i] = player.getEntity();
								break;
							}
						}
					}
					PlayerEntity[] playersTeam2 = new PlayerEntity[2];
					for(int i = 0; i < playersTeam2.length; i++) {
						//Es wird davon ausgegangen dass alle Spieler zugewiesen wurden
						for(Player player : players) {
							if(player.getSeat().getSeatNr() == i) {
								playersTeam2[i] = player.getEntity();
								break;
							}
						}
					}
					TeamEntity[] teams = new TeamEntity[2];
					teams[0] = new TeamEntity();
					teams[0].players = playersTeam1;
					teams[0].teamId = 1;
					teams[1] = new TeamEntity();
					teams[1].players = playersTeam2;
					teams[1].teamId = 2;
					GameStartedInfoMessage msg = new GameStartedInfoMessage();
					msg.teams = teams;
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialNewRoundInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel description = new JLabel("No Fields in NewRoundInfoMessage");
		panel.add(description);
		msgFrame.add(panel,MessageEnumeration.NewRoundInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.NewRoundInfoMessage) {
					NewRoundInfoMessage msg = new NewRoundInfoMessage();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialNewTurnInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		PlayerEntityPanel nextPlayerPanel = new PlayerEntityPanel("nextPlayer");
		panel.add(nextPlayerPanel);
		JCheckBox selectWeisBox = new JCheckBox("selectWeis");
		panel.add(selectWeisBox);
		msgFrame.add(panel,MessageEnumeration.NewTurnInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.NewTurnInfoMessage) {
					NewTurnInfoMessage msg = new NewTurnInfoMessage();
					msg.nextPlayer = nextPlayerPanel.getPlayer();
					msg.selectWeis = selectWeisBox.isSelected();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialPlayerChangedStateMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		PlayerEntityPanel playerPanel = new PlayerEntityPanel("player");
		panel.add(playerPanel);
		JCheckBox isReadyBox = new JCheckBox("isReady");
		panel.add(isReadyBox);
		msgFrame.add(panel,MessageEnumeration.PlayerChangedStateMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.PlayerChangedStateMessage) {
					PlayerChangedStateMessage msg = new PlayerChangedStateMessage();
					msg.player = playerPanel.getPlayer();
					msg.isReady = isReadyBox.isSelected();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialPlayerLeftLobbyInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		PlayerEntityPanel playerPanel = new PlayerEntityPanel("player");
		panel.add(playerPanel);
		msgFrame.add(panel,MessageEnumeration.PlayerLeftLobbyInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.PlayerLeftLobbyInfoMessage) {
					PlayerLeftLobbyInfoMessage msg = new PlayerLeftLobbyInfoMessage();
					msg.player = playerPanel.getPlayer();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialPlayerMovedToLobbyInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		PlayerEntityPanel playerPanel = new PlayerEntityPanel("player");
		panel.add(playerPanel);
		msgFrame.add(panel,MessageEnumeration.PlayerMovedToLobbyInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.PlayerMovedToLobbyInfoMessage) {
					PlayerMovedToLobbyInfoMessage msg = new PlayerMovedToLobbyInfoMessage();
					msg.player = playerPanel.getPlayer();
					msg.player.seat = SeatEntity.NOTATTABLE;
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialPlayerMovedToTableInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		PlayerEntityPanel playerPanel = new PlayerEntityPanel("player");
		panel.add(playerPanel);
		JComboBox<SeatEntity> seatComboBox = new JComboBox<>(SeatEntity.values());
		panel.add(seatComboBox);
		msgFrame.add(panel,MessageEnumeration.PlayerMovedToTableInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.PlayerMovedToTableInfoMessage) {
					PlayerMovedToTableInfoMessage msg = new PlayerMovedToTableInfoMessage();
					msg.player = playerPanel.getPlayer();
					msg.player.seat = (SeatEntity)seatComboBox.getSelectedItem();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialStichInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		PlayerEntityPanel playerWhoWonStichPanel = new PlayerEntityPanel("Player who won Stich:");
		panel.add(playerWhoWonStichPanel);
		
		//Add stuff from extended 
		panel.add(new JLabel("-------Fields from TurnInfoMessage--------"));
		CardPanel laidCardPanel = new CardPanel("laidCard:");
		panel.add(laidCardPanel);
		PlayerEntityPanel playerPanel = new PlayerEntityPanel("Player who made Turn:");
		panel.add(playerPanel);
		msgFrame.add(panel,MessageEnumeration.TurnInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.StichInfoMessage) {
					StichInfoMessage msg = new StichInfoMessage();
					msg.playerWhoWonStich = playerWhoWonStichPanel.getPlayer();
					msg.laidCard = laidCardPanel.getCard();
					msg.player = playerPanel.getPlayer();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	
	private void initialTurnInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		CardPanel laidCardPanel = new CardPanel("laidCard:");
		panel.add(laidCardPanel);
		PlayerEntityPanel playerPanel = new PlayerEntityPanel("player");
		panel.add(playerPanel);
		msgFrame.add(panel,MessageEnumeration.TurnInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.TurnInfoMessage) {
					TurnInfoMessage msg = new TurnInfoMessage();
					msg.laidCard = laidCardPanel.getCard();
					msg.player = playerPanel.getPlayer();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialWiisInfoMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		PlayerEntityPanel player = new PlayerEntityPanel("player");
		panel.add(player);
		int maxWiisCount = 4;
		WeisPanel[] wiisPanels = new WeisPanel[maxWiisCount];
		for(int i = 0; i < wiisPanels.length;i++) {
			wiisPanels[i] = new WeisPanel("Weis#" + (i+1));
			panel.add(wiisPanels[i]);
		}
		msgFrame.add(panel,MessageEnumeration.WiisInfoMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.WiisInfoMessage) {
					WiisInfoMessage msg = new WiisInfoMessage();
					msg.player = player.getPlayer();
					ArrayList<WeisEntity> wiis = new ArrayList<>();
					for(int i = 0; i < wiisPanels.length;i++) {
						if(wiisPanels[i].getWeis() != null)
							wiis.add(wiisPanels[i].getWeis());
					}
					WeisEntity[] wiisEntities = new WeisEntity[wiis.size()];
					msg.wiis = wiis.toArray(wiisEntities);
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	//server_messages
	private void initialChooseTrumpMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JCheckBox canSchiebenBox = new JCheckBox("canSchieben");
		panel.add(canSchiebenBox);
		msgFrame.add(panel,MessageEnumeration.ChooseTrumpMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.ChooseTrumpMessage) {
					ChooseTrumpMessage msg = new ChooseTrumpMessage();
					msg.canSchieben = canSchiebenBox.isSelected();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialGameStateMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel descriptionLabel = new JLabel("not yet implemented");
		panel.add(descriptionLabel);
		msgFrame.add(panel,MessageEnumeration.GameStateMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.GameStateMessage) {
					//GameStateMessage msg = new GameStateMessage();
					//TODO
					//client.handleReceivedMessage(msg);
				}
			}
		});
	}
	private void initialHandOutCardsMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		int cardCount = 9;
		CardPanel[] cardPanels = new CardPanel[cardCount];
		for(int i = 0; i < cardPanels.length;i++) {
			cardPanels[i] = new CardPanel("Card#" + (i+1));
			panel.add(cardPanels[i]);
		}
		JButton btnRandom = new JButton("Random");
		panel.add(btnRandom);
		btnRandom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> usedValues = new ArrayList<>();
				Random rm = new Random();
				for(int i = 0; i < cardPanels.length;i++) {
					int color = 0;
					int value = 0;
					Integer id = 0;
					do {
						color = rm.nextInt(4) + 1;
						value = rm.nextInt(9) + 1;
						id = color * 10 + value;
					}while(usedValues.contains(id));
					usedValues.add(id);
					cardPanels[i].setCard(CardEntity.getById(id));
				}
			}
		});
		
		msgFrame.add(panel,MessageEnumeration.HandOutCardsMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.HandOutCardsMessage) {
					HandOutCardsMessage msg = new HandOutCardsMessage();
					CardEntity[] cards = new CardEntity[cardPanels.length];
					for(int i = 0; i < cardPanels.length;i++) {
						cards[i] = cardPanels[i].getCard();
					}
					msg.cards = cards;
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	/*
	private void initialLobbyStateMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel descriptionLabel = new JLabel("Players are generated automatically. Nothing to do here");
		panel.add(descriptionLabel);
		msgFrame.add(panel,MessageEnumeration.LobbyStateMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.LobbyStateMessage) {
					LobbyStateMessage msg = new LobbyStateMessage();
					msg.players = players;
					client.handleReceivedMessage(msg);
				}
			}
		});
	}
	*/
	private void initialWrongCardMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		CardPanel wrongCard = new CardPanel("wrongCard:");
		panel.add(wrongCard);
		msgFrame.add(panel,MessageEnumeration.WrongCardMessage.toString());
		btnReceive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((MessageEnumeration)msgComboBox.getSelectedItem() == MessageEnumeration.WrongCardMessage) {
					WrongCardMessage msg = new WrongCardMessage();
					msg.wrongCard = wrongCard.getCard();
					client.handleReceivedMessage(msg);
				}
			}
		});
	}

	//inherited methods from ClientCommunication
	@Override
	public void send(Message msg) {
		outputArea.append("-----------START-----------\n");
		if(msg == null) {
			outputArea.append("null Message sent");
		}else {
			outputArea.append("MESSAGE: " + msg.getClass().getSimpleName() + " SENT\n");
			if(msg instanceof ChangeStateMessage) {
				ChangeStateMessage cmsg = (ChangeStateMessage)msg;
				outputArea.append("isReady: " + cmsg.isReady + "\n");
			}else if(msg instanceof ChosenTrumpMessage) {
				ChosenTrumpMessage cmsg = (ChosenTrumpMessage)msg;
				if(cmsg.trump != null)
					outputArea.append("trump: " + cmsg.trump + "\n");
				else
					outputArea.append("trump: null\n");
			}else if(msg instanceof ChosenWiisMessage) {
				ChosenWiisMessage cmsg = (ChosenWiisMessage)msg;
				if(cmsg.wiis != null)
					for(WeisEntity w : cmsg.wiis)
						outputArea.append("Weis: " + w.type + " to " + w.originCard.color + " " + w.originCard.value + "\n");
				else
					outputArea.append("wiis: null\n");
			}else if(msg instanceof JoinLobbyMessage) {
				JoinLobbyMessage cmsg = (JoinLobbyMessage)msg;
				if(cmsg.playerData != null)
					outputArea.append(
							"playerData.name: " + cmsg.playerData.name +  "\n" +
							"playerData.id: " + cmsg.playerData.id + "\n" +
							"playerData.isBot: " + cmsg.playerData.isBot + "\n" +
							"playerData.seatNr: " + cmsg.playerData.seat + "\n"
							);
				else
					outputArea.append("playerData: null\n");
			}else if(msg instanceof JoinTableMessage) {
				JoinTableMessage cmsg = (JoinTableMessage)msg;
				if(cmsg.preferedSeat != null)
					outputArea.append("preferedSeat: " + cmsg.preferedSeat +  "\n");
				else
					outputArea.append("preferedSeat: null\n");
			}else if(msg instanceof LeaveLobbyMessage) {
				
			}else if(msg instanceof LeaveTableMessage) {
				
			}else if(msg instanceof PlaceCardMessage) {
				PlaceCardMessage cmsg = (PlaceCardMessage)msg;
				if(cmsg.card != null)
					outputArea.append("card: " + cmsg.card.color + " " + cmsg.card.value +  "\n");
				else
					outputArea.append("card: null\n");
			}
		}
		outputArea.append("------------END------------\n");
	}

	//Getters and setters
	@Override
	public void setClient(AbstractClient client) {
		this.client = client;
	}
}

