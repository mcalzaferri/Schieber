package bot.test;

import java.util.ArrayList;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.IntelligenceRandom;
import shared.Trump;
import shared.Weis;

public class WeisTest {
	
	public static void main(String[] args) {
		
		// Test if stöck are correctly recognized
		BotIntelligence bot = new IntelligenceRandom();
		int[] hand = {11,12,13,14,15,16,17,18,19};
		bot.setHand(hand); // give all EICHEL
		bot.setTrump(Trump.UNEUFE);
		
		ArrayList<Weis> weise = bot.getWeise();
		System.out.println("Stöck:");
		for(Weis w: weise) {
			System.out.println(w.getType());
			System.out.println(w.getOriginCard().getValue());
		}
		System.out.println();
		
		// Test if 4 gliche are correctly recognized
		int[] hand2 = {11,21,31,41,15,25,35,45,46};
		bot.setHand(hand2);
		bot.setTrump(Trump.UNEUFE);
		
		weise = bot.getWeise();
		System.out.println("4 Gliche:");
		for(Weis w: weise) {
			System.out.println(w.getType());
			System.out.println(w.getOriginCard().getValue());
		}
		System.out.println();
		
		// Test if Folgen are correctly recognized
		int[] hand3 = {12,11,13,22,21,14,33,31,32};
		bot.setHand(hand3);
		bot.setTrump(Trump.UNEUFE);

		weise = bot.getWeise();
		System.out.println("Folgen:");
		for(Weis w: weise) {
			System.out.println(w.getType());
			System.out.println(w.getOriginCard().getColor());
			System.out.println(w.getOriginCard().getValue());
		}
		System.out.println();
	}

}
