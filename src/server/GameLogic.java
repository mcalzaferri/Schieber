package server;

import java.lang.reflect.Array;
import java.util.Hashtable;
import java.util.Map;
import shared.*;

public class GameLogic {
	private Communication com;
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
		for(Map.Entry<Integer, Player> entry : players.entrySet()) {
			Player p = entry.getValue();
		}
	}
}
