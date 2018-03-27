package shared.client;

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
	public void chosenTrumpInfoMessageReceived(Trump trump) {
		model.setTrump(trump);
		doSetTrump(model.getTrump());
	};
	public void endOfRoundMessageReceived() {
		model.setActiveSeatId(0);
		doEndRound();
	};
	public void gameOverMessageReceived(Team winner) {
		model.setActiveSeatId(0);
		model.setTrump(null);
		doEndGame(winner);
	};
	public void gameStartedInfoMessageReceived() {};
	public void newRoundInfoMessageReceived() {};
	public void playerJoinedInfoMessageReceived() {};
	public void playerLeftInfoMessageReceived() {};
	public void playerLeftTableInfoMessageReceived() {};
	public void turnInfoMessageReceived(Card laidCard) {};
	//server_messages
	public void chooseTrumpMessageReceived(boolean canSwitch) {
		model.setCanSwitch(canSwitch);
		doRequestTrump(canSwitch);
	};
	public void handOutCardsMessageReceived(Card[] cards) {
		model.getHand().updateData(cards);
		doUpdateHand(cards);
	};
	public void wrongCardMessageReceived() {};
	public void yourTurnMessageReceived(Card[] validCards) {};
	
	
	//Template methods for Server -> Client
	protected abstract void doSetTrump(Trump trump);
	
	/** Sets the seatId of the Client
	 * @param seatId
	 */
	protected abstract void doSetSeat(int seatId);
	
	/** Determines which seat is currently playing.
	 * If activeSeatId == seatId use publishChosenCard() to finish turn
	 * @param activeSeatId
	 */
	protected abstract void doUpdateActiveSeat(int activeSeatId);
	
	/**Initialises trump selection process. Finish process with publishChosenTrump()
	 * @param canSwitch determines whether the client can use "SCHIEBEN" or not
	 */
	protected abstract void doRequestTrump(boolean canSwitch);
	
	protected abstract void doRequestWeis();
	
	protected abstract void doUpdateDeck(Card[] deckCards);
	
	protected abstract void doUpdateHand(Card[] handCards);
	
	protected abstract void doUpdateScore(Score score);
	
	protected abstract void doEndRound();
	
	protected abstract void doEndGame(Team winner);
	
	protected abstract void doConnected();
	
	protected abstract void doDisconnected();

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
