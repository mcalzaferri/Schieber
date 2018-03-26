package server;

import java.io.IOException;
import java.net.SocketException;

import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.ChosenGameModeMessage;
import ch.ntb.jass.common.proto.player_messages.JoinGameMessage;
import ch.ntb.jass.common.proto.player_messages.PlaceCardMessage;
import ch.ntb.jass.common.proto.player_messages.StartGameMessage;
import ch.ntb.jass.common.proto.server_messages.ChooseGameModeMessage;
import ch.ntb.jass.common.proto.server_messages.YourTurnMessage;
import shared.Communication;
import shared.InternalMessage;
import shared.Player;

public class MessageHandler {
	private GameLogic logic;
	private Communication com;

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
		Message msg = iMsg.message;
		System.out.println(msg.getClass().getSimpleName() + " received");

		if (msg instanceof JoinGameMessage) {
			// TODO: add name field to JoinGameMessage?
			logic.addPlayer(new Player(iMsg.senderAddress, "",
					logic.getPlayerCount()));

			if (logic.getPlayerCount() == 4) {
				handOutCards();
				// request game mode from first player
				send(new ChooseGameModeMessage(), logic.getPlayers().get(0));
				// TODO: change game state
			}
		} else if (msg instanceof ChosenGameModeMessage) {
			StartGameMessage startGameMessage = new StartGameMessage();
//				broadcastMessage(startGameMessage);

			System.out.println("Game has started");
			YourTurnMessage yourTurnMessage = new YourTurnMessage();
			send(yourTurnMessage, logic.getPresentPlayer());

		} else if (msg instanceof PlaceCardMessage) {
			YourTurnMessage yourTurn = new YourTurnMessage();
			broadcastMessage(msg);
			send(yourTurn, logic.getPresentPlayer());
//			} else if (msg instanceof LeaveTableMessage) {
//				for (Player p : logic.getPlayers()) {
//					if(p.getSocketAddress().equals(iMsg.senderAddress)){
//						logic.removePlayer(p);
//						System.out.println("Player " + p.getId()+ " has left the game");
//					}
//				}
//			} else {
			System.err.println("Unkown message!");
		}
	}

	/**
	 * Stop message handler.
	 */
	public void stop() {
		com.close();
		// TODO: broadcast shutdown message?
	}

	/**
	 * Hand out cards.
	 */
	public void handOutCards() {
		for (Player p : logic.getPlayers()) {
			// HandOutCardsMessage msg = new HandOutCardsMessage();
			// msg.cards = logic.getCardsForPlayer(p);
			// com.send(msg, p);
		}
	}

	private void send(Message msg, Player player) throws IOException {
		com.send(msg, player.getSocketAddress());
	}

	private void broadcastMessage(Message msg) throws IOException {
		for (Player p : logic.getPlayers()) {
			send(msg, p);
		}
	}

	public void setReceiveTimeout(int tmo) throws SocketException {
		com.setReceiveTimeout(tmo);
	}
}
