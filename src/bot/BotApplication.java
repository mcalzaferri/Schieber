package bot;

import java.io.IOException;
import java.net.SocketException;

import ch.ntb.jass.common.proto.player_messages.JoinGameMessage;
import client.ClientCommunication;
import shared.ClientCommunicationInterface;
import shared.ServerAddress;

public class BotApplication {

	private static ClientCommunicationInterface communication;
	private static ClientCommunication communication2;
	private static final int port = 5555;
	private static final ServerAddress address = new ServerAddress(port);
	private static VirtualClient client;
	
	
	public static void main(String[] args) throws IOException {
		
		communication2 = new ClientCommunication("146.136.43.84");
		communication2.open();
		
		while(true) {

			communication2.send(new JoinGameMessage());
		
		}
		
		
		//client = new VirtualClient(communication, address);
		//client.setIntelligence(new IntelligenceRandom());
		
		//while(client.active) {
		
		//}

	}

}
