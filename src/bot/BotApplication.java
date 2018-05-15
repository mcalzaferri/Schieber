package bot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Random;

import client.ClientCommunication;
import client.shared.ClientModel;
import client.test.ClientModelView;
import shared.Communication;

public class BotApplication {

	private static ClientCommunication communication;

	public static void main(String[] args) throws IOException {
		int listenPort = Integer.parseInt(args[0]);
		String serverHostname = args[1];
		int serverPort = Integer.parseInt(args[2]);
		start(listenPort, serverHostname, serverPort);
	}

	public static void start(int listenPort, String serverHostname,
			int serverPort) throws SocketException {
		communication = new ClientCommunication(listenPort);
		communication.open();

		ClientModel model = new ClientModel();
		new ClientModelView(model);
		VirtualClient client = new VirtualClient(communication, model,
		//		new IntelligenceMalicious()); // start cheating bot
				new IntelligenceNormal());
		communication.setClient(client);
		client.connect(new InetSocketAddress(serverHostname, serverPort));

	}

}
