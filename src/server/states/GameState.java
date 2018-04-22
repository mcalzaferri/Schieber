package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import server.GameLogic;
import shared.Communication;
import shared.Player;

/**
 * Abstract game state used for the state machine.
 * Concrete states extend this class and implement the handleMessage and/or act
 * function.
 * The static data fields are used by the states to send messages and to talk
 * to the game engine.
 */
public abstract class GameState {
	static protected Communication com;
	static protected GameLogic logic;
	static protected StateMachine stateMachine;

	static public void init(StateMachine sc, Communication c, GameLogic l) {
		stateMachine = sc;
		com = c;
		logic = l;
	}

	/**
	 * Handle message
	 * States that handle messages override this function.
	 * @param sender the player that sent the message
	 * @param msg the message
	 * @return true if it was handled successfully, false otherwise
	 * @throws IOException
	 */
	public boolean handleMessage(Player sender, ToServerMessage msg) throws IOException {
		return false;
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
	 * Helper function that sends a message to all players.
	 * @param msg message to broadcast
	 */
	public static void broadcast(Message msg) throws IOException {
		for (Player p : logic.getPlayers()) {
			send(msg, p);
		}
	}

	public static void send(Message msg, Player player) throws IOException {
		com.send(msg, player.getSocketAddress());
	}

	public static void sendResultMsg(ResultMessage.Code errorCode,
			String errorText, Player player) throws IOException {
		ResultMessage resMsg = new ResultMessage();
		resMsg.code = errorCode;
		resMsg.message = errorText;
		send(resMsg, player);
	}
}
