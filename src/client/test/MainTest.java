package client.test;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.VirtualClient;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import client.ClientController;
import gui.ClientModelTest;
import gui.Gui;
import shared.client.AbstractClient;
import shared.client.ClientModel;
import shared.*;

@SuppressWarnings("all")
public class MainTest {

	public static void main(String[] args) {
		boolean testBot = true;
		Player[] players = new Player[5];
		players[0] = new Player(null, "Enemy1", Seat.LEFTENEMY,false,true,false,1);
		players[1] = new Player(null, "Enemy2", Seat.RIGHTENEMY,false,true,false,2);
		players[2] = new Player(null, "Friend", Seat.PARTNER,false,true,false,3);
		players[3] = new Player(null, "YOU" , Seat.CLIENT,false,true,false,4);
		players[4] = new Player(null, "PlayerInLobby", Seat.NOTATTABLE,false,false,false,0);
		ClientModel model = new ClientModel();
		ClientModelView view = new ClientModelView(model, players);
		ClientCommunicationSimulator sim = new ClientCommunicationSimulator();
		AbstractClient client;
		if(testBot) {
			client = new VirtualClient(sim, model);
		}else {
			 client = new ClientController(sim, model, new Gui(model));
		}
		sim.setClient(client);
	}

}


