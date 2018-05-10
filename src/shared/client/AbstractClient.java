package shared.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import ch.ntb.jass.common.entities.*;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import ch.ntb.jass.common.proto.server_messages.ResultMessage.Code;
import client.BadResultException;
import client.ClientCommunication;
import client.ClientController;
import shared.*;

public abstract class AbstractClient {
	//Fields
	private ClientCommunication com;
	protected ClientModel model;
	private Thread receiveThread;
	
	//Constructors
	public AbstractClient(ClientCommunication com, ClientModel model) {
		this.com = com;
		this.model = model;
	}
	
	public AbstractClient(ClientCommunication com) {
		this(com, new ClientModel());
	}
	
	//Internal methods
	private void clearDeck() {
		if(model.getDeck() != null) {
			model.getDeck().clear();
			doUpdateDeck(model.getDeck().toArray());
		}
	}
	
	//Methods for AbstractClient
	private void showHandOutCardsAnimation(CardEntity[] handEntity, long refreshDelay) {
		CardList handCl = new CardList();
		handCl.updateData(handEntity);
		handCl.sort();
		Card unknownCard = new Card(null, null);
		int i = 0;
		ArrayList<Card> cards = new ArrayList<>(9);
		ArrayList<Card> cardsOnHand = new ArrayList<>(9);
		while(i < 9) {
			for(int j = 0; j < 3; j++, i++) {
				cards.add(unknownCard);
				cardsOnHand.add(handCl.get(i));
			}
			for(int j = 1; j <= 4; j++) {
				for(Team team : model.getTeams()) {
					for(Player player : team.getPlayers()) {
						//Wait for Refresh delay
						if(player.getSeat().getRelativeSeat(model.getThisPlayer().getSeat()).getId() == j) {
							
							if(player.equals(model.getThisPlayer())) {
								showHandOutCardAnimation(player, cardsOnHand);
								player.putCards(cardsOnHand);
							}else {
								showHandOutCardAnimation(player, cards);
								player.putCards(cards);
							}
							playerChanged(player);
						}
					}
				}
			}
		}
	}
	
