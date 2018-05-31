package bot;

import java.util.ArrayList;
import java.util.Random;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.GameMode;
import shared.Trump;

/**
 * This class implements a cheating Bot to test a server. It will always try to play a winning card, regardless if it owns this card.
 * If cheating is not accepted, it will play a random allowed card and try to cheat again for the next card.
 *
 */

public class IntelligenceMalicious extends BotIntelligence {
	
	Random r = new Random();
	int cheatTurn = 9;

	@Override
	public Card getNextCard() {
		if(cardsInHand.size() == cheatTurn) {
			cheatTurn--;
			if(cheatTurn == 0) {
				cheatTurn = 9;
			}
			if(trump.getGameMode() == GameMode.UNEUFE) {
				if(deck.size() > 0) {
					return new Card(deck.get(0).getColor(), CardValue.SECHS);
				} else {
					return new Card(CardColor.EICHEL, CardValue.SECHS);
				}
			} else if(trump.getGameMode() == GameMode.OBENABE) {
				if(deck.size() > 0) {
					return new Card(deck.get(0).getColor(), CardValue.ASS);
				} else {
					return new Card(CardColor.EICHEL, CardValue.ASS);
				}
			} else {
				return new Card(trump.getTrumpfColor(),CardValue.UNDER);
			}
		} else {
			// play random card if cheating is not possible
			Card card;
			ArrayList<Card> allowedCards = getAllowedCards();
			int pick = r.nextInt(allowedCards.size());
			card = allowedCards.get(pick);
			return card;
		}
	}

	@Override
	public Trump selectTrump(boolean canSwitch) {
		// as if I care, Muahahahaha - but OBENABE gives the most points
		return Trump.OBENABE;
	}

}
