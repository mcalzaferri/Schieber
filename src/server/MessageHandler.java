package server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;

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

	private void send(Player player, Message msg) throws IOException {
		com.send(player.getSocketAddress(), msg);
	}

	public void handleMessgages() throws ClassNotFoundException, IOException {
		while (true) {
			System.out.println("Waiting for Message...");
			InternalMessage iMsg = com.internalReceive();
			Message msg = iMsg.message;
			System.out.println("Message received");

			if (msg instanceof JoinGameMessage) {
				if (logic.getPlayers().size() < 4) {
					logic.addPlayer(new Player(iMsg.senderAddress));
					System.out.println("Message recieved: JoinGameMessage");
					System.out.println("Player has been added");
				}
				
				if (logic.getPlayers().size() == 4) {
					handOutCards();
					ChooseGameModeMessage chooseGameMode = new ChooseGameModeMessage();
					//com.send(chooseGameMode, logic.getPlayers());
				}
//			} else if (msg instanceof ChosenGameModeMessage) {
//
//				PickedGameModeMessage pickedGameMode = new PickedGameModeMessage();
//				YourTurnMessage yourTurn = new YourTurnMessage();
//
//				com.broadcast(pickedGameMode);
//				com.send(yourTurn);
//
//				System.out.println("Message recieved: ChosenGameModeMessage");
//				System.out.println("Gamemode has been set");
//			} else if (msg instanceof PlaceCardMessage) {
//
//				PlacedCardMessage placedCard = new PlacedCardMessage(((PlaceCardMessage) message).card);
//				YourTurnMessage yourTurn = new YourTurnMessage();
//
//				com.broadcast(placedCard);
//				com.send(yourTurn, player);
//
//				System.out.println("Message received: PlaceCardMessage");
//				System.out.println("Card has been placed");
			} else {
				System.out.println("invalid Message received");
			}
		}
	}
	
	/**
	 * Hand out cards.
	 */
	public void handOutCards() {
		// hand out cards
		int j = 0;
		for(Map.Entry<Integer, Player> entry : logic.getPlayers().entrySet()) {
			//Player p = entry.getValue();
			//Card[] cards = Arrays.copyOfRange(deck, 4*j, 4*j+3);
			//p.putCards(cards);
			//HandOutCardsMessage msg = new HandOutCardsMessage();
			//msg.cards = cards;
			//com.send(p, msg);
			j++;
		}
	}
	
	private void broadcastMessage(Message msg) throws IOException {
		for(Map.Entry<Integer, Player> entry : logic.getPlayers().entrySet()) {
			com.send(entry.getValue().getSocketAddress(), msg);
		}
	}
}
