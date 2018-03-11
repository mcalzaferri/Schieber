package bot;

import java.util.ArrayList;

import shared.Card;
import shared.Player;
import shared.proto.Message;
import shared.proto.ToPlayerMessage;

public class BotIntelligence {
	
	ArrayList<Card> cardsInHand = new ArrayList<>(), cardsPlayed = new ArrayList<>();
	Player partner;
	int turn;
	
	public ToPlayerMessage receive(Message message) {

		return null;
		
	}
}
