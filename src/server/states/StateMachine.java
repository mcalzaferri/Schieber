package server.states;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import ch.ntb.jass.common.entities.CardEntity;
import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.ScoreEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.LeaveLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.LeaveTableMessage;
import ch.ntb.jass.common.proto.server_info_messages.EndOfRoundInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerLeftLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_messages.GameStateMessage;
import ch.ntb.jass.common.proto.server_messages.LobbyStateMessage;
import server.exceptions.ClientErrorException;
import server.exceptions.InvalidMessageDataException;
import server.exceptions.UnhandledMessageException;
import shared.Card;
import shared.InternalMessage;
import shared.Player;
import shared.Seat;

/**
 * Keeps track of the current state and handles joining and leaving players.
 */
public class StateMachine {
	private GameState currentState;

	public void changeState(GameState state) throws IOException {
		currentState = state;
		System.out.println("switched to new state: " + state.getClass().getSimpleName());
		currentState.act();
	}

	public GameState getCurrentState() {
		return currentState;
	}

	public void handleMessage(Player sender, InternalMessage iMsg) throws IOException, InvalidMessageDataException, UnhandledMessageException, ClientErrorException {
		// joining and leaving players are handled here
		// all other messages are handled by the current state

		if (iMsg.message == null) {
			throw(new InvalidMessageDataException("The data you sent was"
					+ "invalid. Fix your shit!"));
		}

		ToServerMessage msg;

		try {
			msg = (ToServerMessage)iMsg.message;
		} catch (ClassCastException e) {
			throw(new ClientErrorException("The server only accepts data of "
					+ "type ToServerMessage. Fix your shit!"));
		}

		if (msg instanceof JoinLobbyMessage) {
			if (sender == null) {
				// New player joined
				PlayerEntity playerData = ((JoinLobbyMessage) msg).playerData;
				handleNewPlayer(playerData, iMsg.senderAddress);
			} else {
				System.out.println("Player tried to rejoin lobby");
			}
		} else if (msg instanceof LeaveLobbyMessage) {
			GameState.logic.removePlayer(sender);
			PlayerLeftLobbyInfoMessage pllMsg = new PlayerLeftLobbyInfoMessage();
			pllMsg.player = sender.getEntity();
			GameState.broadcast(pllMsg);
		} else if (msg instanceof LeaveTableMessage) {
			if (!sender.isAtTable()) {
				return;
			}

			sender.setSeat(Seat.NOTATTABLE);

			PlayerMovedToLobbyInfoMessage pmtlMsg = new PlayerMovedToLobbyInfoMessage();
			pmtlMsg.player = sender.getEntity();
			GameState.broadcast(pmtlMsg);

			// if a player is leaving an ongoing game the game is cancelled
			if (!(currentState instanceof LobbyState)) {
				EndOfRoundInfoMessage eorim = new EndOfRoundInfoMessage();
				eorim.score = null;
				eorim.gameOver = true;
				GameState.broadcast(eorim);
				changeState(new LobbyState());
			}
		} else {
			// let current state handle message
			currentState.handleMessage(sender, msg);
		}
	}

	private void handleNewPlayer(PlayerEntity playerData, InetSocketAddress playerAddr) throws IOException, ClientErrorException {
		// create new player
		if(playerData.name == null || playerData.name.isEmpty()) {
			playerData.name = "@" + playerAddr;
		}

		for (Player p : GameState.logic.getPlayers()) {
			if (playerData.name.equals(p.getName())) {
				throw(new ClientErrorException("Username is taken."));
			}
		}

		Player sender = new Player(playerAddr, playerData.name, Seat.NOTATTABLE,
				playerData.isBot, false, GameState.logic.generatePlayerId());
		GameState.logic.addPlayer(sender);
		System.out.println("added " + playerData.name + " to the lobby");

		// send current game state to player
		PlayerEntity[] players = Player.getEntities(GameState.logic.getPlayers().toArray(new Player[] {}));
		if(currentState instanceof LobbyState) {
			LobbyStateMessage lsm = new LobbyStateMessage();
			lsm.players = players;
			GameState.send(lsm, sender);
		} else {
			GameStateMessage gsm = new GameStateMessage();

			gsm.players = players;
			gsm.teams = GameState.logic.getTeams();
			gsm.currentPlayer = GameState.logic.getCurrentPlayer().getEntity();

			Map<Integer, CardEntity[]> hands = new HashMap<>();
			for (Player p : GameState.logic.getPlayers()) {
				if (p.isAtTable()) {
					CardEntity[] cs = Card.getEntities(p.getCards().toArray());
					for (CardEntity c : cs) {
						c.color = null;
						c.value = null;
					}
					hands.put(p.getId(), cs);
				}
			}
			gsm.hands = hands;

			gsm.deck = Card.getEntities(GameState.logic.getCardsOnTable().toArray(new Card[] {}));
			gsm.trump = GameState.logic.getTrump().getEntity();

			ScoreEntity score = new ScoreEntity();
			score.scores = GameState.logic.getScores();
			gsm.score = score;

			GameState.send(gsm, sender);
		}

		// inform others that a new player joined
		PlayerMovedToLobbyInfoMessage pmtlMsg = new PlayerMovedToLobbyInfoMessage();
		pmtlMsg.player = sender.getEntity();
		GameState.broadcast(pmtlMsg);
	}
}
