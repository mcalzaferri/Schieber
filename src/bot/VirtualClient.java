package bot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import client.BadResultException;
import client.ClientCommunication;
import client.shared.AbstractClient;
import client.shared.ClientModel;
import shared.Card;
import shared.Player;
import shared.Score;
import shared.Seat;
import shared.Team;
import shared.Trump;
import shared.Weis;

/**
 * This class connects a virtual player (Bot) to the Client classes and therefore the communication functions to the server.
 * It inherits from AbstractClient where also the GUI client inherits from.
 *
 */

public class VirtualClient extends AbstractClient {

	private BotIntelligence ki; // set normal intelligence by default
	public Boolean active;
	private static ArrayList<String> possibleBotNames;

	public VirtualClient(ClientCommunication com, ClientModel model, BotIntelligence intelligence) {
		super(com, model);
		setIntelligence(intelligence);
		
		setBotNames();
		
	}

	private void setBotNames() { // read botnames from a textfile
		Scanner in;
		possibleBotNames = new ArrayList<>();
		try {
			// use "botnames.txt" or "femaleBotNames.txt"
			in = new Scanner(new FileReader("botnames.txt"));
			in.useDelimiter("\n");
			while(in.hasNext()) {
			    possibleBotNames.add(in.next().replace('\r', ' ').trim());
			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}

	public void connect(InetSocketAddress serverAddress, Seat preferedSeat) {
		boolean connected = false;
		Random rm = new Random();
		do {
			try {
				String botName = possibleBotNames.get(rm.nextInt(possibleBotNames.size()));
				ki.setName(botName);
				connect(serverAddress, botName, true, preferedSeat);
				//Wird keine Exception geworfen wurde connect erfolgreich durchgef�hrt
				connected = true;
			} catch (BadResultException e) {
				System.err.println("Connect fehlgeschlagen mit Fehlermessage: " + e.getMessage());
			}
		} while(!connected);
	}
	
	@Override
	// bot doesn't really care about score
	public void doUpdateScore(Score score) {
	}

	@Override
	public void doEndRound() {
		ki.resetBot();
	} 

	@Override
	public void doEndGame(Team teamThatWon) {
		this.ki = new IntelligenceNormal();
	}

	@Override
	public void doSetSeat(int seatId) {
		seatId--; // zero based for bot, one based for client
		ki.setSelfID(seatId);
		ki.setPartnerID((seatId+2)%4);
		ki.setEnemyLeftID((seatId+1)%4);
		ki.setEnemyRightID((seatId+3)%4);
	}

	@Override
	public void doUpdateActiveSeat(int activeSeatId) {
		ki.setActivePlayerID(activeSeatId-1); // zero based for bot, one based for client
	}

	@Override
	public void doRequestTrump(boolean canSwitch) {
		super.publishChosenTrump(ki.selectTrump(canSwitch));
		ki.setTrumpfGemacht(true);
		if(!canSwitch) {
			ki.setGeschoben(true);
		}

	}
	
	@Override
	public void doPlayerShowedWiis(Weis[] wiis, Player player) {
		ki.showWeis(wiis, player.getId()-1); // zero based for Bot
	}

	@Override
	public void doUpdateDeck(Card[] deckCards) {
		ki.setDeck(cardsToIds(deckCards));
		ki.updateOutOfCardLists(cardsToIds(deckCards));
		
		if(deckCards.length == 4) {
			ki.updateMaxCards();
		}

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
		if(selectWiis) {
			ArrayList<Weis> wiisAl = ki.getWeise();
			Weis[] wiis = new Weis[wiisAl.size()];
			super.publishChosenWiis(wiisAl.toArray(wiis));
		}
		super.publishChosenCard(ki.getNextCard());
	}

	@Override
	protected void doHandleBadResultException(BadResultException e) {
		System.err.println(e.getMessage());
		
	}
	

}
