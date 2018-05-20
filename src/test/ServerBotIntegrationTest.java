package test;

import java.io.IOException;

import bot.BotApplication;
import server.ServerApp;

public class ServerBotIntegrationTest {
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

		// Start 4 bots
		for (int i = 0; i < 4; i++) {
			BotApplication.start("localhost", serverListenPort);
		}
	}
}
