package server.states;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.LeaveLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.LeaveTableMessage;
import ch.ntb.jass.common.proto.server_info_messages.EndOfRoundInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerLeftLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_messages.GameStateMessage;
import ch.ntb.jass.common.proto.server_messages.LobbyStateMessage;
import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import shared.InternalMessage;
import shared.Player;
import shared.Seat;

/**
 * Keeps track of the current state and handles joining and leaving players.
 */
public class StateMachine {
	private GameState currentState;

	/**
	 * Init state machine with default initial state.
	 * @throws IOException
	 */
	public StateMachine() throws IOException {
		changeState(new LobbyState());
	}

	/**
	 * Init state machine with a specific state.
	 * @param state initial state
	 */
	public StateMachine(GameState state) {
		currentState = state;
	}

	public void changeState(GameState state) throws IOException {
		currentState = state;
		currentState.act();
	}

	public GameState getCurrentState() {
		return currentState;
	}

	public boolean handleMessage(Player sender, InternalMessage iMsg) throws IOException {
		// joining and leaving players are handled here
		// all other messages are handled by the current state

		if (iMsg.message == null) {
			GameState.sendResultMsg(ResultMessage.Code.PROTOCOL_ERROR,
					"The data you sent was invalid. Fix your shit!", sender);
		}

		ToServerMessage msg;

		try {
			msg = (ToServerMessage)iMsg.message;
		} catch(ClassCastException e) {
			GameState.sendResultMsg(ResultMessage.Code.PROTOCOL_ERROR,
					"The server only accepts data of type ToServerMessage. " +
					"Fix your shit!",
					sender);
			System.err.println("Received a message that is not a ToServerMessage.");
			return false;
		}

		try {
			if (msg instanceof JoinLobbyMessage) {
				if(sender == null) {
					// New player joined
					PlayerEntity playerData = ((JoinLobbyMessage) msg).playerData;
					if(playerData == null) {
						System.err.print(msg.getClass().getSimpleName()
								+ " with no data received");
						return true;
					}
					handleNewPlayer(playerData, iMsg.senderAddress);
					return true;
				} else {
					System.out.println("Player tried to rejoin lobby");
					return true;
				}
			} else if(msg instanceof LeaveLobbyMessage) {				
				PlayerLeftLobbyInfoMessage pllim = new PlayerLeftLobbyInfoMessage();			
				GameState.broadcast(pllim);
				GameState.logic.removePlayer(sender);			
				return true;				
			} else if(msg instanceof LeaveTableMessage) {				
				PlayerMovedToLobbyInfoMessage pmtlim = new PlayerMovedToLobbyInfoMessage();
				GameState.broadcast(pmtlim);	
				sender.setSeat(Seat.NOTATTABLE);
				EndOfRoundInfoMessage eorim = new EndOfRoundInfoMessage();
				GameState.broadcast(eorim);
				changeState(new LobbyState());		
				return true;				
			} else {
				// let current state handle message
				return currentState.handleMessage(sender, msg);
			}
		} catch (Exception e) {
			GameState.sendResultMsg(ResultMessage.Code.PROTOCOL_ERROR,
					"Something went wrong while processing the data you sent." +
					" The server will just assume it's your fault and continue." +
					" This might help you fix your code:" + System.lineSeparator() +
					e.getMessage(),
					sender);
			return true;
		}
	}

	private void handleNewPlayer(PlayerEntity playerData, InetSocketAddress playerAddr) throws IOException {
		// create new player
		if(playerData.name == null || playerData.name.isEmpty()) {
			playerData.name = "@" + playerAddr;
		}
		Player sender = new Player(playerAddr, playerData.name, Seat.NOTATTABLE,
				playerData.isBot, false, GameState.logic.getPlayerCount());
		GameState.logic.addPlayer(sender);

		// inform others that a new player joined
		GameState.broadcast(new PlayerMovedToLobbyInfoMessage());

		// send current game state to player
		PlayerEntity[] players = GameState.logic.getPlayers().toArray(new PlayerEntity[0]);
		if(currentState instanceof LobbyState) {
			LobbyStateMessage lsm = new LobbyStateMessage();
			lsm.players = players;
			GameState.send(lsm, sender);
		} else {
			GameStateMessage gsm = new GameStateMessage();
			gsm.players = players;
			// TODO
			//gsm.teams = ;
			//gsm.currentPlayer = ;
			//gsm.hands = ;
			//gsm.deck = ;
			//gsm.trump = ;
			//gsm.score = ;
			GameState.send(gsm, sender);
		}
		System.out.println("Added " + playerData.name + " to the lobby");
	}
}
