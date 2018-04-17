package server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.GameMode;
import shared.Player;

/**
 * This class does all the game specific stuff like handling players, keeping
 * track of the cards that were played and calculating scores.
 */
public class GameLogic {
	private GameMode mode;
	private CardColor trumpf;
	/**
	 * Maps seatNr to Player object
	 */
	private ArrayList<Player> players;
	/**
	 * Seat nr of player that has to take action.
	 * 0 means it's noones move.
	 */
	private int currentSeatNr = 0;
	private Card[] deck;

	public GameLogic() {
		players = new ArrayList<>();
	}

	/**
	 * Create a new shuffled deck of cards.
	 */
	public void createDeck() {
		deck = new Card[36];
		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(CardColor.getById(i / 9 + 1),
			                   CardValue.getById(i % 9 + 1));
		}

		// Fisher–Yates shuffle
		Random random = new Random();
		for (int i = deck.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			Card temp = deck[index];
			deck[index] = deck[i];
			deck[i] = temp;
		}
	}

	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	/**
	 * Assign cards to player.
	 * @return array with nine cards
	 */
	public Card[] assignCardsToPlayer(Player p) {
		if (p.getSeatNr() == 0) {
			System.err.println("Tried to assign cards to player which is not at the table.");
			return null;
		}

		Card[] cards = Arrays.copyOfRange(deck, 9 * (p.getSeatNr() - 1),
		                                        9 * (p.getSeatNr() - 1) + 9);
		p.putCards(cards);
		return cards;
	}

	/**
	 * Add player to game.
	 * @param p player to add
	 */
	public void addPlayer(Player p) {
		players.add(p);
	}

	/**
	 * Remove player from game.
	 * @param p player to remove
	 */
	public void removePlayer(Player p) {
		players.remove(p.getSeatNr());
	}

	/**
	 * Adds a player to the table by assigning a seat to him.
	 * @param p player to add to the table
	 * @param prefearedSeatNr the players preferred seat
	 * @return true on success, false otherwise
	 */
	public boolean addPlayerToTable(Player p, int preferredSeatNr) {
		if(preferredSeatNr < 1 || preferredSeatNr > 4) {
			System.err.println("Failed to add player to table: invalid seatNr: "
					+ preferredSeatNr);
			return false;
		}

		for (Player player : players) {
			if (player.getSeatNr() == preferredSeatNr) {
				System.err.println("Failed to add player to table: seat occupied");
				return false;
			}
		}

		if(p.getSeatNr() > 0) {
			System.err.println(
					"Failed to add player to table: player already at table");
		}

		players.remove(p.getSeatNr());
		p.setSeatNr(preferredSeatNr);
		addPlayer(p);
		return true;
	}

	/**
	 * @return true if all players at the table are ready, false otherwise
	 */
	public boolean allPlayersReady() {
		for (Player p : players) {
			if (p.getSeatNr() > 0 && !p.isReady()) {
				return false;
			}
		}
		return true;
	}

	public Player getPlayer(InetSocketAddress addr) {
		for (Player p : players) {
			if(p.getSocketAddress().equals(addr)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Get player whose turn it is.
	 * @return player that has to take action
	 */
	public Player getNextPlayer() {
		currentSeatNr++;
		if(currentSeatNr == 5) {
			currentSeatNr = 1;
		}
		return players.get(++currentSeatNr);
	}

	/**
	 * Place a card.
	 * After the fourth card has been placed the round is over.
	 * @return false if move is invalid, true otherwise
	 */
	public boolean placeCard(Card c) {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public Player getRoundWinner() {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public int getPlayerCount() {
		return players.size();
	}
}
