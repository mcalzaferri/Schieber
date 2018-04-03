package shared.client;

import java.util.Dictionary;

import ch.ntb.jass.common.entities.PlayerEntity;
import shared.*;

public class ClientModel {
	//Fields
	private Player thisPlayer;
	private Dictionary<Integer,Player> players;
	private Team[] teams;
	private CardList hand;
	private CardList deck;
	private Trump trump;
	private Boolean weis;
	private Score score;
	private Integer activeSeatId;
	private Boolean canSwitch;
	private GameState gameState;
	
	//Constructors
	
	//Methods
	public void updateScore(Score score) {
		//TODO
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
	public void setWeis(Weis weis){
		this.weis = weis;
	}
	public void getWeis(){
		return weis;
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

	public Dictionary<Integer,Player> getPlayers() {
		return players;
	}

	public void setPlayers(Dictionary<Integer,Player> players) {
		this.players = players;
	}

	public Player getThisPlayer() {
		return thisPlayer;
	}

	public void setThisPlayer(Player thisPlayer) {
		this.thisPlayer = thisPlayer;
	}
}
