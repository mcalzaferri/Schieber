package server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

import ch.ntb.jass.common.proto.*;
import shared.*;

public class GameLogic {
	private ServerCommunication com;
	private GameMode mode;
	private CardColor trumpf;
	private Hashtable<Integer, Player> players;
	private Card[] deck;

	public GameLogic(Communication com) {
		players = new Hashtable<>();
		deck = new Card[36];
		// fill deck with cards
	}

	/**
	 * @param p
	 * @return False if table is full (4 players), true otherwise;
	 */
	public boolean addPlayer(Player p) {
		players.put(p.getId(), p);
		return false;
	}

	/**
	 * Place a card.
	 * After the fourth card has been placed the round is over.
	 * @return False if move is invalid, true otherwise.
	 */
	public boolean placeCard(Card c) {
		return false;
	}

	public Player getRoundWinner() {
		return null;
	}

	/**
	 * Shuffle and hand out cards.
	 */
	public void handOutCards() {
		// shuffle deck
		// ...
		
		// hand out cards
		int j = 0;
		for(Map.Entry<Integer, Player> entry : players.entrySet()) {
			Player p = entry.getValue();
			Card[] cards = Arrays.copyOfRange(deck, 4*j, 4*j+3);
			p.putCards(cards);
			//HandOutCardsMessage msg = new HandOutCardsMessage();
			//msg.cards = cards;
			//com.send(p, msg);
			j++;
		}
	}

	public void broadcastMessage(Message msg) throws IOException {
		for(Map.Entry<Integer, Player> entry : players.entrySet()) {
			com.send(entry.getValue(), msg);
		}
	}
}
