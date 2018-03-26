package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.GameMode;
import shared.Player;

public class GameLogic {
	private GameMode mode;
	private CardColor trumpf;
	private ArrayList<Player> players;
	private Player presentPlayer;
	private int playerPointer = 0;
	private Card[] deck;

	public GameLogic() {
		startGame();
	}

	/**
	 * Start a new Game. Creates a new list for players and a new shuffled deck
	 * of cards.
	 */
	public void startGame() {
		players = new ArrayList<>();

		deck = new Card[36];
		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(CardColor.getColorById(i / 9 + 1),
					CardValue.getValueById(i % 9));
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

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	/**
	 * Return cards that will be handed out.
	 *
	 * @return array with nine cards
	 */
	public Card[] getCardsForPlayer(Player p) {
		Card[] cards = Arrays.copyOfRange(deck, 9 * p.getId(),
				9 * p.getId() + 8);
		p.putCards(cards);
		return cards;
	}

	/**
	 * Add player to game.
	 *
	 * @param p player to be added
	 * @return false if table is full (4 players), true otherwise
	 */
	public boolean addPlayer(Player p) {
		if (players.size() == 4) {
			return false;
		}
		players.add(p);
		return true;
	}

	public void removePlayer(Player p){
		players.remove(p.getId());
	}

	/**
	 *
	 * @return Gets the player, who is on the turn.
	 */
	public Player getPresentPlayer(){
		if(playerPointer == players.size()){
			playerPointer = 0;
		}
		return getPlayers().get(playerPointer++);
	}

	/**
	 * Place a card. After the fourth card has been placed the round is over.
	 *
	 * @return false if move is invalid, true otherwise
	 */
	public boolean placeCard(Card c) {
		return false;
	}

	public Player getRoundWinner() {
		return null;
	}

	public int getPlayerCount() {
		return players.size();
	}
}
