package client;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.VirtualClient;
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
		
		/* Test Bot
		BotIntelligence ki = new IntelligenceNormal();
		VirtualClient bot = new VirtualClient(com);
		bot.setIntelligence(ki);
		com.setClient(bot);
		*/
	
	}

}


