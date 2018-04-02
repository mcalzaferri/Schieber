package client;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.ChosenGameModeMessage;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.LeaveTableMessage;
import ch.ntb.jass.common.proto.player_messages.PlaceCardMessage;
import shared.Card;
import shared.Communication;
import shared.InternalMessage;
import shared.ServerAddress;
import shared.Trump;
import shared.Weis;
import shared.client.AbstractClient;

public class ClientCommunication extends Communication {
	private InetSocketAddress serverAddress;
	private AbstractClient client;

	public ClientCommunication() {
		super();
	}

	public ClientCommunication(int listenPort) {
		super(listenPort);
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

	private void receive() throws ClassNotFoundException, IOException {
		InternalMessage msg = super.internalReceive();
		// Discard messages that were not sent by the server.
		if (!serverAddress.equals(msg.senderAddress) || msg == null) {
			return;
		}
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
	public void connect(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
		JoinLobbyMessage msg = new JoinLobbyMessage();
		// TODO send(msg);
	}
}
