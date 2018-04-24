package test;

import java.net.InetSocketAddress;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bot.IntelligenceNormal;
import bot.VirtualClient;
import client.ClientCommunication;
import server.ServerApp;
import shared.client.ClientModel;

public class ServerBotIntegrationTest {
	final int serverListenPort = 65000;
	final int clientListenPort = 64000;
	ClientCommunication cCom;
	VirtualClient client;
	ServerApp app;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		app = new ServerApp(serverListenPort);
		app.setReceiveTimeout(100);
		app.open();

		cCom =  new ClientCommunication();
		cCom.setReceiveTimeout(100);
		cCom.open();
		Random rm = new Random();
		client = new VirtualClient(cCom, new ClientModel(),new InetSocketAddress("146.136.43.84",65000),new IntelligenceNormal());
		client.setIntelligence(new IntelligenceNormal());

	}

	@After
	public void tearDown() throws Exception {
		app.stop();
		cCom.close();
	}

	@Test
	public void chooseTrumpTest() throws Exception {
		cCom.connect(new InetSocketAddress("localhost",serverListenPort), "ChooseTrumpTest", true);
		// TODO implement test
	}

}
