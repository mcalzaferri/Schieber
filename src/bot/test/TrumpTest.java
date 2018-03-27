package bot.test;

import bot.IntelligenceNormal;
import shared.CardColor;

public class TrumpTest {

	public static void main(String[] args) {

		IntelligenceNormal bot = new IntelligenceNormal();
		int[] hand = {10,11,18,13,14,15,16,17,20};
		bot.setHand(hand);
		
		System.out.println(bot.getSichereStichUndenufeObenabe(false));
		System.out.println(bot.getSichereStich(CardColor.SCHELLE));

	}

}
