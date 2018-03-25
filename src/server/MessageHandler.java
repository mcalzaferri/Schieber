package server;

import java.io.IOException;
import java.net.SocketException;

import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.JoinGameMessage;
import ch.ntb.jass.common.proto.server_messages.ChooseGameModeMessage;
import shared.Communication;
import shared.InternalMessage;
import shared.Player;

public class MessageHandler {
	private GameLogic logic;
	private Communication com;

	public MessageHandler(GameLogic logic) throws SocketException {
		super();
		this.logic = logic;
		com = new Communication();
		com.open();
	}

	private void send(Message msg, Player player) throws IOException {
		com.send(msg, player.getSocketAddress());
	}

	public void handleMessgages() throws ClassNotFoundException, IOException {
		while (true) {
			System.out.println("Waiting for Message...");
			InternalMessage iMsg = com.internalReceive();
			Message msg = iMsg.message;
			System.out.println(msg.getClass().getSimpleName() + " received");

			if (msg instanceof JoinGameMessage) {
				if (logic.getPlayerCount() == 4) {
					System.out.println("Warning: Table already full");
					continue;
				}
				// TODO: add name field to JoinGameMessage?
				logic.addPlayer(new Player(iMsg.senderAddress, "",
						logic.getPlayerCount()));

				handOutCards();

				// request game mode from first player
				send(new ChooseGameModeMessage(), logic.getPlayers().get(0));
				// } else if (msg instanceof ChosenGameModeMessage) {
				//
				// PickedGameModeMessage pickedGameMode = new
				// PickedGameModeMessage();
				// YourTurnMessage yourTurn = new YourTurnMessage();
				//
				// com.broadcast(pickedGameMode);
				// com.send(yourTurn);
				//
				// System.out.println("Message recieved:
				// ChosenGameModeMessage");
				// System.out.println("Gamemode has been set");
				// } else if (msg instanceof PlaceCardMessage) {
				//
				// PlacedCardMessage placedCard = new
				// PlacedCardMessage(((PlaceCardMessage) message).card);
				// YourTurnMessage yourTurn = new YourTurnMessage();
				//
				// com.broadcast(placedCard);
				// com.send(yourTurn, player);
				//
				// System.out.println("Message received: PlaceCardMessage");
				// System.out.println("Card has been placed");
			} else {
				System.out.println("invalid Message received");
			}
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

	private void broadcastMessage(Message msg) throws IOException {
		for (Player p : logic.getPlayers()) {
			send(msg, p);
		}
	}
}
