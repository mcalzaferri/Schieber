package test;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bot.IntelligenceNormal;
import bot.VirtualClient;
import client.ClientCommunication;
import server.GameLogic;
import server.MessageHandler;
import shared.Communication;

public class ServerBotIntegrationTest {
	final int serverListenPort = 65000;
	final int clientListenPort = 64000;
	ClientCommunication cCom;
	VirtualClient client;
	MessageHandler msgHandler;
	GameLogic logic;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		logic = new GameLogic();
		msgHandler = new MessageHandler(logic, serverListenPort);
		msgHandler.setReceiveTimeout(100);

		cCom =  new ClientCommunication();
		cCom.setReceiveTimeout(100);
		cCom.open();
		client = new VirtualClient(cCom);
		client.setIntelligence(new IntelligenceNormal());

	}

	@After
	public void tearDown() throws Exception {
		msgHandler.stop();
		cCom.close();
	}

	@Test
	public void chooseTrumpTest() {
		cCom.connect(new InetSocketAddress("localhost",serverListenPort));
		// TODO implement test
	}

}
