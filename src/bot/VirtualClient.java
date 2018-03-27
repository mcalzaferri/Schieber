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
	public void doUpdateScore(Score score) {
		this.score = score;
	}

	@Override
	public void doEndRound() {
		// we could do some checks here, but otherwise not interesting for bot(?)
	}

	@Override
	public void doEndGame(Team winner) {
		super.disconnect();
		//disable bot
		this.active = false;
		//destroy intelligence = reset bot
		this.ki = null;
	}

	@Override
	public void doSetSeat(int seatId) {
		mySeatId = seatId;
	}

	@Override
	public void doUpdateActiveSeat(int activeSeatId) {
		if(activeSeatId == mySeatId) {
			Card card = ki.getNextCard();
			super.publishChosenCard(card);
		}
	}

	@Override
	public void doRequestTrump(boolean canSwitch) {
		Trump trump = ki.selectTrump(canSwitch);
		super.publishChosenTrump(trump);
		
	}
	
	//TODO: @Override
	public void doRequestWeis() {
		//TODO: needed in AbstractClient?
		ArrayList<Weis> weise = ki.getWeise();
		for(Weis w : weise) {
			// TODO: super.publishChosenWeis(w);
		}
		
	}

	@Override
	public void doUpdateDeck(Card[] deckCards) {
		int[] deckCardIds = cardsToIds(deckCards);
		ki.setDeck(deckCardIds);
		
	}

	@Override
	public void doUpdateHand(Card[] handCards) {
		int[] handCardIds = cardsToIds(handCards);
		ki.setHand(handCardIds);
		
	}

	@Override
	public void doSetTrump(Trump trump) {
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
	public void doConnected() {
		active = true;
		
	}

	@Override
	public void doDisconnected() {
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
