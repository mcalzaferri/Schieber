package shared.client;

import java.net.InetSocketAddress;

import ch.ntb.jass.common.entities.*;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import client.ClientCommunication;
import shared.*;

public abstract class AbstractClient {
	//Fields
	private ClientCommunication com;
	protected ClientModel model;
	
	//Constructors
	public AbstractClient(ClientCommunication com, ClientModel model) {
		this.com = com;
		this.model = model;
	}
	
	public AbstractClient(ClientCommunication com) {
		this(com, new ClientModel());
	}
	
	//Methods for Communication -> Client
	/** This method is called by the communication class when a new message has been received.
	 * Its main purpose is to parse the message and then handle the specific  message.
	 * @param msg The Message that has been received.
	 */
	public void handleReceivedMessage(Message msg) {
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
				doUpdateScore(score);
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
					for(int j = 0; i <= 1; i++) {
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
					model.getDeck().clear();
					doUpdateDeck(model.getDeck().toArray());
				}
				model.setGameState(GameState.TURNACTIVE);
				model.setActiveSeatId(msg.nextPlayer.seat.getSeatNr());
				//If the player is you, select card
				if(msg.nextPlayer.id == model.getThisPlayer().getId()) {
					doRequestCard(msg.selectWeis);
				}
			}

			@Override
			public void msgReceived(PlayerChangedStateMessage msg) {
				model.getPlayerById(msg.player.id).setReady(msg.isReady);
				playerChanged(model.getPlayerById(msg.player.id));
			}

			@Override
			public void msgReceived(PlayerLeftLobbyInfoMessage msg) {
				model.getPlayers().remove(msg.player.id);
				
			}
			
			@Override
			public void msgReceived(PlayerMovedToLobbyInfoMessage msg) {
				model.updatePlayer(msg.player);
				model.getPlayerById(msg.player.id).setAtTable(false);
				Team[] teams = model.getTeams();
				if(teams != null) {
					for(int i = 0; i <= 1; i++) {
						teams[i].removePlayer(msg.player.id);
					}
					teamsChanged(model.getTeams());
				}
				playerChanged(model.getPlayerById(msg.player.id));
			}

			@Override
			public void msgReceived(PlayerMovedToTableInfoMessage msg) {
				model.updatePlayer(msg.player);
				model.getPlayerById(msg.player.id).setAtTable(true);
				playerChanged(model.getPlayerById(msg.player.id));
			}

			@Override
			public void msgReceived(TurnInfoMessage msg) {
				model.getDeck().add(new Card(msg.laidCard));
				if(msg.player.id == model.getThisPlayer().getId()) {
					//This player has laid that card
					model.getHand().remove(msg.laidCard.calcId());
				}
				playerChanged(model.getPlayerById(msg.player.id));
				doUpdateDeck(model.getDeck().toArray());
				doUpdateHand(model.getHand().toArray());
				
				if(model.getGameState() != GameState.PLAYOVER)
					model.setGameState(GameState.TURNOVER);
			}

			@Override
			public void msgReceived(StichInfoMessage msg) {
				model.setGameState(GameState.PLAYOVER);
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
				model.getHand().updateData(msg.cards);
				doUpdateHand(model.getHand().toArray());
				model.setPossibleWiis(model.getHand().getPossibleWiis(model.getTrump()));
			}

			@Override
			public void msgReceived(LobbyStateMessage msg) {
				for(PlayerEntity player : msg.players) {
					model.getPlayers().put(player.id, new Player(player));
					playerChanged(model.getPlayerById(player.id));
				}
			}

			@Override
			public void msgReceived(WrongCardMessage msg) {
				// TODO Is this message obsolete anyways?
				
			}			
		});
	}
	//Non Abstract Template methods for Server -> Client
	protected void playerChanged(Player player) {}
	protected void teamsChanged(Team[] teams) {}
	
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
	
	protected abstract void doRequestWeis();
	
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
		msg.wiis = wiis;
		com.send(msg);
	}
	protected void joinLobby(String username) {
		JoinLobbyMessage msg = new JoinLobbyMessage();
		model.setThisPlayer(new Player(null,username,Seat.NOTATTABLE));
		msg.playerData = model.getThisPlayer();
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
		msg.card = card;
		com.send(msg);
	}
	
	/**
	 * @param serverAddress Address to connect to
	 */
	public void connect(InetSocketAddress serverAddress) {
		 com.connect(serverAddress);
	}
	
	public void disconnect() {
		com.disconnect();
	}
	
	//Getters and Setters
	public ClientModel getModel() {
		return this.model;
	}
}
