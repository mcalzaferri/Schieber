package bot.test;

import bot.IntelligenceNormal;
import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.Trump;
import shared.Weis;
import shared.WeisType;

public class NextCardTest {

	public static void main(String[] args) {
		// Test for own winning cards
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
		
		// Test for partner winning cards
		int[] hand2 = {11,12,13,14,15,21,22,23,24};
		bot.setHand(hand2);
		bot.setTrump(Trump.ROSE);
		bot.setSelfID(0);
		bot.setPartnerID(2);
		Weis[] wiis = {new Weis(WeisType.VIERBLATT, new Card(CardColor.ROSE,CardValue.UNDER))};
		bot.showWeis(wiis, 2);
		bot.updateMaxCards();
		
		selection = bot.getNextCard();
		System.out.println(selection.getColor());
		System.out.println(selection.getValue());
		
		// Test for enemy winning hands
		IntelligenceNormal bot2 = new IntelligenceNormal();
		int[] hand3 = {11,12,13,14,15,21,22,23,24};
		bot2.setHand(hand3);
		bot2.setTrump(Trump.EICHEL);
		bot2.setSelfID(0);
		bot2.setPartnerID(2);
		Weis[] wiis2 = {new Weis(WeisType.VIERBLATT, new Card(CardColor.EICHEL,CardValue.UNDER))};
		bot2.showWeis(wiis2, 1);
		bot2.setDeck(new int[0]);
		bot2.updateMaxCards();


		selection = bot2.getNextCard();
		System.out.println(selection.getColor());
		System.out.println(selection.getValue());
		
		
		
	}

}
