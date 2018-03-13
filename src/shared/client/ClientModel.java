package shared.client;

import shared.*;

public class ClientModel {
	//Fields
	private Team[] teams;
	private CardList hand;
	private CardList deck;
	private Trump trump;
	
	//Constructors
	
	//Methods
	
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
}
