package client;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.VirtualClient;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import client.ClientController;
import client.test.ClientCommunicationSimulator;
import client.test.MessageEnumeration;
import gui.ClientModelTest;
import gui.Gui;
import shared.client.ClientModel;
import shared.*;

public class MainTest {

	public static void main(String[] args) {
		/*
		ClientModel model = new ClientModelTest();
		Gui gui = new Gui(model);
		ClientCommunicationSimulator com = new ClientCommunicationSimulator();
		ClientController controller = new ClientController(com, model, gui);
		*/
		ClientCommunicationSimulator sim = new ClientCommunicationSimulator(null);
		/* Test Bot
		BotIntelligence ki = new IntelligenceNormal();
		VirtualClient bot = new VirtualClient(com);
		bot.setIntelligence(ki);
		com.setClient(bot);
		*/
	
	}

}