	//Methods for Communication -> Client
	/** This method is called by the communication class when a new message has been received.
	 * Its main purpose is to parse the message and then handle the specific  message.
	 * @param msg The Message that has been received.
	 */
	public void handleReceivedMessage(Message msg) {
		AbstractClient caller = this;
		ClientMessageDecoder.decodeMessage(msg, new ClientMessageDecoder() {

			//server_info_messages
			@Override
			public void msgReceived(ChosenTrumpInfoMessage msg) {
				Trump trump = Trump.getByEntity(msg.trump);
				model.setTrump(trump);
				doSetTrump(trump);
			}

			@Override
			public void msgReceived(EndOfRoundInfoMessage msg) {
				Score score = new Score(msg.score);
				model.setScore(score);
				model.setTrump(null);
				doUpdateScore(score);
				doSetTrump(null);
				if(msg.gameOver) {
					model.setGameState(GameState.GAMEOVER);
					doEndGame();
				}else {
					model.setGameState(GameState.ROUNDOVER);
					doEndRound();
				}
			}

			@Override
			public void msgReceived(GameStartedInfoMessage msg) {
				Team[] teams = new Team[2];
				for(int i = 0; i <= 1; i++) {
					teams[i] = new Team(msg.teams[i],model.getPlayers());
					//Update all added players with the data in the msg
					for(int j = 0; j <= 1; j++) {
						teams[i].getPlayer(j).update(msg.teams[i].players[j]);
					}
				}
				model.setTeams(teams);
				model.setGameState(GameState.STARTED);
				teamsChanged(teams);
				doSetSeat(model.getThisPlayer().getSeatNr());
			}

			@Override
			public void msgReceived(NewRoundInfoMessage msg) {
				//TODO What is this message good for?
				
			}
			
			public void msgReceived(NewTurnInfoMessage msg) {
				//If the previous GameState wasnt TURNOVER (This means it was GAMEOVER OR ROUNDOVER OR PLAYOVER) the deck must be emptied
				if(model.getGameState() != GameState.TURNOVER) {
					clearDeck();
				}
				model.setGameState(GameState.TURNACTIVE);
				model.setActiveSeatId(msg.nextPlayer.seat.getSeatNr());
				//If the player is you, select card
				if(msg.nextPlayer.id == model.getThisPlayer().getId()) {
					if(msg.selectWeis) {
						model.setPossibleWiis(model.getHand().getPossibleWiis(model.getTrump()));
					}
					//Only request Weis if there is actually something to wiis
					doRequestCard(msg.selectWeis && model.getHand().getPossibleWiis(model.getTrump()).length > 0);
				}
			}

			@Override
			public void msgReceived(PlayerChangedStateMessage msg) {
				model.getPlayer(msg.player).setReady(msg.isReady);
				playerChanged(model.getPlayer(msg.player));
			}

			@Override
			public void msgReceived(PlayerLeftLobbyInfoMessage msg) {
				model.getPlayers().remove(msg.player.id);
				
			}
			
			@Override
			public void msgReceived(PlayerMovedToLobbyInfoMessage msg) {
				model.updatePlayer(msg.player);
				model.getPlayer(msg.player).setSeat(Seat.NOTATTABLE);
				Team[] teams = model.getTeams();
				if(teams != null) {
					for(int i = 0; i <= 1; i++) {
						teams[i].removePlayer(msg.player.id);
					}
					teamsChanged(model.getTeams());
				}
				playerChanged(model.getPlayer(msg.player));
			}

			@Override
			public void msgReceived(PlayerMovedToTableInfoMessage msg) {
				model.updatePlayer(msg.player);
				if(msg.player.seat == null || msg.player.seat == SeatEntity.NOTATTABLE) {
					model.getPlayer(msg.player).setSeat(Seat.SEAT1);
				}
				playerChanged(model.getPlayer(msg.player));
			}

			@Override
			public void msgReceived(TurnInfoMessage msg) {
				Card removedCard;
				Player player = model.getPlayer(msg.player);
				int cardPos = 0;
				if(model.getThisPlayer().equals(player)) {
					removedCard = player.getCards().getCardById(msg.laidCard.calcId());
					cardPos = player.getCards().indexOf(removedCard);
					player.getCards().remove(removedCard);
					doUpdateHand(model.getHand().toArray());
					showLayCardAnimation(model.getThisPlayer(), removedCard, cardPos);
				}else {
					player.getCards().remove(0);
					removedCard = new Card(msg.laidCard);
					showLayCardAnimation(player, removedCard, cardPos);
					
				}
				model.addToDeck(removedCard, msg.player);
				doUpdateDeck(model.getDeck().toArray());
				playerChanged(player);
				if(model.getGameState() != GameState.PLAYOVER)
					model.setGameState(GameState.TURNOVER);
			}

			@Override
			public void msgReceived(StichInfoMessage msg) {
				model.setGameState(GameState.PLAYOVER);
				stichInfo(model.getPlayer(msg.playerWhoWonStich));
			}
			
			@Override
			public void msgReceived(WiisInfoMessage msg) {
				Weis[] wiis = new Weis[msg.wiis.length];
				for(int i = 0; i < msg.wiis.length; i++) {
					wiis[i] = new Weis(msg.wiis[i]);
				}
				doPlayerShowedWiis(wiis, new Player(msg.player));
				
			}

			//server_messages
			@Override
			public void msgReceived(ChooseTrumpMessage msg) {
				doRequestTrump(msg.canSchieben);
			}

			@Override
			public void msgReceived(GameStateMessage msg) {
				//TODO
			}

			@Override
			public void msgReceived(HandOutCardsMessage msg) {
				//Clear deck
				clearDeck();
				//First remove all cards
				for(Team team : model.getTeams()) {
					for(Player player : team.getPlayers()) {
						player.getCards().clear();
					}
				}
				model.setGameState(GameState.STARTED);
				//New handling
				//First check if this player is a bot or not
				if(caller instanceof ClientController) {
					showHandOutCardsAnimation(msg.cards, ((ClientController)caller).getRefreshDelay());
				}else {
					//If its a bot, no need for fancy animations
					//Now set the other players hand to 9 unknown cards
					Card unknownCard = new Card(null, null);
					Card[] ca = new Card[9];
					for(int i = 0; i < ca.length; i++) {
						ca[i] = unknownCard;
					}
					for(Team team : model.getTeams()) {
						for(Player player : team.getPlayers()) {
							if(player.equals(model.getThisPlayer())) {
								continue;
							}
							//Store a copy of the array in each unknown player
							player.putCards(ca.clone());
						}
					}
				}
				model.getHand().updateData(msg.cards);
				model.getHand().sort();
				doUpdateHand(model.getHand().toArray());
			}

			@Override
			public void msgReceived(LobbyStateMessage msg) {
				for(PlayerEntity player : msg.players) {
					model.updatePlayer(player);
					playerChanged(model.getPlayer(player));
				}
			}

			@Override
			public void msgReceived(ResultMessage msg) {
				if(msg.code == Code.OK) {
					goodResultReceived(msg.message);
				}else {
					doHandleBadResultException(new BadResultException(msg.message));
				}
			}	
			
			@Override
			public void msgReceived(WrongCardMessage msg) {
				// TODO Is this message obsolete anyways?
				
			}	
		});
	}
	//Non Abstract Template methods for Server -> Client
	protected void stichInfo(Player playerWhoWonStich) {}
	protected void playerChanged(Player player) {}
	protected void teamsChanged(Team[] teams) {}
	protected void goodResultReceived(String message) {}
	protected void showHandOutCardAnimation(Player cardReceiver, ArrayList<Card> newHand) {}
	protected void showLayCardAnimation(Player player, Card card, int cardPos) {}
	
	
	//Abstract Template methods for Server -> Client
	protected abstract void doSetTrump(Trump trump);
	
