package shared.client;

import java.awt.List;
import java.util.ArrayList;

import client.ClientCommunication;
import shared.*;

public abstract class AbstractClient {
	//Fields
	private ClientCommunication com;
	protected ClientModel model;
	
	//Constructors
	public AbstractClient(ClientCommunication com, ClientModel model) {
		this.com = com;
		this.model = model;
	}
	
	public AbstractClient(ClientCommunication com) {
		this(com, new ClientModel());
	}
	
	//Methods for Communication -> Client
	//server_info_messages
	private void chosenGameModeInfoMessageReceived() {};
	private void endOfRoundMessageReceived() {};
	private void gameOverMessageReceived() {};
	private void gameStartedInfoMessageReceived() {};
	private void newRoundInfoMessageReceived() {};
	private void playerJoinedInfoMessageReceived() {};
	private void playerLeftInfoMessageReceived() {};
	private void playerLeftTableInfoMessageReceived() {};
	private void turnInfoMessageReceived() {};
	//server_messages
	private void chooseGameModeMessageReceived() {};
	private void handOutCardsMessageReceived() {};
	private void wrongCardMessageReceived() {};
	private void yourTurnMessageReceived() {};
	
	
	//Template methods for Server -> Client
	public abstract void doSetTrump(Trump trump);
	
	/** Sets the seatId of the Client
	 * @param seatId
	 */
	public abstract void doSetSeat(int seatId);
	
	/** Determines which seat is currently playing.
	 * If activeSeatId == seatId use publishChosenCard() to finish turn
	 * @param activeSeatId
	 */
	public abstract void doUpdateActiveSeat(int activeSeatId);
	
	/**Initialises trump selection process. Finish process with publishChosenTrump()
	 * @param canSwitch determines whether the client can use "SCHIEBEN" or not
	 */
	public abstract void doRequestTrump(boolean canSwitch);
	
	public abstract void doRequestWeis();
	
	public abstract void doUpdateDeck(Card[] deckCards);
	
	public abstract void doUpdateHand(Card[] handCards);
	
	public abstract void doUpdateScore(Score score);
	
	public abstract void doEndRound();
	
	public abstract void doEndGame(Team winner);
	
	public abstract void doConnected();
	
	public abstract void doDisconnected();

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
