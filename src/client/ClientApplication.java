package client;

import java.io.IOException;

import client.shared.ClientModel;
import client.test.ClientModelView;
import gui.Gui;

public class ClientApplication {
	
	private static ClientCommunication communication;
	
	public static void main(String[] args) throws IOException {
		communication =  new ClientCommunication();
		ClientModel model = new ClientModel();
		new ClientModelView(model);
		ClientController client = new ClientController(communication, model,
				new Gui(model));
		communication.setClient(client);
		client.setRefreshDelay(0);
	}
}
