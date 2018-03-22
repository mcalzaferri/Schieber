package bot;

import java.util.ArrayList;

import client.ClientCommunication;
import shared.Card;
import shared.Score;
import shared.Team;
import shared.Trump;
import shared.Weis;
import shared.client.AbstractClient;

public class VirtualClient extends AbstractClient {

	private BotIntelligence ki = new IntelligenceRandom(); // set random intelligence by default
	public Boolean active;
	private int mySeatId;
	private Score score;
	
	public VirtualClient(ClientCommunication com) {
		super(com);
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
	
	//TODO: @Override
	protected void requestWeis() {
		//TODO: needed in AbstractClient?
		ArrayList<Weis> weise = ki.getWeise();
		for(Weis w : weise) {
			// TODO: super.publishChosenWeis(w);
		}
		
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

	@Override
	protected void connected() {
		active = true;
		
	}

	@Override
	protected void disconnected() {
		active = false;
		
	}
	

}
