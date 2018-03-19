package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.proto.Message;
import shared.Communication;
import shared.InternalMessage;

public class ClientCommunication extends Communication {
	private InetSocketAddress serverAddress;

	public ClientCommunication(String serverIp) {
		super();
		serverAddress = new InetSocketAddress(serverIp, this.port);
	}

	/**
	 * Send message to server.
	 * @param msg Message to send.
	 * @throws IOException
	 */
	public void send(Message msg) throws IOException {
		super.send(serverAddress, msg);
	}

	public Message receive() throws ClassNotFoundException, IOException {
		InternalMessage msg = super.internalReceive();
		// Discard messages that were not sent by the server.
		if(!serverAddress.getAddress().equals(msg.SenderAddress)) {
			return null;
		}
		return msg.message;
	}
}
