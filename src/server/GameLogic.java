package server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.TeamEntity;
import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.Player;
import shared.Seat;
import shared.Trump;

/**
 * This class does all the game specific stuff like handling players, keeping
 * track of the cards that were played and calculating scores.
 *
 * Terms:
 * game  - multiple rounds (until target score is reached)
 * round - 9 runs (36 card played)
 * run   - one iteration (4 cards played)
 */
public class GameLogic {
	private final int team1Id = 1;
	private final int team2Id = 2;

	private final int playerIdStart = 1;
	/** used to generate new player IDs */
	private int playerId = playerIdStart;
	private Trump trump;
	private ArrayList<Player> players;
	/** seat of player whose turn it is */
	private Seat currentSeat;
	/** first card played in run */
	private Card firstCard;
	/** maps team via team ID to their score */
	private Map<Integer, Integer> scores;
	/** seat that started the round */
	private Seat roundStarter;
	/** counts placed cards */
	private int cardCounter;
	private int targetScore;
	private Map<Seat, Card> cardsOnTable;
	/** winner of last run */
	Seat lastWinner;

	public GameLogic() {
		players = new ArrayList<>();
		scores = new HashMap<>();
		cardsOnTable = new HashMap<>();
	}

	/**
	 * Initialize a new game
	 */
	public void init() {
		roundStarter = null;
		targetScore = 1000;
		scores.clear();
		initRound();
		initRun();
	}

	/**
	 * Initialize a new round
	 */
	public void initRound() {
		cardCounter = 0;
		trump = null;
		lastWinner = null;

		if (roundStarter == null) {
			roundStarter = Seat.SEAT1;
		} else {
			roundStarter = getNextSeat(roundStarter);
		}
		currentSeat = roundStarter;

		assignCardsToPlayers();
		initRun();
	}

	/**
	 * Initialize a new run
	 */
	private void initRun() {
		cardsOnTable.clear();
		if (lastWinner != null) {
			currentSeat = lastWinner;
		}
		firstCard = null;
	}

	/**
	 * @param trump trump to set
	 */
	public void setTrump(Trump trump){
		this.trump = trump;
	}

	/**
	 * @return read only list of all players
	 */
	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	/**
	 * Create shuffled deck of cards and assign it to the players.
	 * @param player player to assign cards to
	 * @return array with nine cards
	 */
	private void assignCardsToPlayers() {
		Card[] deck = new Card[36];

		// fill deck with cards
		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(CardColor.getById(i / 9 + 1),
			                   CardValue.getById(i % 9 + 1));
		}

		// shuffle deck (Fisher–Yates shuffle)
		Random random = new Random();
		for (int i = deck.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			Card temp = deck[index];
			deck[index] = deck[i];
			deck[i] = temp;
		}

		// assign cards
		for (Player player : getPlayersAtTable()) {
			player.putCards(Arrays.copyOfRange(deck, 9 * (player.getSeatNr() - 1),
	                9 * (player.getSeatNr() - 1) + 9));
			}
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
	 * @param preferredSeat the players preferred seat
	 */
	public void addPlayerToTable(Player player, Seat preferredSeat) {
		if (preferredSeat == null) {
			// player did not specify a seat: assign a free one
			player.setSeat(getFreeSeat());
			return;
		}

		if (player.getSeat().equals(preferredSeat)) {
			// Player is already sitting at this seat.
			return;
		}

		for (Player p : players) {
			if (p.getSeat().equals(preferredSeat)) {
				// seat is occupied: assign a free one
				player.setSeat(getFreeSeat());
				return;
			}
		}

		player.setSeat(preferredSeat);
	}

	/**
	 * @return first free seat
	 */
	private Seat getFreeSeat() {
		for (Seat seat : Seat.values()) {
			if (seat == Seat.NOTATTABLE) {
				continue;
			}

			boolean free = true;
			for (Player p : players) {
				if(p.getSeat() == seat) {
					free = false;
				}
			}

			if (free) {
				return seat;
			}
		}
		return null;
	}

	/**
	 * @return true if all four players at the table are ready, false otherwise
	 */
	public boolean areAllPlayersReady() {
		int i = 0;
		for (Player p : players) {
			if (p.isAtTable() && p.isReady()) {
				i++;
			}
		}
		return i == 4;
	}