	/** Sets the seatId of the Client
	 * @param seatId
	 */
	protected abstract void doSetSeat(int seatId);
	
	/** Determines which seat is currently playing.
	 * If activeSeatId == seatId use publishChosenCard() to finish turn
	 * @param activeSeatId
	 */
	protected abstract void doUpdateActiveSeat(int activeSeatId);
	
	/**Initialises trump selection process. Finish process with publishChosenTrump()
	 * @param canSwitch determines whether the client can use "SCHIEBEN" or not
	 */
	protected abstract void doRequestTrump(boolean canSwitch);
	
	protected abstract void doRequestCard(boolean selectWiis);
	
	protected abstract void doUpdateDeck(Card[] deckCards);
	
	protected abstract void doUpdateHand(Card[] handCards);
	
	protected abstract void doUpdateScore(Score score);
	
	protected abstract void doEndRound();
	
	protected abstract void doEndGame();
	
	protected abstract void doConnected();
	
	protected abstract void doDisconnected();
	
	/**
	 * This method tells the GUI and the Bot if Weis was published
	 * @param Weis(e), which player did it
	 */
	protected abstract void doPlayerShowedWiis(Weis[] wiis, Player player);

	protected abstract void doHandleBadResultException(BadResultException e);
	
	//Methods for Client -> Server
	protected void publishChangedState(boolean isReady) {
		ChangeStateMessage msg = new ChangeStateMessage();
		msg.isReady = isReady;
		com.send(msg);
	}
	protected void publishChosenTrump(Trump trump) {
		ChosenTrumpMessage msg = new ChosenTrumpMessage();
		msg.trump = trump.getEntity();
		com.send(msg);
	}
	protected void publishChosenWiis(Weis[] wiis) {
		ChosenWiisMessage msg = new ChosenWiisMessage();
		msg.wiis = Weis.getEntities(wiis);
		com.send(msg);
	}
	protected void joinTable(Seat preferedSeat) {
		JoinTableMessage msg = new JoinTableMessage();
		msg.preferedSeat = preferedSeat.getSeatEntity();
		com.send(msg);
	}
	protected void leaveLobby() {
		LeaveLobbyMessage msg = new LeaveLobbyMessage();
		com.send(msg);
	}
	protected void leaveTable() {
		LeaveTableMessage msg = new LeaveTableMessage();
		com.send(msg);
	}
	protected void publishChosenCard(Card card) {
		PlaceCardMessage msg = new PlaceCardMessage();
		msg.card = card.getEntity();
		com.send(msg);
	}
	
	/**
	 * @param serverAddress Address to connect to
	 * @throws Exception 
	 */
	public void connect(InetSocketAddress serverAddress, String username, boolean isBot) throws BadResultException {
		model.setThisPlayer(new Player(serverAddress,username,Seat.NOTATTABLE));
		com.connect(serverAddress, username, isBot);
		doConnected();
		
		//Start receive Thread after connection has been established
		receiveThread = new Thread(com);
		receiveThread.start();
		
		//If this is a bot, try to join the table and set seat to true. If this fails the bot should be terminated
		if(isBot) {
			joinTable(Seat.SEAT1);
			publishChangedState(true);
		}
	}
	
	public void disconnect() {
		com.disconnect();
		doDisconnected();
	}
	
	//Getters and Setters
	public ClientModel getModel() {
		return this.model;
	}
}
