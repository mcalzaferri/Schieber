package client.test;

import java.net.InetSocketAddress;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.VirtualClient;
import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToTableInfoMessage;
import client.ClientController;
import gui.ClientModelTest;
import gui.Gui;
import shared.client.AbstractClient;
import shared.client.ClientModel;
import shared.*;

@SuppressWarnings("all")
public class MainTest {

	public static void main(String[] args) {
		boolean testBot = false;
		PlayerEntity[] players = initialPlayers();
		ClientModel model = new ClientModel();
		model.setThisPlayer(new Player(players[3]));
		ClientModelView view = new ClientModelView(model, players);
		ClientCommunicationSimulator sim = new ClientCommunicationSimulator(players);
		AbstractClient client;
		if(testBot) {
			client = new VirtualClient(sim, model, new InetSocketAddress("localhost", 65000), new IntelligenceNormal());

		}else {
			 client = new ClientController(sim, model, new Gui(model));
		}
		sim.setClient(client);
		//Automation of a few messages for faster progress
		/*
		for(PlayerEntity player : players) {
			PlayerMovedToTableInfoMessage msg = new PlayerMovedToTableInfoMessage();
			msg.player = player;
			client.handleReceivedMessage(msg);
		}
		*/
	}
	public static PlayerEntity[] initialPlayers() {
		PlayerEntity[] players = new PlayerEntity[5];
		players[0] = new PlayerEntity();
		players[0].id = 1;
		players[0].isBot = false;
		players[0].name = "Enemy1";
		players[0].seat = SeatEntity.SEAT2;
		
		players[1] = new PlayerEntity();
		players[1].id = 2;
		players[1].isBot = false;
		players[1].name = "Enemy2";
		players[1].seat = SeatEntity.SEAT4;
		
		players[2] = new PlayerEntity();
		players[2].id = 3;
		players[2].isBot = false;
		players[2].name = "Friend";
		players[2].seat = SeatEntity.SEAT3;
		
		players[3] = new PlayerEntity();
		players[3].id = 4;
		players[3].isBot = false;
		players[3].name = "YOU";
		players[3].seat = SeatEntity.SEAT1;
		
		players[4] = new PlayerEntity();
		players[4].id = 5;
		players[4].isBot = false;
		players[4].name = "AnotherPlayer";
		players[4].seat = SeatEntity.NOTATTABLE;
		return players;
	}

}


