package bot;

import java.util.Random;

import shared.Card;
import shared.CardList;
import shared.GameMode;
import shared.Player;
import shared.Trump;

public class IntelligenceRandom extends BotIntelligence {
	
		Random random = new Random();
		
		/**
		 * @return Randomly chosen Card from the hand
		 */
		public Card getNextCard() {
			Card card;
			int pick = random.nextInt(cardsInHand.size());
			card = cardsInHand.remove(pick);
			//TODO(Lukas): What happens if card is not accepted by server? When can the card be removed from the list?
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


