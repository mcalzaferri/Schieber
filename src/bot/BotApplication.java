package bot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import client.ClientCommunication;
import shared.ServerAddress;

public class BotApplication {

	private static ClientCommunication communication;
	private static final int port = 5555;
	private static final ServerAddress address = new ServerAddress(port);
	private static VirtualClient client;
	
	
	public static void main(String[] args) throws IOException {
		communication =  new ClientCommunication();
		communication.connect(new InetSocketAddress("146.136.43.84",port));
		communication.open();
		
		while(true) {

		
		}
		
		
		//client = new VirtualClient(communication, address);
		//client.setIntelligence(new IntelligenceRandom());
		
		//while(client.active) {
		
		//}

	}

}
