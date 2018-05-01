package test;

import java.io.IOException;

import bot.BotApplication;
import client.ClientApplication;
import server.ServerApp;

public class ServerGuiIntegrationTest {
	static final int serverListenPort = 65000;
	static final int clientListenPort = 64000;

	public static void main(String[] args) throws IOException {
		// Start server
		Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerApp.main(new String[] {Integer.toString(serverListenPort)});
				} catch (ClassNotFoundException|IOException e) {
					e.printStackTrace();
				}
			}
		});
		serverThread.start();

		//Start Gui
				ClientApplication.main(new String[] {Integer.toString(clientListenPort + 4),
							"localhost",  Integer.toString(serverListenPort)});
		// Start 4 bots
		for (int i = 0; i < 3; i++) {
			BotApplication.main(new String[] {Integer.toString(clientListenPort + i),
					"localhost",  Integer.toString(serverListenPort)});
		}
	}
}
