package bot;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.GameMode;
import shared.Trump;

public class IntelligenceMalicious extends BotIntelligence {

	@Override
	public Card getNextCard() {
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
	}

	@Override
	public Trump selectTrump(boolean canSwitch) {
		// as if I care, Muahahahaha - but OBENABE gives the most points
		return Trump.OBENABE;
	}

}
