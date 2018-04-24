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

import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.proto.player_messages.JoinTableMessage;
import client.ClientCommunication;
import server.states.LobbyState;

/**
 * Unit tests for the game engine and state machine.
 */
public class ServerTest {
	final int serverListenPort = 65000;
	final int clientListenPort = 64000;
	ServerApp app;
	ClientCommunication cCom;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		app = new ServerApp(serverListenPort);
		app.com.open();
		app.com.setReceiveTimeout(100);

		cCom = new ClientCommunication(clientListenPort);
		cCom.open();
		cCom.setReceiveTimeout(100);
	}

	@After
	public void tearDown() throws Exception {
		app.stop();
		cCom.close();
	}

	@Test
	public void testJoinLobby() throws Exception {
		cCom.connect(new InetSocketAddress("localhost", serverListenPort), "ServerTestJoinLobby", true);

		waitForMessage();

		assertEquals(1, app.logic.getPlayerCount());
		assertEquals(0, app.logic.getPlayer(
				new InetSocketAddress("localhost", clientListenPort)).getSeatNr());
	}

	@Test
	public void testJoinTable() throws Exception {
		app.stateMachine.changeState(new LobbyState());
		cCom.connect(new InetSocketAddress("localhost", serverListenPort), "ServerTestJoinTable", true);
		waitForMessage();

		JoinTableMessage msg = new JoinTableMessage();
		msg.preferedSeat = SeatEntity.SEAT1;
		cCom.send(msg);
		waitForMessage();
		assertEquals(msg.preferedSeat.getSeatNr(), app.logic.getPlayer(
				new InetSocketAddress("localhost", clientListenPort)).getSeatNr());
	}

	/**
	 * Helper function that waits for a message and fails if it times out.
	 */
	private void waitForMessage() throws ClassNotFoundException, IOException {
		try {
			app.handleMessage();
		} catch (SocketTimeoutException e) {
			fail("Timed out while waiting for the message.");
		}
	}
}
