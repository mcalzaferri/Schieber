package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bot.BotApplication;
import bot.IntelligenceNormal;
import bot.VirtualClient;
import client.ClientCommunication;
import server.ServerApp;
import shared.client.ClientModel;

public class ServerBotIntegrationTest {
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

		// Start 4 bots
		for (int i = 0; i < 4; i++) {
			BotApplication.main(new String[] {Integer.toString(clientListenPort + i),
					"localhost",  Integer.toString(serverListenPort)});
		}
	}
}
