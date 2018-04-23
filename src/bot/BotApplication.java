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
		communication.connect(new InetSocketAddress("146.136.43.84",port), "cat-bot " + rm.nextInt(999), true);

		VirtualClient vc = new VirtualClient(communication, new ClientModel());
		vc.setIntelligence(new IntelligenceNormal());


		while(true) {


		}


		//client = new VirtualClient(communication, address);
		//client.setIntelligence(new IntelligenceRandom());

		//while(client.active) {

		//}

	}

}
