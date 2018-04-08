package client;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.*;
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
		// TODO send(msg);
	}

	/**
	 * Connect to server.
	 * @param serverAddress Address of the Game Server
	 */
	public void connect(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
		JoinLobbyMessage msg = new JoinLobbyMessage();
		msg.playerData = new PlayerEntity();
		send(msg);
	}

	/**
	 * Connect to server using the default port.
	 * @param ip IP address / hostname of the Game Server
	 */
	public void connect(String ip) {
		connect(new InetSocketAddress(ip, this.port));
	}
}
