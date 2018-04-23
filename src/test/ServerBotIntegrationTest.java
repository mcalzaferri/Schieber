package test;

import java.net.InetSocketAddress;

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
		client = new VirtualClient(cCom, new ClientModel());
		client.setIntelligence(new IntelligenceNormal());

	}

	@After
	public void tearDown() throws Exception {
		app.stop();
		cCom.close();
	}

	@Test
	public void chooseTrumpTest() {
		cCom.connect(new InetSocketAddress("localhost",serverListenPort), "ChooseTrumpTest");
		// TODO implement test
	}

}
