package bot;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;

import client.BadResultException;
import client.ClientCommunication;
import shared.Card;
import shared.Player;
import shared.Score;
import shared.Trump;
import shared.Weis;
import shared.client.AbstractClient;
import shared.client.ClientModel;

public class VirtualClient extends AbstractClient {

	private BotIntelligence ki; // set normal intelligence by default
	public Boolean active;
	private int mySeatId;
	private Score score;
	private static String[] possibleBotNames = {"cat-bot","dog-bot","hot-bot","nt-bot","not-a-bot","definitely-not-a-bot","ro-bot","TheLegend27"};
	
	public VirtualClient(ClientCommunication com, ClientModel model, InetSocketAddress serverAddress, BotIntelligence intelligence) {
		super(com, model);
		setIntelligence(intelligence);
		boolean connected = false;
		Random rm = new Random();
		do {
			try {
				connect(serverAddress, possibleBotNames[rm.nextInt(possibleBotNames.length)], true);
				//Wird keine Exception geworfen wurde connect erfolgreich durchgef�hrt
				connected = true;
			} catch (BadResultException e) {
				System.err.println("Connect fehlgeschlagen mit Fehlermessage: " + e.getMessage());
			}
		}while(!connected);
		
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
	public void doEndGame() {
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
	}

	@Override
	public void doRequestTrump(boolean canSwitch) {
		super.publishChosenTrump(ki.selectTrump(canSwitch));
		ki.setTrumpfGemacht(true);
		if(!canSwitch) {
			ki.setGeschoben(true);
		}
		
	}
	
	public void doRequestWeis() {
		ArrayList<Weis> wiisAl = ki.getWeise();
		Weis[] wiis = new Weis[wiisAl.size()];
		super.publishChosenWiis(wiisAl.toArray(wiis));
		}
	
	public void doPlayerShowedWiis(Weis[] wiis, Player player) {
		ki.showWeis(wiis, player.getId());
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

	@Override
	protected void doRequestCard(boolean selectWiis) {
		super.publishChosenCard(ki.getNextCard());
		if(selectWiis) {
			doRequestWeis();
		}
	}

	@Override
	protected void doHandleBadResultException(BadResultException e) {
		//TODO handle bad result
		System.err.println(e.getMessage());
		
	}
	

}
