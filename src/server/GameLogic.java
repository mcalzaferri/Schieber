package server;

import java.net.InetSocketAddress;
import java.util.*;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.TeamEntity;
import ch.ntb.jass.common.entities.WeisEntity;
import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.Player;
import shared.Seat;
import shared.Trump;
import shared.Weis;

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
	/** connects the player with his Weises */
	private Map<Player, WeisEntity[]> declaredWeise;
	/** seat of player whose turn it is */
	private Seat currentSeat;
	/** first card played in run */
	private Card firstCard;
	/** maps team via team ID to their score */
	private Map<Integer, Integer> scores;
	/** seat that starts the round */
	private Seat roundStarter;
	/** counts placed cards */
	private int cardCounter;
	private int targetScore;
	private Map<Card, Seat> cardsOnTable;
	/** winner of last run */
	private Seat lastWinner;

	public GameLogic() {
		players = new ArrayList<>();
		scores = new HashMap<>();
		cardsOnTable = new HashMap<>();
		declaredWeise = new HashMap<>();
		cardCounter = -1;
		trump = null;
		lastWinner = null;
		currentSeat = null;
		firstCard = null;
		roundStarter = null;
		targetScore = -1;
	}

	/**
	 * Initialize a new game
	 */
	public void init() {
		roundStarter = null;
		targetScore = 1000;
		scores.clear();
		scores.put(team1Id, 0);
		scores.put(team2Id, 0);
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
		} else {
			currentSeat = roundStarter;
		}
		firstCard = null;
	}

	/**
	 * @param trump trump to set
	 */
	public void setTrump(Trump trump){
		this.trump = trump;
	}

	public Trump getTrump() {
		return trump;
	}

	/**
	 * @return read only list of all players
	 */
	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	/**
	 * Create shuffled deck of cards and assign it to the players.
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
			player.putCards(Arrays.copyOfRange(deck,
							9 * (player.getSeatNr() - 1),
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
			// player is already sitting at this seat
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
	 * @return first free seat if one is available, NOTATTABLE otherwise
	 */
	private Seat getFreeSeat() {
		for (Seat seat : Seat.values()) {
			if (seat == Seat.NOTATTABLE) {
				continue;
			}

			boolean free = true;
			for (Player p : players) {
				if (p.getSeat() == seat) {
					free = false;
				}
			}

			if (free) {
				return seat;
			}
		}
		return Seat.NOTATTABLE;
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
			if (p.getSocketAddress().equals(addr)) {
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
			if (p.getSeat().equals(seat)) {
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
	private TeamEntity getTeam1() {
		TeamEntity team = new TeamEntity();
		team.teamId = team1Id;
		team.players = new PlayerEntity[] {getPlayer(Seat.SEAT1).getEntity(),
				getPlayer(Seat.SEAT3).getEntity()};
		return team;
	}
	private TeamEntity getTeam2() {
		TeamEntity team = new TeamEntity();
		team.teamId = team2Id;
		team.players = new PlayerEntity[] {getPlayer(Seat.SEAT2).getEntity(),
				getPlayer(Seat.SEAT4).getEntity()};
		return team;
	}
	public TeamEntity[] getTeams() {
		return new TeamEntity[] { getTeam1(), getTeam2() };
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
	 * a new run starts every four cards.
	 * a new round starts every 36 cards.
	 * If the score target has been reached the game is over.
	 * @return move status
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

		if (cardsOnTable.isEmpty()) {
			firstCard = card;
		}

		cardsOnTable.put(card, currentSeat);

		currentSeat = getNextSeat(currentSeat);

		cardCounter++;

		if (cardCounter % 4 == 0) {
			// get run winner
			Card highCard = Card.highest(cardsOnTable.keySet(), firstCard, trump);
			lastWinner = cardsOnTable.get(highCard);

			// add score to team
			int teamId = getTeamId(lastWinner);
			addScore(teamId, calcTableScore());

			if (cardCounter == 36) {
				addScore(teamId, 5 * trump.getScoreMultiplicator());
			}

			for (int score : scores.values()) {
				if (score >= targetScore) {
					return MoveStatus.GAMEOVER;
				}
			}

			if (cardCounter == 36) {
				currentSeat = null;
				return MoveStatus.ROUNDOVER;
			}
			initRun();
			return MoveStatus.RUNOVER;
		}
		return MoveStatus.OK;
	}

	private void addScore(int teamId, int value) {
		scores.put(teamId, scores.get(teamId) + value);
	}

	private int calcTableScore() {
		int score = 0;
		for (Card c : cardsOnTable.keySet()) {
			score += c.getScore(trump);
		}
		return score;
	}

	public Player getRunWinner() {
		return getPlayer(lastWinner);
	}

	/**
	 * @return true if it's the first run of the round, false otherwise
	 */
	public boolean inFirstRun() {
		return cardCounter < 4;
	}
	
	public boolean weiseAreValid(Player p, WeisEntity[] claimedWeisEntity){
		int truthCounter = 0;
		int provisionalWeis = 0;
		Weis[] verifyWeisArray = p.getCards().getPossibleWiis(trump);
		Weis[] claimedWeisArray = new Weis[claimedWeisEntity.length];
		
		for(int i = 0; i < claimedWeisEntity.length; i++){
			Weis claimedWeis = new Weis(claimedWeisEntity[i]);			
			claimedWeisArray[i] = claimedWeis;						
			for(int j = 0; j < claimedWeisEntity.length; j++) {
								
				if(claimedWeisArray[i].compareTo(verifyWeisArray[j], trump) == 0) {
					truthCounter++;					
				}				
			}		
		}
		if(truthCounter == claimedWeisEntity.length){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void addWeisToScoreBoard(){
		//TODO: Implement Scoreboardbullshittery
	}

	public boolean wiis(Player p) {
//		player.getCards().getPossibleWiis()
		throw new UnsupportedOperationException("Function not implemented.");
	}
	
	public Map<Player, WeisEntity[]> getDeclaredWeise(){
		return declaredWeise;
	}
	public void setDeclaredWeise(Player player, WeisEntity[] weis){
		declaredWeise.put(player, weis);
	}
	
	public int getCardCounter(){
		return cardCounter;
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
	public int getTablePlayerCount() {
		return getPlayersAtTable().size();
	}

	public int generatePlayerId() {
		return playerId++;
	}

	public Player getRoundStarter() {
		return getPlayer(roundStarter);
	}

	public Collection<Card> getCardsOnTable() {
		return cardsOnTable.keySet();
	}
}
