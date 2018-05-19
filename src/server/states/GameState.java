package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.player_messages.*;
import server.GameLogic;
import server.ServerCommunication;
import server.exceptions.ClientErrorException;
import server.exceptions.UnhandledMessageException;
import shared.Player;

/**
 * Abstract game state used for the state machine.
 * Concrete states extend this class and implement the handleMessage and/or act
 * function.
 * The static data fields are used by the states to send messages and to talk
 * to the game engine.
 */
public abstract class GameState {
	static protected ServerCommunication com;
	static protected GameLogic logic;
	static protected StateMachine stateMachine;

	/**
	 * Initialize game state
	 *
	 * @param sc used to switch to new states
	 * @param c  used to dispatch messages
	 * @param l  used to talk to the game engine
	 */
	static public void init(StateMachine sc, ServerCommunication c, GameLogic l) {
		stateMachine = sc;
		com = c;
		logic = l;
	}

	/**
	 * Handle message
	 * States that handle messages override this function.
	 *
	 * @param sender the player that sent the message
	 * @param msg    the sent message
	 */
	public void handleMessage(Player sender, JoinTableMessage msg)
			throws UnhandledMessageException, IOException {
		throw (new UnhandledMessageException());
	}

	/**
	 * @see GameState#handleMessage(Player, JoinTableMessage)
	 */
	public void handleMessage(Player sender, ChangeStateMessage msg)
			throws UnhandledMessageException, IOException {
		throw (new UnhandledMessageException());
	}

	/**
	 * @see GameState#handleMessage(Player, JoinTableMessage)
	 */
	public void handleMessage(Player sender, FillEmptySeatsMessage msg)
			throws UnhandledMessageException {
		throw (new UnhandledMessageException());
	}

	/**
	 * @see GameState#handleMessage(Player, JoinTableMessage)
	 */
	public void handleMessage(Player sender, ChosenTrumpMessage msg)
			throws UnhandledMessageException, IOException, ClientErrorException {
		throw (new UnhandledMessageException());
	}

	/**
	 * @see GameState#handleMessage(Player, JoinTableMessage)
	 */
	public void handleMessage(Player sender, ChosenWiisMessage msg)
			throws UnhandledMessageException, IOException, ClientErrorException {
		throw (new UnhandledMessageException());
	}

	/**
	 * @see GameState#handleMessage(Player, JoinTableMessage)
	 */
	public void handleMessage(Player sender, PlaceCardMessage msg)
			throws UnhandledMessageException, ClientErrorException, IOException {
		throw (new UnhandledMessageException());
	}

	/**
	 * Trigger state entry action
	 * This function is called when the state machine switches to this state.
	 * States with an entry action override this function.
	 */
	public void act() throws IOException { }
}