package bot.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.IntelligenceRandom;
import shared.Card;
import shared.Trump;
import shared.Weis;

public class BotTest {
	
	static ArrayList<Integer> allCards;
	static Random r;
	static IntelligenceNormal bot1;
	static IntelligenceNormal bot2;
	static IntelligenceNormal bot3;
	static IntelligenceNormal bot4;

	public static void main(String[] args) {
		
		bot1 = new IntelligenceNormal();
		bot2 = new IntelligenceNormal();
		bot3 = new IntelligenceNormal();
		bot4 = new IntelligenceNormal();
		
		long seed = 0;
		
		r = new Random(seed);
		allCards = new ArrayList<>();
		allCards = generateAllCards();
		
		// Generate Random Hand and give to bots
		ArrayList<Integer> bot1Hand = generateRandomHand();
		bot1.setHand(al2A(bot1Hand));
		ArrayList<Integer> bot2Hand = generateRandomHand();
		bot2.setHand(al2A(bot2Hand));
		ArrayList<Integer> bot3Hand = generateRandomHand();
		bot3.setHand(al2A(bot3Hand));
		ArrayList<Integer> bot4Hand = generateRandomHand();
		bot4.setHand(al2A(bot4Hand));
		
		// Generate fixed hand
		// int[] testHand = generateFixedHand();
		
		// print Hands
		print("Hand Bot1", al2A(bot1Hand));
		print("Hand Bot2", al2A(bot2Hand));
		print("Hand Bot3", al2A(bot3Hand));
		print("Hand Bot4", al2A(bot4Hand));
		
		// set IDs
		bot1.setSelfID(0);
		bot1.setEnemyLeftID(1);
		bot1.setPartnerID(2);
		bot1.setEnemyRightID(3);
		
		bot2.setSelfID(1);
		bot2.setEnemyLeftID(2);
		bot2.setPartnerID(3);
		bot2.setEnemyRightID(0);
		
		bot3.setSelfID(2);
		bot3.setEnemyLeftID(3);
		bot3.setPartnerID(0);
		bot3.setEnemyRightID(1);
		
		bot4.setSelfID(3);
		bot4.setEnemyLeftID(0);
		bot4.setPartnerID(1);
		bot4.setEnemyRightID(2);
		
		
		// select Trump
		Trump trump = bot1.selectTrump(true);
		System.out.println("Trump (Bot1)");
		System.out.println("---------------");
		System.out.println(trump);
		System.out.println("----------------");
		System.out.println();
		
		if(trump == Trump.SCHIEBEN) {
			trump = bot3.selectTrump(false);
			System.out.println("Trump (Bot3)");
			System.out.println("---------------");
			System.out.println(trump);
			System.out.println("----------------");
			System.out.println();
			
		}
		
		//tell Trump to all Bots
		bot1.setTrump(trump);
		bot2.setTrump(trump);
		bot3.setTrump(trump);
		bot4.setTrump(trump);
		
		// playing round 1 (with Wiis)
		int[] deck1 = new int[0];
		updateBots(deck1, 0);
		Card card1 = bot1.getNextCard();
		ArrayList<Weis> weise = bot1.getWeise();
		print("Bot1 plays",card1);
		if(!weise.isEmpty()) {
			print("Bot1 weises", weise);
			Weis[] weisA = new Weis[weise.size()];
			weise.toArray(weisA);
			bot2.showWeis(weisA, 0);
			bot3.showWeis(weisA, 0);
			bot4.showWeis(weisA, 0);
		}
		
		bot2.setActivePlayerID(1);
		int[] deck2 = new int[1];
		deck2[0] = card1.getId();
		updateBots(deck2, 1);
		Card card2 = bot2.getNextCard();
		weise = bot2.getWeise();
		print("Bot2 plays",card2);
		if(!weise.isEmpty()) {
			print("Bot2 weises", weise);
			Weis[] weisA = new Weis[weise.size()];
			weise.toArray(weisA);
			bot1.showWeis(weisA, 1);
			bot3.showWeis(weisA, 1);
			bot4.showWeis(weisA, 1);
		}
		
		bot3.setActivePlayerID(2);
		int[] deck3 = new int[2];
		deck3[0] = card1.getId();
		deck3[1] = card2.getId();
		updateBots(deck3,2);
		Card card3 = bot3.getNextCard();
		weise = bot3.getWeise();
		print("Bot3 plays",card3);
		if(!weise.isEmpty()) {
			print("Bot3 weises", weise);
			Weis[] weisA = new Weis[weise.size()];
			weise.toArray(weisA);
			bot1.showWeis(weisA, 2);
			bot2.showWeis(weisA, 2);
			bot4.showWeis(weisA, 2);
		}
		
		bot4.setActivePlayerID(3);
		int[] deck4 = new int[3];
		deck4[0] = card1.getId();
		deck4[1] = card2.getId();
		deck4[2] = card3.getId();
		updateBots(deck4,3);
		Card card4 = bot4.getNextCard();
		weise = bot4.getWeise();
		print("Bot4 plays",card4);
		if(!weise.isEmpty()) {
			print("Bot4 weises", weise);
			Weis[] weisA = new Weis[weise.size()];
			weise.toArray(weisA);
			bot1.showWeis(weisA, 3);
			bot2.showWeis(weisA, 3);
			bot3.showWeis(weisA, 3);
		}
		
		// end round - update deck info
		int[] fullDeck = new int[4];
		fullDeck[0] = card1.getId();
		fullDeck[1] = card2.getId();
		fullDeck[2] = card3.getId();
		fullDeck[3] = card4.getId();
		updateBots(fullDeck,0);
		bot1.updateMaxCards();
		bot2.updateMaxCards();
		bot3.updateMaxCards();
		bot4.updateMaxCards();
		
		// update Hands
		bot1Hand.remove((Integer) card1.getId());
		bot1.setHand(al2A(bot1Hand));
		bot2Hand.remove((Integer) card2.getId());
		bot2.setHand(al2A(bot2Hand));
		bot3Hand.remove((Integer) card3.getId());
		bot3.setHand(al2A(bot3Hand));
		bot4Hand.remove((Integer) card4.getId());
		bot4.setHand(al2A(bot4Hand));
	
		// further rounds
		for(int i = 2; i < 10; i++) {
			System.out.println("Round " + i);
			System.out.println("----------------------");
			
			updateBots(deck1,0);
			card1 = bot1.getNextCard();
			System.out.println("Bot1 plays: " + card1.getColor() + " " + card1.getValue());
			
			deck2[0] = card1.getId();
			updateBots(deck2,1);
			card2 = bot2.getNextCard();
			System.out.println("Bot2 plays: " + card2.getColor() + " " + card2.getValue());
			
			deck3[0] = card1.getId();
			deck3[1] = card2.getId();
			updateBots(deck3,2);
			card3 = bot3.getNextCard();
			System.out.println("Bot3 plays: " + card3.getColor() + " " + card3.getValue());
			
			deck4[0] = card1.getId();
			deck4[1] = card2.getId();
			deck4[2] = card3.getId();
			updateBots(deck4,3);
			card4 = bot4.getNextCard();
			System.out.println("Bot4 plays: " + card4.getColor() + " " + card4.getValue());
			
			fullDeck[0] = card1.getId();
			fullDeck[1] = card2.getId();
			fullDeck[2] = card3.getId();
			fullDeck[3] = card4.getId();
			updateBots(fullDeck,0);
			bot1.updateMaxCards();
			bot2.updateMaxCards();
			bot3.updateMaxCards();
			bot4.updateMaxCards();
			
			bot1Hand.remove((Integer) card1.getId());
			bot1.setHand(al2A(bot1Hand));
			bot2Hand.remove((Integer) card2.getId());
			bot2.setHand(al2A(bot2Hand));
			bot3Hand.remove((Integer) card3.getId());
			bot3.setHand(al2A(bot3Hand));
			bot4Hand.remove((Integer) card4.getId());
			bot4.setHand(al2A(bot4Hand));
			
			System.out.println();
			
		}
		
		

	}
	
