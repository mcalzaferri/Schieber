package server.exceptions;

/**
 * Thrown when a player sends invalid data.
 */
public class InvalidMessageDataException extends Exception {
	public InvalidMessageDataException(String message) { super(message); }
}
