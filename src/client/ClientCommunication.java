package client;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.proto.*;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import shared.Card;
import shared.Communication;
import shared.InternalMessage;
import shared.ServerAddress;
import shared.Trump;

public class ClientCommunication extends Communication {
	private InetSocketAddress serverAddress;

	public ClientCommunication(String serverIp) {
		super();
		serverAddress = new InetSocketAddress(serverIp, this.port);
	}

	//Internal methods
	
	/**
	 * Send message to server.
	 * @param msg Message to send.
	 * @throws IOException
	 */
	private void send(Message msg) throws IOException {
		super.send(serverAddress, msg);
	}

	private Message receive() throws ClassNotFoundException, IOException {
		InternalMessage msg = super.internalReceive();
		// Discard messages that were not sent by the server.
		if(!serverAddress.getAddress().equals(msg.senderAddress)) {
			return null;
		}
		return msg.message;
	}
	
	//Methods for clients
	
	public void publishChosenCard(Card card) {
		PlaceCardMessage msg = new PlaceCardMessage();
		//TODO msg.card = card;
		//TODO send(msg);
	}
	public void publishChosenTrump(Trump trump) {
		ChosenGameModeMessage msg = new ChosenGameModeMessage();
		//TODO msg.color = trump.getTrumpfColor();
		//TODO msg.mode = trump.getGameMode();
		//TODO send(msg);
	}
	public void disconnect() {
		LeaveTableMessage msg = new LeaveTableMessage();
		//TODO send(msg);
	}
	/**
	 * @param serverAddress Address of the Game Server
	 */
	public void connect(ServerAddress serverAddress) {
		JoinGameMessage msg = new JoinGameMessage();
		//TODO send(msg);
	}
}
