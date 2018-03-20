package server;

import java.util.Hashtable;

import shared.Card;
import shared.CardColor;
import shared.GameMode;
import shared.Player;

public class GameLogic {
	private GameMode mode;
	private CardColor trumpf;
	private Hashtable<Integer, Player> players;
	private Card[] deck;

	public GameLogic() {
		players = new Hashtable<>();
		deck = new Card[36];
		// fill deck with cards
		// shuffle deck
	}
	
	/**
	 * Starting new Game.
	 */
	public void startGame(Player player) {

	}
	
	public Hashtable<Integer, Player> getPlayers() {
		return players;
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
}
