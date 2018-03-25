package client;

import client.ClientController;
import client.test.ClientCommunicationSimulator;
import gui.ClientModelTest;
import gui.Gui;
import shared.client.ClientModel;
import shared.*;

public class MainTest {

	public static void main(String[] args) {
		ClientModel model = new ClientModelTest();
		Gui gui = new Gui(model);
		ClientCommunicationSimulator com = new ClientCommunicationSimulator();
		ClientController controller = new ClientController(com, model, gui);
		com.setClient(controller);
	}

}


