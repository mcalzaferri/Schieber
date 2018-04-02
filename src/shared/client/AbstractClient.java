package shared.client;

import java.net.InetSocketAddress;

import ch.ntb.jass.common.entities.CardEntity;
import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.proto.Message;
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
			public void msgReceived(ChosenGameModeInfoMessage msg) {
				//TODO this will be renamed to ChosenTrumpInfoMessage
				Trump trump = Trump.EICHEL;//TODO delete this when fixed.
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
				model.setActiveSeatId(msg.nextPlayer.seat.seatNr);
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
			public void msgReceived(PlayerMovedToLobbyInfoMessage msg) {
				model.updatePlayer(msg.player);
				model.getPlayerById(msg.player.id).setAtTable(false);
				Team[] teams = model.getTeams();
				for(int i = 0; i <= 1; i++) {
					teams[i].removePlayer(msg.player.id);
				}
				teamsChanged(model.getTeams());
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
					model.getHand().remove(CardEntity.getId(msg.laidCard));
				}
				playerChanged(model.getPlayerById(msg.player.id));
				doUpdateDeck(model.getDeck().toArray());
				doUpdateHand(model.getHand().toArray());
				
				if(msg.emptyDeck)
					model.setGameState(GameState.PLAYOVER);
				else
					model.setGameState(GameState.TURNOVER);
			}

			@Override
			public void msgReceived(WiisInfoMessage msg) {
				// TODO Auto-generated method stub
				
			}

			//server_messages
			@Override
			public void msgReceived(ChooseGameModeMessage msg) {
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
	
	//TODO: review proposal and accept/decline
	//proposal Lukas
	/**
	 * This method tells the GUI and the Bot if Weis was published
	 * @param Weis(e), which player did it
	 */
	//protected abstract void doShowWeis(Weis[] wiis, int playerID)();

	//Methods for Client -> Server
	public void publishChosenTrump(Trump trump) {
		com.publishChosenTrump(trump);
	}
	
	public void publishChosenCard(Card card) {
		com.publishChosenCard(card);
	}
	
	public void publishChosenWeis(Weis[] wiis) {
		com.publishChosenWiis(wiis);
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
}
