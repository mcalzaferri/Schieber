package client.test;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.VirtualClient;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import client.ClientController;
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
		//ClientCommunicationSimulator sim = new ClientCommunicationSimulator(null);
		ClientModel model = new ClientModel();
		Player[] players = new Player[5];
		players[0] = new Player(null, "Enemy1", 2,false,true,false,1);
		players[1] = new Player(null, "Enemy2", 4,false,true,false,2);
		players[2] = new Player(null, "Friend", 3,false,true,false,3);
		players[3] = new Player(null, "YOU" , 1,false,true,false,4);
		players[4] = new Player(null, "PlayerInLobby", 0,false,false,false,0);
		ClientModelView modelView = new ClientModelView(model, players);
		/* Test Bot
		BotIntelligence ki = new IntelligenceNormal();
		VirtualClient bot = new VirtualClient(com);
		bot.setIntelligence(ki);
		com.setClient(bot);
		*/
	
	}

}


