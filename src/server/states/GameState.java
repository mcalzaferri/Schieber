package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.ToServerMessage;
import server.GameLogic;
import server.exceptions.ClientErrorException;
import server.exceptions.UnhandledMessageException;
import shared.Communication;
import shared.Player;

/**
 * Abstract game state used for the state machine.
 * Concrete states extend this class and implement the handleMessage and/or act
 * function.
 * The static data fields are used by the states to send messages and to talk
 * to the game engine.
 * State diagram with all messages:
 * <img src="{@docRoot}/common/StateDiagram.png">
 */
public abstract class GameState {
	static protected Communication com;
	static protected GameLogic logic;
	static protected StateMachine stateMachine;

	/**
	 * Initialize game state
	 * @param sc used to switch to new states
	 * @param c used to dispatch messages
	 * @param l used to talk to the game engine
	 */
	static public void init(StateMachine sc, Communication c, GameLogic l) {
		stateMachine = sc;
		com = c;
		logic = l;
	}

	/**
	 * Handle message
	 * States that handle messages override this function.
	 * @param sender the player that sent the message
	 * @param msg the sent message
	 * @throws IOException
	 * @throws UnhandledMessageException
	 * @throws ClientErrorException
	 */
	public void handleMessage(Player sender, ToServerMessage msg) throws IOException, UnhandledMessageException, ClientErrorException {
		throw(new UnhandledMessageException());
	}

	/**
	 * Trigger state action
	 * This function is called when the state machine switches to this state.
	 * States with an entry action override this function.
	 */
	public void act() throws IOException { }

	/*
	 *  Helper functions for states
	 */

	/**
	 * Send a message to all players
	 * @param msg message to broadcast
	 */
	public static void broadcast(Message msg) throws IOException {
		for (Player p : logic.getPlayers()) {
			send(msg, p);
		}
	}

	/**
	 * Send a message to the specified player
	 * @param msg message to send
	 * @param player recipient
	 * @throws IOException
	 */
	public static void send(Message msg, Player player) throws IOException {
		com.send(msg, player.getSocketAddress());
//		System.out.println("sent " + msg.getClass().getSimpleName() +
//				" to " + player);
	}
}
