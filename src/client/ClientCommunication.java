package client;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.*;

import shared.Communication;
import shared.InternalMessage;
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
	public void send(Message msg){
		try {
			super.send(msg, serverAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receive() throws ClassNotFoundException, IOException {
		InternalMessage msg = super.internalReceive();
		// Discard messages that were not sent by the server.
		if (!serverAddress.equals(msg.senderAddress) || msg == null) {
			return;
		}
		client.handleReceivedMessage(msg.message);
	}

	// Methods for clients

	public void disconnect() {
		LeaveTableMessage msg = new LeaveTableMessage();
		send(msg);
		// TODO what do now?
	}

	/**
	 * Connect to server.
	 * @param serverAddress Address of the Game Server
	 */
	public void connect(InetSocketAddress serverAddress, String username, boolean isBot) {
		this.serverAddress = serverAddress;
		JoinLobbyMessage msg = new JoinLobbyMessage();
		msg.playerData = new PlayerEntity();
		msg.playerData.isBot = false;
		msg.playerData.name = username;
		send(msg);
	}
}
