package bot;

import java.util.ArrayList;

import shared.Card;
import shared.CardList;
import shared.Player;
import shared.proto.Message;
import shared.proto.ToPlayerMessage;

public class BotIntelligence {
	
	private CardList cardsInHand;
	private CardList cardsPlayed;								
	private CardList deck;										//fkaiser: added, but cardsPlayed can be used							
	private Player partner;
	private int turn;
	
	public void setHand(CardList currHand)
	{
		cardsInHand = currHand;
	}
	
	public void setDeck(CardList currDeck)
	{
		deck = currDeck;
		
		//TODO: (fkaiser) add last Card of currDeck to cardsPlayed so no setcardPlayed-function is needed
		//example:
		//cardsPlayed.add(currDeck(IndexOfLastCard))
		
	}

	public Card getNextCard() {
		
		//TODO: fkaiser getNextCard - for now randomly
		return null;
	}
	
}
