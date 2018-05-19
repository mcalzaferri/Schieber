package bot;

import java.net.InetSocketAddress;

import client.ClientCommunication;
import client.shared.ClientModel;
import client.test.ClientModelView;

public class BotApplication {

	private static ClientCommunication communication;

	public static void main(String[] args) {
		String serverHostname = args[0];
		int serverPort = Integer.parseInt(args[1]);
		start(serverHostname, serverPort);
	}

	public static void start(String serverHostname, int serverPort) {
		communication = new ClientCommunication();

		ClientModel model = new ClientModel();
		new ClientModelView(model);
		VirtualClient client = new VirtualClient(communication, model,
		//		new IntelligenceMalicious()); // start cheating bot
				new IntelligenceNormal());
		communication.setClient(client);
		client.connect(new InetSocketAddress(serverHostname, serverPort));

	}

}
