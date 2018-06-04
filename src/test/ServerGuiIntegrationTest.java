package test;

import java.io.IOException;

import bot.BotApplication;
import client.ClientApplication;
import server.ServerApp;
import shared.Seat;

public class ServerGuiIntegrationTest {
	static final int serverListenPort = 65000;

	public static void main(String[] args) {
		// Start server
		new Thread(() -> {
			try {
				ServerApp.start(serverListenPort);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

		//Start Gui
		ClientApplication.main(null);

		// Start Bots
//		for (int i = 0; i < 3; i++) {
			BotApplication.start("localhost",  serverListenPort, Seat.SEAT1);
//		}
	}
}
