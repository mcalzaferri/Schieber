package bot;

import shared.ClientCommunicationInterface;
import shared.ServerAddress;

public class BotApplication {

	private static ClientCommunicationInterface communication;
	private static final int port = 5555;
	private static final ServerAddress address = new ServerAddress();
	private static VirtualClient client;
	
	
	public static void main(String[] args) {
		client = new VirtualClient(communication, address);
		client.setIntelligence(new IntelligenceRandom());
		
		while(client.active) {
		
		}

	}

}
