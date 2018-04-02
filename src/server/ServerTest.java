package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import client.ClientCommunication;

public class ServerTest {
	MessageHandler msgHandler;
	GameLogic logic;
	ClientCommunication cCom;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		logic = new GameLogic();
		msgHandler = new MessageHandler(logic, 65000);
		msgHandler.setReceiveTimeout(100);

		cCom = new ClientCommunication();
		cCom.connect(new InetSocketAddress("localhost", 65000));
		cCom.open();
		cCom.setReceiveTimeout(100);

		logic.startGame();
	}

	@After
	public void tearDown() throws Exception {
		msgHandler.stop();
		cCom.close();
	}

	@Test
	public void testPlayerJoin() throws ClassNotFoundException, IOException {
		// cCom.connect();
		try {
			msgHandler.handleMessage();
		} catch (SocketTimeoutException e) {
			fail("no message received");
		}
		assertEquals(1, logic.getPlayerCount());
	}
}
