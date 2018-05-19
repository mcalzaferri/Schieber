package test;

import java.io.IOException;

import bot.BotApplication;
import client.ClientApplication;
import server.ServerApp;

public class ServerGuiIntegrationTest {
	static final int serverListenPort = 65000;

	public static void main(String[] args) throws IOException {
		// Start server
		new Thread(() -> {
			try {
				ServerApp.start(serverListenPort);
			} catch (ClassNotFoundException|IOException e) {
				e.printStackTrace();
			}
		}).start();

		//Start Gui
		ClientApplication.main(null);

		// Start 3 bots
		for (int i = 0; i < 3; i++) {
			BotApplication.start("localhost",  serverListenPort);
		}
	}
}
