package bot.test;

import bot.IntelligenceNormal;
import shared.CardColor;

/**
 * Test class used to test Trump making of a Bot - Manual Test!
 *
 */

public class TrumpTest {

	public static void main(String[] args) {

		IntelligenceNormal bot = new IntelligenceNormal();
		int[] hand = {29,21,22,23,24,25,26,27,28};
		bot.setHand(hand);
		
		System.out.println(bot.getSichereStichUndenufeObenabeBeginning(false));
		System.out.println(bot.getSichereStichBeginning(CardColor.ROSE));

	}

}
