package shared.client;

import shared.*;

public class ClientModel {
	//Fields
	private Player[] players;
	private CardList hand;
	private CardList deck;
	private Trump trump;
	private Score score;
	private Integer seatId;
	private Integer activeSeatId;
	private Boolean canSwitch;
	
	//Constructors
	
	//Methods
	public void updateScore(Score score) {
		//TODO
	}
	
	public void updateHand(int[] handCardIds) {
		hand.updateData(handCardIds);
	}
	
	public void updateDeck(int[] deckCardIds) {
		deck.updateData(deckCardIds);
	}
	
	//Getters and Setters
	public Player[] getPlayers() {
		return players;
	}
	public void setTeams(Player[] players) {
		this.players = players;
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
		return seatId;
	}
	public void setSeatId(int seatId) {
		this.seatId = seatId;
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
}
