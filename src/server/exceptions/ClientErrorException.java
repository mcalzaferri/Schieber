package server.exceptions;

/**
 * Thrown when a player sends an illegal message like trying to place a card
 * when its not his move.
 */
public class ClientErrorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2604548168328188810L;

	public ClientErrorException(String message) {
		super(message);
	}
}