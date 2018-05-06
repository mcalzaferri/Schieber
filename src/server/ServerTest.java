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

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.JoinTableMessage;
import shared.Communication;
import shared.Seat;

/**
 * Unit tests for the game engine and state machine.
 */
public class ServerTest {
	private final int serverListenPort = 65000;
	private final int clientListenPort = 64000;
	private final InetSocketAddress serverAddr = new InetSocketAddress("localhost", serverListenPort);
	private final InetSocketAddress clientAddr = new InetSocketAddress("localhost", clientListenPort);
	private ServerApp app;
	private Communication client;

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

		client = new Communication(clientListenPort);
		client.open();
		client.setReceiveTimeout(100);
	}

	@After
	public void tearDown() throws Exception {
		app.stop();
		client.close();
	}

	@Test
	public void testJoinTable() throws Exception {
		JoinLobbyMessage jlMsg = new JoinLobbyMessage();
		jlMsg.playerData = new PlayerEntity();
		sendMsgToServer(jlMsg);

		waitForMessage();

		assertEquals(1, app.logic.getPlayerCount());
		assertEquals(Seat.NOTATTABLE, app.logic.getPlayer(clientAddr).getSeat());

		JoinTableMessage jtMsg = new JoinTableMessage();
		jtMsg.preferedSeat = SeatEntity.SEAT1;
		sendMsgToServer(jtMsg);

		waitForMessage();
		assertEquals(jtMsg.preferedSeat,
		             app.logic.getPlayer(clientAddr).getSeat().getSeatEntity());
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

	/**
	 * Helper function that sends a message from the client to the server
	 * @param msg message to send
	 * @throws IOException
	 */
	private void sendMsgToServer(ToServerMessage msg) throws IOException {
		client.send(msg, serverAddr);
	}
}
