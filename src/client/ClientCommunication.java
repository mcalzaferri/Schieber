package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.proto.Message;
import shared.Communication;

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

	public void receive(Message msg) throws ClassNotFoundException, IOException {
		InetAddress senderAddr = null;
		super.receive(msg, senderAddr);
		// Discard messages that were not sent by the server.
		if(!serverAddress.getAddress().equals(senderAddr)) {
			msg = null;
		}
	}
}
