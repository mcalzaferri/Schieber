package server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.ntb.jass.common.proto.Message;
import client.ClientCommunication;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.proto.player_messages.JoinLobbyMessage;
import ch.ntb.jass.common.proto.player_messages.JoinTableMessage;
import server.states.GameState;
import server.states.LobbyState;
import server.states.StateMachine;
import shared.Player;
import shared.Seat;

import static org.junit.Assert.*;

class SchieberMsgBuffer implements SchieberMessageHandler {
	private List<PlayerMessagePair> msgQueue;
	private StateMachine sm;

	class PlayerMessagePair {
		public Player p;
		public Message msg;
		public PlayerMessagePair(Player p, Message msg) {
			this.p = p;
			this.msg = msg;
		}
	}

	public SchieberMsgBuffer(StateMachine sm) {
		msgQueue = new ArrayList<>();
		this.sm = sm;
	}

	@Override
	synchronized public void handleMessage(Player sender, Message msg) {
		msgQueue.add(new PlayerMessagePair(sender, msg));
	}

	/**
	 * Wait for a message and fail if it takes too long
	 */
	public void waitForMsg() {
		for (int i = 0; i < 10; i++) {
			if (!msgQueue.isEmpty()) {
				PlayerMessagePair pmp = msgQueue.remove(0);
				try {
					// forward message to state machine
					sm.handleMessage(pmp.p, pmp.msg);
					return;
				} catch (Exception e) {
					e.printStackTrace();
					fail();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		fail("Timed out while waiting for the message.");
	}
}

/**
 * Unit tests for the game engine and state machine.
 */
public class ServerTest {
	private final int serverListenPort = 65000;
	private final InetSocketAddress serverAddr = new InetSocketAddress("localhost", serverListenPort);

	private ServerCommunication com;
	private StateMachine stateMachine;
	private GameLogic logic;
	private SchieberMsgBuffer msgBuf;

	private ClientCommunication client;

	@BeforeClass
	public static void setUpBeforeClass() {
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() throws Exception {

		// server setup

		com = new ServerCommunication();
		logic = new GameLogic();

		stateMachine = new StateMachine();
		GameState.init(stateMachine, com, logic);
		stateMachine.changeState(new LobbyState());

		msgBuf = new SchieberMsgBuffer(stateMachine);

		com.setListenPort(serverListenPort);
		com.open();

		new Thread(() -> com.accept(msgBuf, stateMachine) ).start();

		// client setup

		client = new ClientCommunication();
		client.open(serverAddr);
	}

	@After
	public void tearDown() {
		client.close();
		com.close();
	}


	@Test
	public void testJoinTable() {
		JoinLobbyMessage jlMsg = new JoinLobbyMessage();
		jlMsg.playerData = new PlayerEntity();
		client.send(jlMsg);

		msgBuf.waitForMsg();

		assertEquals(1, logic.getPlayerCount());
		Collection<Player> players = logic.getPlayers();
		assertEquals(1, players.size());
		Player player1 = players.iterator().next();
		assertEquals(Seat.NOTATTABLE, player1.getSeat());
		assertFalse(player1.isReady());
		assertFalse(player1.isAtTable());
		assertTrue(player1.isInLobby());

		JoinTableMessage jtMsg = new JoinTableMessage();
		jtMsg.preferedSeat = SeatEntity.SEAT1;
		client.send(jtMsg);

		msgBuf.waitForMsg();
		assertEquals(Seat.getBySeatNr(jtMsg.preferedSeat.getSeatNr()), player1.getSeat());
		assertEquals(logic.getPlayer(Seat.getBySeatNr(jtMsg.preferedSeat.getSeatNr())), player1);
	}
}
