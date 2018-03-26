package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.ClientCommunication;

class ServerTest {
	MessageHandler msgHandler;
	GameLogic logic;
	ClientCommunication cCom;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		logic = new GameLogic();
		msgHandler = new MessageHandler(logic, 65000);
		msgHandler.setReceiveTimeout(100);

		ClientCommunication cCom = new ClientCommunication(
				new InetSocketAddress("localhost", 65000), 64000);
		cCom.open();
		cCom.setReceiveTimeout(100);

		logic.startGame();
	}

	@AfterEach
	void tearDown() throws Exception {
		msgHandler.stop();
		cCom.close();
	}

	@Test
	void testPlayerJoin() throws ClassNotFoundException, IOException {
		// cCom.connect();
		try {
			msgHandler.handleMessage();
		} catch (SocketTimeoutException e) {
			fail("no message received");
		}
		assertEquals(1, logic.getPlayerCount());
	}
}