	private static int[] generateFixedHand() {
		int[] testHand = new int[9];
		for(int i=0;i<9;i++) {
			testHand[i] = allCards.remove(0);
		}
		Arrays.sort(testHand);
		return testHand;
	}

	private static ArrayList<Integer> generateRandomHand() {
		
		ArrayList<Integer> testHand = new ArrayList<>();
		for(int i=0;i<9;i++) {
			testHand.add(allCards.remove(r.nextInt(allCards.size())));
		}
		testHand.sort(null);
		return testHand;
	}
	
	private static ArrayList<Integer> generateAllCards(){
		for(int i = 11; i <= 19; i++) { // add all EICHEL
			allCards.add(i);
		}
		for(int i = 21; i <= 29; i++) { // add all ROSE
			allCards.add(i);
		}
		for(int i = 31; i <= 39; i++) { // add all SCHILTE
			allCards.add(i);
		}
		for(int i = 41; i <= 49; i++) { // add all SCHELLE
			allCards.add(i);
		}
		return allCards;
	}
	
	private static void print(String s, int[] cards) {
		System.out.println(s);
		System.out.println("----------------------");
		for(int id : cards) {
			Card card = new Card(id);
			System.out.println(card.getColor() + " " + card.getValue());
		}
		System.out.println("----------------------");
		System.out.println();
	}
	
	private static void print(String s, Card card) {
		System.out.println(s);
		System.out.println("----------------------");
		System.out.println(card.getColor() + " " + card.getValue());
		System.out.println("----------------------");
		System.out.println();
	}
	
	private static void print(String s, ArrayList<Weis> weise) {
		System.out.println(s);
		System.out.println("----------------------");
		for(Weis w : weise) {
			System.out.println(w.getType());
			System.out.println(w.getOriginCard().getColor() + " " + w.getOriginCard().getValue());
		}
		System.out.println("----------------------");
		System.out.println();
	}
	
	private static int[] al2A(ArrayList<Integer> al) {
		int[] a = new int[al.size()];
		for(int i = 0; i<al.size(); i++) {
			a[i] = al.get(i);
		}
		return a;
	}
	
	private static void updateBots(int[] deck, int id) {
		
		bot1.setDeck(deck);
		bot2.setDeck(deck);
		bot3.setDeck(deck);
		bot4.setDeck(deck);
		
		bot1.updateOutOfCardLists(deck);
		bot2.updateOutOfCardLists(deck);
		bot3.updateOutOfCardLists(deck);
		bot4.updateOutOfCardLists(deck);
		
		bot1.setActivePlayerID(id);
		bot2.setActivePlayerID(id);
		bot3.setActivePlayerID(id);
		bot4.setActivePlayerID(id);
	}
	

}
