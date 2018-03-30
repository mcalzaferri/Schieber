package bot.test;

import bot.IntelligenceNormal;
import shared.Card;
import shared.Trump;

public class NextCardTest {

	public static void main(String[] args) {
		IntelligenceNormal bot = new IntelligenceNormal();
		int[] hand = {21,22,23,24,25,26,27,28,31};
		bot.setHand(hand);
		bot.setTrump(Trump.OBENABE);
		int[] deck = {29};
		bot.setDeck(deck);
		bot.updateMaxCards();
		
		bot.setDeck(new int[0]);
		
		Card selection = bot.getNextCard();
		System.out.println(selection.getColor());
		System.out.println(selection.getValue());
		
	}

}
