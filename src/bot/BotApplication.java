package bot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

import client.ClientCommunication;
import shared.Communication;
import shared.ServerAddress;
import shared.client.ClientModel;

public class BotApplication {

	private static ClientCommunication communication;

	public static void main(String[] args) throws IOException {
		int listenPort = Communication.defaultListenPort;
		String serverHostname = "146.136.43.84";
		int serverPort = Communication.defaultListenPort;

		try {
			listenPort = Integer.parseInt(args[0]);
			serverHostname = args[1];
			serverPort = Integer.parseInt(args[2]);
			communication =  new ClientCommunication(listenPort);
		} catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
			communication =  new ClientCommunication();
		}
		communication.open();

		VirtualClient client = new VirtualClient(communication, new ClientModel(),
				new IntelligenceNormal());
		client.connect(new InetSocketAddress(serverHostname,serverPort));

	}

}
