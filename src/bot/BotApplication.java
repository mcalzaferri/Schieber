package bot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

import client.ClientCommunication;
import shared.ServerAddress;
import shared.client.ClientModel;

public class BotApplication {

	private static ClientCommunication communication;
	private static final int port = 65000;
	private static final ServerAddress address = new ServerAddress(port);
	private static VirtualClient client;


	public static void main(String[] args) throws IOException {
		communication =  new ClientCommunication();
		communication.open();
		Random rm = new Random();
		client = new VirtualClient(communication, new ClientModel(),
				new InetSocketAddress("146.136.43.84",port),new IntelligenceNormal());


		//client = new VirtualClient(communication, address);
		//client.setIntelligence(new IntelligenceRandom());

		//while(client.active) {

		//}

	}

}
