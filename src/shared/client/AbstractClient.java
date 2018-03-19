package shared.client;

import shared.*;

public abstract class AbstractClient {
	//Fields
	private ClientCommunicationInterface com;
	
	//Constructors
	public AbstractClient(ClientCommunicationInterface com) {
		this.com = com;
	}
	
	//Methods for Server -> Client
	protected abstract void setTrump(Trump trump);
	
	/** Sets the seatId of the Client
	 * @param seatId
	 */
	protected abstract void setSeat(int seatId);
	
	/** Determines which seat is currently playing.
	 * If activeSeatId == seatId use publishChosenCard() to finish turn
	 * @param activeSeatId
	 */
	protected abstract void updateActiveSeat(int activeSeatId);
	
	/**Initialises trump selection process. Finish process with publishChosenTrump()
	 * @param canSwitch determines whether the client can use "SCHIEBEN" or not
	 */
	protected abstract void requestTrump(boolean canSwitch);
	
	protected abstract void updateDeck(int[] deckCardIds);
	
	protected abstract void updateHand(int[] handCardIds);
	
	protected abstract void updateScore(Score score);
	
	protected abstract void endRound();
	
	protected abstract void endGame(Team winner);

	//Methods for Client -> Server
	protected void publishChosenTrump(Trump trump) {
		com.publishChosenTrump(trump);
	}
	
	protected void publishChosenCard(Card card) {
		com.publishChosenCard(card);
	}
	
	/**
	 * @param serverAddress Address to connect to
	 * @return true if successfully connected
	 */
	protected boolean connect(ServerAddress serverAddress) {
		return com.connect(serverAddress);
	}
	
	protected void disconnect() {
		com.disconnect();
	}
}
