package bot;

import java.util.ArrayList;

import client.ClientCommunication;
import shared.Card;
import shared.Player;
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
		// update max cards with knowledge from deck cards
		ki.updateMaxCards();
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
		ki.setSelfID(seatId);
		ki.setPartnerID((seatId+2)%4);
		ki.setEnemyLeftID((seatId+1)%4);
		ki.setEnemyRightID((seatId+3)%4);
	}

	@Override
	public void doUpdateActiveSeat(int activeSeatId) {
		ki.setActivePlayerID(activeSeatId);
		if(activeSeatId == mySeatId) {
			super.publishChosenCard(ki.getNextCard());
		}
	}

	@Override
	public void doRequestTrump(boolean canSwitch) {
		super.publishChosenTrump(ki.selectTrump(canSwitch));
		
	}
	
	@Override
	public void doRequestWeis() {
		super.publishChosenWeis((Weis[]) ki.getWeise().toArray()); 
	}
	
	public void doShowWeis(Weis[] wiis, int playerID) {
		ki.showWeis(wiis, playerID);
	}

	@Override
	public void doUpdateDeck(Card[] deckCards) {
		ki.setDeck(cardsToIds(deckCards));
		ki.updateOutOfCardLists(cardsToIds(deckCards));
		
	}

	@Override
	public void doUpdateHand(Card[] handCards) {
		ki.setHand(cardsToIds(handCards));
		
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
