package server.states;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;

import ch.ntb.jass.common.entities.CardEntity;
import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.ScoreEntity;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.EndOfGameInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerLeftLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_messages.GameStateMessage;
import ch.ntb.jass.common.proto.server_messages.LobbyStateMessage;
import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import server.ClientConnection;
import server.ClientConnectionClosedListener;
import server.SchieberMessageHandler;
import server.exceptions.ClientErrorException;
import server.exceptions.InvalidMessageDataException;
import server.exceptions.UnhandledMessageException;
import shared.Card;
import shared.Player;
import shared.Seat;

/**
 * Keeps track of the state the game is in and handles joining and leaving
 * players.
 *
 * State diagram with all messages:
 * <img src="{@docRoot}/common/StateDiagram.png">
 */
public class StateMachine implements SchieberMessageHandler, ClientConnectionClosedListener {
	private GameState currentState;

	/**
	 * Switch to specified game state and executes its entry action
	 * @param state game state to switch to
	 */
	public void changeState(GameState state) throws IOException {
		currentState = state;
		System.out.println("switched to new state: " +
				state.getClass().getSimpleName());
		currentState.act();
	}

	/**
	 * Handle message
	 * Join/leave lobby and leave table messages are handled here all other
	 * messages are handled by the current state.
	 * @param sender the player that sent the message
	 * @param message the sent message
	 */
	@Override
	public synchronized void handleMessage(Player sender, Message message)
			throws IOException, InvalidMessageDataException,
			UnhandledMessageException, ClientErrorException {

		if (message == null) {
			throw(new InvalidMessageDataException("The data you sent was"
					+ "invalid. Fix your shit!"));
		}

		ToServerMessage msg;
		try {
			msg = (ToServerMessage) message;
		} catch(ClassCastException e) {
			throw(new ProtocolException("Server only accepts messages of type ToServerMessage"));
		}

		if (msg instanceof JoinLobbyMessage) {
			if (sender.isInLobby()) {
				System.out.println("Player tried to rejoin lobby");
			} else {
				// New player joined
				PlayerEntity playerData = ((JoinLobbyMessage) msg).playerData;
				handleNewPlayer(sender, playerData);
			}
		} else if (msg instanceof LeaveLobbyMessage) {
			removePlayer(sender);
		} else if (msg instanceof LeaveTableMessage) {
			if (!sender.isAtTable()) {
				return;
			}

			sender.setSeat(Seat.NOTATTABLE);

			PlayerMovedToLobbyInfoMessage pmtlMsg = new PlayerMovedToLobbyInfoMessage();
			pmtlMsg.player = sender.getEntity();
			GameState.com.broadcast(pmtlMsg);

			// if a player is leaving an ongoing game the game is cancelled
			if (!(currentState instanceof LobbyState)) {
				EndOfGameInfoMessage eogMsg = new EndOfGameInfoMessage();
				eogMsg.teamThatWon = null;
				GameState.com.broadcast(eogMsg);

				changeState(new LobbyState());

				System.out.println("Game is cancelled. " + sender + " left");
				ResultMessage rMsg = new ResultMessage();
				rMsg.message = "Game is cancelled. " + sender + " left";
				rMsg.code = ResultMessage.Code.FAILURE;
				GameState.com.broadcast(rMsg);
			}
		} else if (msg instanceof ChangeStateMessage) {
			currentState.handleMessage(sender, (ChangeStateMessage)msg);
		} else if (msg instanceof FillEmptySeatsMessage) {
			currentState.handleMessage(sender, (FillEmptySeatsMessage)msg);
		} else if (msg instanceof JoinTableMessage) {
			currentState.handleMessage(sender, (JoinTableMessage)msg);
		} else if (msg instanceof ChosenTrumpMessage) {
			currentState.handleMessage(sender, (ChosenTrumpMessage)msg);
		} else if (msg instanceof ChosenWiisMessage) {
			currentState.handleMessage(sender, (ChosenWiisMessage)msg);
		} else if (msg instanceof PlaceCardMessage) {
			currentState.handleMessage(sender, (PlaceCardMessage)msg);
		} else {
			throw(new UnhandledMessageException());
		}
	}

	/**
	 * @param player nearly empty player object which was created after a
	 *               connection with the client was established
	 * @param playerData info about the new player
	 */
	private void handleNewPlayer(Player player, PlayerEntity playerData)
			throws ClientErrorException {

		if(playerData.name != null && !playerData.name.isEmpty()) {
			for (Player p : GameState.logic.getPlayers()) {
				if (playerData.name.equals(p.getName())) {
					throw(new ClientErrorException("Username is taken."));
				}
			}

			player.setName(playerData.name);
		}

		player.setSeat(Seat.NOTATTABLE);
		player.setBot(playerData.isBot);
		player.setReady(false);

		GameState.logic.addPlayer(player);
		System.out.println("added " + player + " to the lobby");

		// send current game state to player
		PlayerEntity[] players = Player.getEntities(GameState.logic.getPlayers().toArray(new Player[] {}));
		if(currentState instanceof LobbyState) {
			LobbyStateMessage lsm = new LobbyStateMessage();
			lsm.players = players;
			GameState.com.send(lsm, player);
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

			GameState.com.send(gsm, player);
		}

		// inform others that a new player joined
		PlayerMovedToLobbyInfoMessage pmtlMsg = new PlayerMovedToLobbyInfoMessage();
		pmtlMsg.player = player.getEntity();
		GameState.com.broadcast(pmtlMsg);
	}

	/**
	 * Remove player from game and tell clients about it
	 */
	public synchronized void removePlayer(Player p) {
		GameState.logic.removePlayer(p);
		PlayerLeftLobbyInfoMessage pllMsg = new PlayerLeftLobbyInfoMessage();
		pllMsg.player = p.getEntity();
		GameState.com.broadcast(pllMsg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectionClosed(ClientConnection con) {
		removePlayer(con.getPlayer());
		GameState.com.removeClientConnection(con);
	}
}
