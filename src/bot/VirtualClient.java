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
	public void setSeat(int seatId) {
		mySeatId = seatId;
	}

	@Override
	public void updateActiveSeat(int activeSeatId) {
		if(activeSeatId == mySeatId) {
			Card card = ki.getNextCard();
			super.publishChosenCard(card);
		}
	}

	@Override
	public void requestTrump(boolean canSwitch) {
		Trump trump = ki.selectTrump(canSwitch);
		super.publishChosenTrump(trump);
		
	}
	
	//TODO: @Override
	public void requestWeis() {
		//TODO: needed in AbstractClient?
		ArrayList<Weis> weise = ki.getWeise();
		for(Weis w : weise) {
			// TODO: super.publishChosenWeis(w);
		}
		
	}

	@Override
	public void updateDeck(Card[] deckCards) {
		int[] deckCardIds = cardsToIds(deckCards);
		ki.setDeck(deckCardIds);
		
	}

	@Override
	public void updateHand(Card[] handCards) {
		int[] handCardIds = cardsToIds(handCards);
		ki.setHand(handCardIds);
		
	}

	@Override
	public void setTrump(Trump trump) {
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
	public void connected() {
		active = true;
		
	}

	@Override
	public void disconnected() {
		active = false;
		
	}
	
	/**
	 * auxiliary function to cope with changes on the AbstractClient Interface
	 * @param Card[]
	 * @return int[] IDs
	 */
	private int[] cardsToIds(Card[] cards) {
		int[] ids = new int[cards.length];
		for(int i=0; i<cards.length;i++) {
			ids[i] = cards[i].getId();
		}
		return ids;
	}
	

}
