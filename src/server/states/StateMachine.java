package server.states;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.LeaveLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.LeaveTableMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_messages.GameStateMessage;
import ch.ntb.jass.common.proto.server_messages.LobbyStateMessage;
import shared.InternalMessage;
import shared.Player;

/**
 * Keeps track of the current state and handles joining and leaving players.
 */
public class StateMachine {
	private GameState currentState;

	/**
	 * Init state machine with default initial state.
	 */
	public StateMachine() {
		changeState(new LobbyState());
	}

	/**
	 * Init state machine with a specific state.
	 * @param state initial state
	 */
	public StateMachine(GameState state) {
		currentState = state;
	}

	public void changeState(GameState state) {
		currentState = state;
	}

	public GameState getCurrentState() {
		return currentState;
	}

	public boolean handleMessage(Player sender, InternalMessage iMsg) throws IOException {
		// joining and leaving players are handled here
		// all other messages are handled by the current state

		ToServerMessage msg = (ToServerMessage)iMsg.message;

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
			// TODO
			return false;
		} else if(msg instanceof LeaveTableMessage) {
			// TODO
			return false;
		} else {
			// let current state handle message
			return currentState.handleMessage(sender, msg);
		}
	}

	private void handleNewPlayer(PlayerEntity playerData, InetSocketAddress playerAddr) throws IOException {
		// create new player
		if(playerData.name == null || playerData.name.isEmpty()) {
			playerData.name = "@" + playerAddr;
		}
		Player sender = new Player(playerAddr, playerData.name, 0,
				playerData.isBot, false, false, GameState.logic.getPlayerCount());
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
