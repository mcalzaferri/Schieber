package shared.client;

import shared.*;

public class ClientModel {
	//Fields
	private Team[] teams;
	private CardList hand;
	private CardList deck;
	private Trumpf trumpf;
	
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
	public Trumpf getTrumpf() {
		return trumpf;
	}
	public void setTrumpf(Trumpf trumpf) {
		this.trumpf = trumpf;
	}
}
