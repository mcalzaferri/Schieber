package server.exceptions;

/**
 * Thrown when an error occurs that concerns all players.
 */
public class GameException extends Exception {
	public GameException(String message) {
		super(message);
	}
}