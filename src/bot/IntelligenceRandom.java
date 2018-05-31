package bot;

import java.util.ArrayList;
import java.util.Random;

import shared.Card;
import shared.Trump;

/**
 * This class generates a Bot that plays according to the rules but will simply play a random card, and is therefor quite stupid.
 *
 */

public class IntelligenceRandom extends BotIntelligence {
	
		Random random = new Random();
		
		/**
		 * @return Randomly chosen Card from the allowed cards
		 */
		@Override
		public Card getNextCard() {
			Card card;
			ArrayList<Card> allowedCards = getAllowedCards();
			int pick = random.nextInt(allowedCards.size());
			card = allowedCards.get(pick);
			return card;
		}

		/**
		 * @param is Schieben allowed?
		 * @return Trump color resp. Schieben, obenabe oder undenuffe
		 */
		@Override
		public Trump selectTrump(boolean canSwitch) {
			Trump trump;
			if(canSwitch) { // SCHIEBEN if possible (Bot nix schuld ;-)
				trump = Trump.SCHIEBEN;
			} else {
				int pick = random.nextInt(Trump.values().length-1)+1; // SCHIEBEN must not be chosen
				trump = Trump.values()[pick];
			}
			return trump;
		}
		
}


