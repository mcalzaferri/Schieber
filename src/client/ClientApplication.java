package client;

import java.io.IOException;
import java.net.InetSocketAddress;

import bot.IntelligenceNormal;
import bot.VirtualClient;
import client.shared.ClientModel;
import client.test.ClientModelView;
import gui.Gui;
import shared.Communication;

public class ClientApplication {
	
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
		ClientModel model = new ClientModel();
		new ClientModelView(model);
		ClientController client = new ClientController(communication, model,
				new Gui(model));
		communication.setClient(client);
		client.setRefreshDelay(500);
	}
}
