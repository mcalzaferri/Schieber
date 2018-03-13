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
	
	protected abstract void setActivePlayer(Player activePlayer);
	
	protected abstract void updateDeck(CardList deck);
	
	protected abstract void updateHand(CardList hand);
	
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
	
	protected boolean connect(ServerAddress serverAddress) {
		return com.connect(serverAddress);
	}
	
	protected void disconnect() {
		com.disconnect();
	}
}
