package bot;

import shared.Card;
import shared.CardList;
import shared.ClientCommunicationInterface;
import shared.Communication;
import shared.Player;
import shared.Score;
import shared.ServerAddress;
import shared.Team;
import shared.Trump;
import shared.Weis;
import shared.client.AbstractClient;
import shared.proto.ChooseGameModeMessage;
import shared.proto.JoinGameMessage;
import shared.proto.Message;
import shared.proto.PlaceCardMessage;
import shared.proto.ToPlayerMessage;

public class VirtualClient extends AbstractClient {

	private ClientCommunicationInterface com;
	private BotIntelligence ki = new IntelligenceRandom(); // set random intelligence by default
	public Boolean active = true;
	private int mySeatId;
	private Score score;
	
	
	public VirtualClient(ClientCommunicationInterface communication, ServerAddress address) {
		super(communication);
		this.com = communication;
		
		while(!super.connect(address)) {
			// try to connect
		}
	
	}


	@Override
	// bot doesn't really care about score, but we still store it
	public void updateScore(Score score) {
		this.score = score;
		
	}

	@Override
	public void endRound() {
		// we could do some checks here, but otherwise not interesting for bot(?)
	}

	@Override
	public void endGame(Team winner) {
		super.disconnect();
		//disable bot
		this.active = false;
		//destroy intelligence = reset bot
		this.ki = null;
	}


	@Override
	protected void setSeat(int seatId) {
		mySeatId = seatId;
		
	}

	@Override
	protected void updateActiveSeat(int activeSeatId) {
		if(activeSeatId == mySeatId) {
			Card card = ki.getNextCard();
			super.publishChosenCard(card);
			
		}
		
	}

	@Override
	protected void requestTrump(boolean canSwitch) {
		Trump trump = ki.selectTrump(canSwitch);
		super.publishChosenTrump(trump);
		
	}

	@Override
	protected void updateDeck(int[] deckCardIds) {
		ki.setDeck(deckCardIds);
		
	}

	@Override
	protected void updateHand(int[] handCardIds) {
		ki.setHand(handCardIds);
		
	}

	@Override
	protected void setTrump(Trump trump) {
		ki.setTrump(trump);
		
	}
	
	/**
	 * set strategy for the Schieber bot
	 * @param intelligence
	 */
	public void setIntelligence(BotIntelligence intelligence) {
		ki = intelligence;
	}
	

}
