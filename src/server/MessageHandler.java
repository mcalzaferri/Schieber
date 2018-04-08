package server;

import java.io.IOException;
import java.net.SocketException;

import ch.ntb.jass.common.entities.*;
import ch.ntb.jass.common.proto.*;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import server.states.GameState;
import server.states.LobbyState;
import shared.Communication;
import shared.InternalMessage;
import shared.Player;

public class MessageHandler {
	private GameLogic logic;
	private Communication com;
	private GameState currentState;

	public MessageHandler(GameLogic logic) throws SocketException {
		this.logic = logic;
		com = new Communication();
		com.open();
	}

	public MessageHandler(GameLogic logic, int port) throws SocketException {
		this.logic = logic;
		com = new Communication(port);
		com.open();
	}

	public void run() throws ClassNotFoundException, IOException {
		while (true) {
			handleMessage();
		}
	}

	protected void handleMessage() throws ClassNotFoundException, IOException {
		System.out.println("Waiting for Message...");
		InternalMessage iMsg = com.internalReceive();

		ToServerMessage msg = (ToServerMessage)iMsg.message;
		Player sender = logic.getPlayer(iMsg.senderAddress);

		System.out.print("received " + msg.getClass().getSimpleName());
		if(sender != null) {
			System.out.println(" from " + sender.getName());
		} else {
			System.out.println();
		}

		if (msg instanceof JoinLobbyMessage) {
			if(sender == null) {
				// Handle new player

				// create new player
				PlayerEntity ePlayer = ((JoinLobbyMessage)msg).playerData;
				if(ePlayer == null) {
					System.err.print(msg.getClass().getSimpleName()
							+ " with no data received");
					return;
				}
				if(ePlayer.name == null || ePlayer.name.isEmpty()) {
					ePlayer.name = "@" + iMsg.senderAddress;
				}
				sender = new Player(iMsg.senderAddress, ePlayer.name, 0,
						ePlayer.isBot, false, false, logic.getPlayerCount());
				logic.addPlayer(sender);

				// inform others that a player joined
				broadcastMessage(new PlayerMovedToLobbyInfoMessage());

				PlayerEntity[] players = logic.getPlayers().toArray(new PlayerEntity[0]);

				if(currentState instanceof LobbyState) {
					LobbyStateMessage lsm = new LobbyStateMessage();
					lsm.players = players;
					send(lsm, sender);
				} else {
					GameStateMessage gsm = new GameStateMessage();
					gsm.players = players;
					// TODO:
//					gsm.teams = ;
//					gsm.currentPlayer = ;
//					gsm.hands = ;
//					gsm.deck = ;
//					gsm.trump = ;
//					gsm.score = ;
					send(gsm, sender);
				}
				System.out.println("Added " + ePlayer.name + " to the lobby");
			} else {
				System.out.println("Player tried to rejoin lobby");
			}
		} else {
			// Let current state handle the message
			if(!currentState.handle(sender, msg)) {
				System.err.println("Message not handled, current state = "
						+ currentState.getClass().getSimpleName());
			}
		}
	}

	public void changeState(GameState state) {
		currentState = state;
	}

	/**
	 * Stop message handler.
	 */
	public void stop() {
		com.close();
		// TODO: broadcast shutdown message?
	}

	public void handOutCards() throws IOException {
		for (Player p : logic.getPlayers()) {
			HandOutCardsMessage msg = new HandOutCardsMessage();
			msg.cards = logic.assignCardsToPlayer(p);
			send(msg, p);
		}
	}

	public void send(Message msg, Player player) throws IOException {
		com.send(msg, player.getSocketAddress());
	}

	public void broadcastMessage(Message msg) throws IOException {
		for (Player p : logic.getPlayers()) {
			send(msg, p);
		}
	}

	public void setReceiveTimeout(int tmo) throws SocketException {
		com.setReceiveTimeout(tmo);
	}
}
