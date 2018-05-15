package test;

import java.io.IOException;

import bot.BotApplication;
import server.ServerApp;

public class ServerBotIntegrationTest {
	static final int serverListenPort = 65000;
	static final int clientListenPort = 64000;

	public static void main(String[] args) throws IOException {
		// Start server
		Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerApp.start(serverListenPort);
				} catch (ClassNotFoundException|IOException e) {
					e.printStackTrace();
				}
			}
		});
		serverThread.start();

		// Start 4 bots
		for (int i = 0; i < 4; i++) {
			BotApplication.start(clientListenPort + i, "localhost",
					serverListenPort);
		}
	}
}
