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
import server.states.GameState;
import server.states.LobbyState;

public class ServerTest {
	final int serverListenPort = 65000;
	final int clientListenPort = 64000;
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
		msgHandler = new MessageHandler(logic, serverListenPort);
		msgHandler.setReceiveTimeout(100);
		GameState.init(msgHandler, logic);

		cCom = new ClientCommunication(clientListenPort);
		cCom.open();
		cCom.setReceiveTimeout(100);
	}

	@After
	public void tearDown() throws Exception {
		msgHandler.stop();
		cCom.close();
	}

	@Test
	public void testJoinLobby() throws ClassNotFoundException, IOException {
		msgHandler.changeState(new LobbyState());
		cCom.connect(new InetSocketAddress("localhost", serverListenPort));

		handleMessage();

		assertEquals(1, logic.getPlayerCount());
		assertEquals(0, logic.getPlayer(
				new InetSocketAddress("localhost", clientListenPort)).getSeatNr());
	}

	@Test
	public void testJoinTable() throws ClassNotFoundException, IOException {
		msgHandler.changeState(new LobbyState());
		cCom.connect(new InetSocketAddress("localhost", serverListenPort));
		handleMessage();

		JoinTableMessage msg = new JoinTableMessage();
		msg.preferedSeat = new SeatEntity();
		msg.preferedSeat.seatNr = 1;
		cCom.send(msg);
		handleMessage();
		assertEquals(msg.preferedSeat.seatNr, logic.getPlayer(
				new InetSocketAddress("localhost", clientListenPort)).getSeatNr());
	}

	private void handleMessage() throws ClassNotFoundException, IOException {
		try {
			msgHandler.handleMessage();
		} catch (SocketTimeoutException e) {
			fail("no message received");
		}
	}
}
