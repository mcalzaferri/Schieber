package client;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.ChosenGameModeMessage;
import ch.ntb.jass.common.proto.player_messages.JoinGameMessage;
import ch.ntb.jass.common.proto.player_messages.LeaveTableMessage;
import ch.ntb.jass.common.proto.player_messages.PlaceCardMessage;
import shared.Card;
import shared.Communication;
import shared.InternalMessage;
import shared.ServerAddress;
import shared.Trump;
import shared.Weis;

public class ClientCommunication extends Communication {
	private InetSocketAddress serverAddress;

	protected ClientCommunication() {
		super();
		serverAddress = null;
	}

	public ClientCommunication(String serverIp) {
		super();
		serverAddress = new InetSocketAddress(serverIp, this.port);
	}

	public ClientCommunication(InetSocketAddress serverAddr, int listenPort) {
		super(listenPort);
		serverAddress = serverAddr;
	}

	// Internal methods

	/**
	 * Send message to server.
	 * @param msg Message to send.
	 * @throws IOException
	 */
	private void send(Message msg) throws IOException {
		super.send(msg, serverAddress);
	}

	private Message receive() throws ClassNotFoundException, IOException {
		InternalMessage msg = super.internalReceive();
		// Discard messages that were not sent by the server.
		if (!serverAddress.getAddress().equals(msg.senderAddress)) {
			return null;
		}
		return msg.message;
	}

	// Methods for clients

	public void publishChosenCard(Card card) {
		PlaceCardMessage msg = new PlaceCardMessage();
		// TODO msg.card = card;
		// TODO send(msg);
	}

	public void publishChosenTrump(Trump trump) {
		ChosenGameModeMessage msg = new ChosenGameModeMessage();
		// TODO msg.color = trump.getTrumpfColor();
		// TODO msg.mode = trump.getGameMode();
		// TODO send(msg);
	}

	public void publishChosenWiis(Weis[] wiis) {

	}

	public void disconnect() {
		LeaveTableMessage msg = new LeaveTableMessage();
		// TODO send(msg);
	}

	/**
	 * @param serverAddress Address of the Game Server
	 */
	// TODO REV: Isn't server IP is already set in the constructor? What is the
	// ServerAddress class for?
	public void connect(ServerAddress serverAddress) {
		JoinGameMessage msg = new JoinGameMessage();
		// TODO send(msg);
	}
}