	/**
	 * Get player via his socket address
	 * @param addr socket address
	 * @return player object
	 */
	public Player getPlayer(InetSocketAddress addr) {
		for (Player p : players) {
			if(p.getSocketAddress().equals(addr)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Get player via his seat
	 * @param seat the seat the player is sitting at
	 * @return player object if the player is sitting at the table, null
	 *         otherwise
	 */
	public Player getPlayer(Seat seat) {
		for (Player p : players) {
			if(p.getSeat().equals(seat)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Get partner of player
	 * @param player player to retrieve partner of
	 * @return partner
	 */
	public Player getPartner(Player player){
		switch (player.getSeat()) {
		case SEAT1 :
			return getPlayer(Seat.SEAT3);
		case SEAT2 :
			return getPlayer(Seat.SEAT4);
		case SEAT3 :
			return getPlayer(Seat.SEAT1);
		case SEAT4 :
			return getPlayer(Seat.SEAT2);
		default:
			break;
		}
		System.err.println("Unimplemented Seat");
		return null;
	}

	/**
	 * Get teams as player entity array
	 * @return team entity
	 * @{
	 */
	public TeamEntity getTeam1() {
		TeamEntity team = new TeamEntity();
		team.teamId = team1Id;
		team.players = new PlayerEntity[] {getPlayer(Seat.SEAT1).getEntity(),
				getPlayer(Seat.SEAT3).getEntity()};
		return team;
	}
	public TeamEntity getTeam2() {
		TeamEntity team = new TeamEntity();
		team.teamId = team2Id;
		team.players = new PlayerEntity[] {getPlayer(Seat.SEAT2).getEntity(),
				getPlayer(Seat.SEAT4).getEntity()};
		return team;
	}
	/** @} */

	private int getTeamId(Seat seat) {
		switch(seat) {
		case SEAT1:
		case SEAT3:
			return team1Id;
		case SEAT2:
		case SEAT4:
			return team2Id;
		case NOTATTABLE:
			return -1;
		}
		System.err.println("unhandled team");
		return -1;
	}

	/**
	 * @return seat next to the specified seat
	 */
	private Seat getNextSeat(Seat seat) {
		switch(seat) {
		case NOTATTABLE:
			return Seat.SEAT1;
		case SEAT1:
			return Seat.SEAT2;
		case SEAT2:
			return Seat.SEAT3;
		case SEAT3:
			return Seat.SEAT4;
		case SEAT4:
			return Seat.SEAT1;
		default:
			return null;
		}
	}

	/**
	 * Get player whose turn it is
	 * @return player that has to take action
	 */
	public Player getCurrentPlayer() {
		return getPlayer(currentSeat);
	}

	/**
	 * Place a card
	 * A run starts every four cards.
	 * A round starts evers 36 cards.
	 * If the score limit has been reached the game is over.
	 * @return move status enum
	 */
	public MoveStatus placeCard(Card card) {
		if (cardCounter == 36) {
			return MoveStatus.INVALID;
		}

		Player p = getCurrentPlayer();

		if (!card.isAllowed(p.getCards().toArray(), firstCard, trump)) {
			return MoveStatus.NOTALLOWED;
		}

		if (!p.removeCard(card)) {
			System.err.println("player tried to play a card that he does not have");
			return MoveStatus.INVALID;
		}

		cardsOnTable.put(currentSeat, card);

		currentSeat = getNextSeat(currentSeat);

		cardCounter++;

		if (cardCounter % 4 == 0) {
			calcRunWinner();
			scores.put(getTeamId(lastWinner), calcTableScore());
			if (cardCounter == 36) {
				currentSeat = null;
				for (Map.Entry<Integer, Integer> entry : scores.entrySet()) {
					if (entry.getValue() >= targetScore) {
						return MoveStatus.GAMEOVER;
					}
				}
				return MoveStatus.ROUNDOVER;
			}
			initRun();
			return MoveStatus.RUNOVER;
		}
		return MoveStatus.OK;
	}

	public enum MoveStatus {
		NOTALLOWED, INVALID, OK, RUNOVER, ROUNDOVER, GAMEOVER;
	}

	private void calcRunWinner() {
		Seat seat = roundStarter;
		Seat winner = seat;
		Card highCard = cardsOnTable.get(seat);
		for (int i = 0; i < 3; i++) {
			Card card = cardsOnTable.get(seat);
			//TODO: angeben handled the right way?
			if (card.compareTo(highCard, trump/*, firstCard*/) > 0) {
				highCard = card;
				winner = seat;
			}
			seat = getNextSeat(seat);
		}
		lastWinner = winner;
	}

	//TODO: calc score
	private int calcTableScore() {
		int score = 0;
//		for (Map.Entry<Seat, Card> entry : cardsOnTable.entrySet()) {
//			score += entry.getValue().getValue().
//		}
		return score;
	}

	public Player getRunWinner() {
		return getPlayer(lastWinner);
	}

	public boolean inFirstRun() {
		return cardCounter < 4;
	}

	public boolean wiis(Player p) {
//		player.getCards().getPossibleWiis()
		throw new UnsupportedOperationException("Function not implemented.");
	}

	public Map<Integer, Integer> getScores() {
		return scores;
	}

	public int getPlayerCount() {
		return players.size();
	}

	private List<Player> getPlayersAtTable() {
		ArrayList<Player> tablePlayers = new ArrayList<>();
		for (Player p : players) {
			if (p.isAtTable()) {
				tablePlayers.add(p);
			}
		}
		return tablePlayers;
	}

	/**
	 * @return number of players that sit at the table
	 */
	public int getTablePlayerCount () {
		return getPlayersAtTable().size();
	}

	public int generatePlayerId() {
		return playerId++;
	}
}
