package server;

public interface ClientConnectionClosedListener {
	/**
	 * Remove client connection
	 * This function is called by a client connection when the client
	 * disconnects. The associated player can then be removed from the game by
	 * the listener.
	 */
	void connectionClosed(ClientConnection con);
}
