package server.exceptions;

/**
 * Thrown when a player sends invalid data.
 */
public class InvalidMessageDataException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2115970229328842652L;

	public InvalidMessageDataException(String message) { super(message); }
}
