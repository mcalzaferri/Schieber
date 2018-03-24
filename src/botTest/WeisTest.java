package botTest;

import java.util.ArrayList;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.IntelligenceRandom;
import shared.Trump;
import shared.Weis;

public class WeisTest {
	
	public static void main(String[] args) {
		
		BotIntelligence bot = new IntelligenceRandom();
		int[] hand = {10,11,12,13,14,15,16,17,18};
		bot.setHand(hand); // give all EICHEL
		bot.setTrump(Trump.EICHEL);
		
		ArrayList<Weis> weise = bot.getWeise();
		for(Weis w: weise) {
			System.out.println(w.getType());
		}
		
		
	}

}
