package shared.client;

import java.util.Map;

import ch.ntb.jass.common.entities.PlayerEntity;
import shared.*;

public class ClientModel {
	//Fields
	private Player thisPlayer;
	private Map<Integer,Player> players;
	private Team[] teams;
	private CardList hand;
	private CardList deck;
	private Trump trump;
	private Weis[] possibleWiis;
	private Score score;
	private Integer activeSeatId;
	private Boolean canSwitch;
	private GameState gameState;
	
	//Constructors
	
	//Methods
	public void updateScore(Score score) {
		this.score.updateScore(score);
	}
	
	public void updateHand(Card[] handCards) {
		hand.updateData(handCards);
	}
	
	public void updateDeck(Card[] deckCards) {
		deck.updateData(deckCards);
	}
	
	public Player getPlayerById(Integer id) {
		return players.get(id);
	}
	
	public void updatePlayer(PlayerEntity entity) {
		getPlayerById(entity.id).update(entity);
	}
	
	//Getters and Setters
	public Team[] getTeams() {
		return teams;
	}
	public void setTeams(Team[] teams) {
		
		this.teams = teams;
	}
	public CardList getHand() {
		return hand;
	}
	public void setHand(CardList hand) {
		this.hand = hand;
	}
	public CardList getDeck() {
		return deck;
	}
	public void setDeck(CardList deck) {
		this.deck = deck;
	}
	public Trump getTrump() {
		return trump;
	}
	public void setTrump(Trump trump) {
		this.trump = trump;
	}
	public int getSeatId() {
		return getThisPlayer().getSeatNr();
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public int getActiveSeatId() {
		return activeSeatId;
	}

	public void setActiveSeatId(int activeSeatId) {
		this.activeSeatId = activeSeatId;
	}

	public Boolean getCanSwitch() {
		return canSwitch;
	}

	public void setCanSwitch(Boolean canSwitch) {
		this.canSwitch = canSwitch;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Map<Integer,Player> getPlayers() {
		return players;
	}

	public void setPlayers(Map<Integer,Player> players) {
		this.players = players;
	}

	public Player getThisPlayer() {
		return thisPlayer;
	}

	public void setThisPlayer(Player thisPlayer) {
		this.thisPlayer = thisPlayer;
	}

	public Weis[] getPossibleWiis() {
		return possibleWiis;
	}

	public void setPossibleWiis(Weis[] possibleWiis) {
		this.possibleWiis = possibleWiis;
	}
}
