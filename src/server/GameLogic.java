package server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.entities.TeamEntity;
import ch.ntb.jass.common.entities.TrumpEntity;
import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.GameMode;
import shared.Player;
import shared.Score;

/**
 * This class does all the game specific stuff like handling players, keeping
 * track of the cards that were played and calculating scores.
 *
 * Terms:
 * game  - multiple rounds (until score limit is reached)
 * round - 9 runs (36 card played)
 * run   - one iteration (4 cards played)
 */
public class GameLogic {
	private GameMode mode;
	private TrumpEntity trump;
//	private CardColor trumpf;
	/**
	 * Maps seatNr to Player object
	 */
	private ArrayList<Player> players;
	private SeatEntity currentSeat;
	private Card[] deck;

	public GameLogic() {
		players = new ArrayList<>();
		init();
	}

	/**
	 * Initialize new game.
	 */
	public void init() {
		currentSeat = SeatEntity.NOTATTABLE;
		for (Player p : players) {
			p.putCards(null);
			p.setSeatNr(SeatEntity.NOTATTABLE.getSeatNr());
			p.setReady(false);
		}
		// TODO: possibly more stuff has to be done here
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
	
	/**
	 * Set trump
	 */
	
	public void setTrump(TrumpEntity trump){
		this.trump = trump;
	}
	

	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	/**
	 * Assign cards to player.
	 * @param player player to assign cards to
	 * @return array with nine cards
	 */
	public Card[] assignCardsToPlayer(Player player) {
		if (player.getSeatNr() == 0) {
			System.err.println("Tried to assign cards to player which is not at the table.");
			return null;
		}

		Card[] cards = Arrays.copyOfRange(deck, 9 * (player.getSeatNr() - 1),
		                                        9 * (player.getSeatNr() - 1) + 9);
		player.putCards(cards);
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
		players.remove(p);
	}

	/**
	 * Add player to the table by assigning a seat to him
	 * @param player player to add to the table
	 * @param prefearedSeatNr the players preferred seat
	 * @return true on success, false otherwise
	 */
	public boolean addPlayerToTable(Player player, SeatEntity seat) {
		if (player.getSeatNr() == seat.getSeatNr()) {
			// Player is already sitting at this seat.
			return true;
		}

		for (Player p : players) {
			if (p.getSeatNr() == seat.getSeatNr()) {
				System.err.println("Failed to add player to table: seat occupied");
				return false;
			}
		}

		player.setSeatNr(seat.getSeatNr());
		return true;
	}

	/**
	 * @return true if all players at the table are ready, false otherwise
	 */
	public boolean areAllPlayersReady() {
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

	public Player getPlayer(SeatEntity seat) {
		for (Player p : players) {
			if(p.getSeatNr() == seat.getSeatNr()) {
				return p;
			}
		}
		return null;
	}

	public TeamEntity getTeam1() {
		TeamEntity team = new TeamEntity();
		team.players = new Player[] {getPlayer(SeatEntity.SEAT1), getPlayer(SeatEntity.SEAT3)};
		return team;
	}
	public TeamEntity getTeam2() {
		TeamEntity team = new TeamEntity();
		team.players = new Player[] {getPlayer(SeatEntity.SEAT2), getPlayer(SeatEntity.SEAT4)};
		return team;
	}

	/**
	 * Get player whose next
	 * @return next player that has to take action
	 * @throws Exception
	 */
	public Player nextPlayer() {
		switch(currentSeat) {
		case NOTATTABLE:
			currentSeat = SeatEntity.SEAT1;
			break;
		case SEAT1:
			currentSeat = SeatEntity.SEAT2;
			break;
		case SEAT2:
			currentSeat = SeatEntity.SEAT3;
			break;
		case SEAT3:
			currentSeat = SeatEntity.SEAT4;
			break;
		case SEAT4:
			currentSeat = SeatEntity.SEAT1;
			break;
		default:
			System.err.println("Unimplemented Seat");
		}
		return getCurrentPlayer();
	}

	/**
	 * Get player whose turn it is.
	 * @return player that has to take action
	 */
	public Player getCurrentPlayer() {
		return getPlayer(currentSeat);
	}

	/**
	 * Place a card
	 * After the fourth card has been placed the run is over.
	 * @return false if move is invalid, true otherwise
	 */
	public boolean placeCard(Card c) {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public Player getRunWinner() {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public boolean inFirstRun() {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public boolean isRunOver() {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public boolean isRoundOver() {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public boolean isGameOver() {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public Score getScore() {
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public int getPlayerCount() {
		return players.size();
	}
}
