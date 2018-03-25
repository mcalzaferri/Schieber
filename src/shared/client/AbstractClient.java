package shared.client;

import java.awt.List;
import java.util.ArrayList;

import client.ClientCommunication;
import shared.*;

public abstract class AbstractClient {
	//Fields
	private ClientCommunication com;
	
	//Constructors
	public AbstractClient(ClientCommunication com) {
		this.com = com;
	}
	
	//Methods for Server -> Client
	public abstract void setTrump(Trump trump);
	
	/** Sets the seatId of the Client
	 * @param seatId
	 */
	public abstract void setSeat(int seatId);
	
	/** Determines which seat is currently playing.
	 * If activeSeatId == seatId use publishChosenCard() to finish turn
	 * @param activeSeatId
	 */
	public abstract void updateActiveSeat(int activeSeatId);
	
	/**Initialises trump selection process. Finish process with publishChosenTrump()
	 * @param canSwitch determines whether the client can use "SCHIEBEN" or not
	 */
	public abstract void requestTrump(boolean canSwitch);
	
	public abstract void requestWeis();
	
	public abstract void updateDeck(Card[] deckCards);
	
	public abstract void updateHand(Card[] handCards);
	
	public abstract void updateScore(Score score);
	
	public abstract void endRound();
	
	public abstract void endGame(Team winner);
	
	public abstract void connected();
	
	public abstract void disconnected();

	//Methods for Client -> Server
	public void publishChosenTrump(Trump trump) {
		com.publishChosenTrump(trump);
	}
	
	public void publishChosenCard(Card card) {
		com.publishChosenCard(card);
	}
	
	public void publishChosenWeis(Weis[] wiis) {
		com.publishChosenWiis(wiis);
	}
	
	/**
	 * @param serverAddress Address to connect to
	 * @return true if successfully connected
	 */
	public void connect(ServerAddress serverAddress) {
		 com.connect(serverAddress);
	}
	
	public void disconnect() {
		com.disconnect();
	}
}
