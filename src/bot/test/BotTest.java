package bot.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import bot.BotIntelligence;
import bot.IntelligenceNormal;
import bot.IntelligenceRandom;
import shared.Card;
import shared.Trump;

public class BotTest {
	
	static ArrayList<Integer> allCards;
	static Random r;

	public static void main(String[] args) {
		
		IntelligenceNormal bot = new IntelligenceNormal();
		r = new Random();
		allCards = new ArrayList<>();
		
		// Generate Random Hand
		int[] testHand = generateRandomHand();
		
		// Generate fixed hand
		// int[] testHand = generateFixedHand();
		
		// print Hand
		System.out.println("Cards on Hand");
		System.out.println("---------------");
		for(int id : testHand) {
			Card card = Card.getCardById(id);
			System.out.println(card.getColor() + " " + card.getValue());
		}
		System.out.println("----------------");
		System.out.println();
		

		bot.setHand(testHand);
		bot.setSelfID(0);
		bot.setEnemyLeftID(1);
		bot.setPartnerID(2);
		bot.setEnemyRightID(3);

		// select Trump
		Trump trump = bot.selectTrump(false);
		System.out.println("Trump");
		System.out.println("---------------");
		System.out.println(trump);
		System.out.println("----------------");
		System.out.println();
		bot.setTrump(trump);
		
		int card1ID = allCards.remove(r.nextInt(allCards.size()));
		int card2ID = allCards.remove(r.nextInt(allCards.size()));
		int card3ID = allCards.remove(r.nextInt(allCards.size()));
		
		bot.setActivePlayerID(1);
		int[] deck1 = {card1ID};
		bot.setDeck(deck1);
		
		bot.setActivePlayerID(2);
		int[] deck2 = {card1ID,card2ID};
		bot.setDeck(deck2);
		
		bot.setActivePlayerID(3);
		int[] deck3 = {card1ID,card2ID,card3ID};
		bot.setDeck(deck3);
		
		bot.setActivePlayerID(0);
		
		// print Deck
		System.out.println("Cards on Table");
		System.out.println("---------------");
		for(int id : deck3) {
			Card card = Card.getCardById(id);
			System.out.println(card.getColor() + " " + card.getValue());
		}
		System.out.println("----------------");
		System.out.println();
		
		ArrayList<Card> allowedCards = bot.getAllowedCards();
		
		// print allowed Cards
		System.out.println("Allowed Cards");
		System.out.println("---------------");
		for(Card card : allowedCards) {
			System.out.println(card.getColor() + " " + card.getValue());
		}
		System.out.println("----------------");
		System.out.println();
		
		/* print values
		System.out.println("Cards on Hand by Color");
		System.out.println("---------------");
	    int[] values = bot.getValueOnHand();
		System.out.println("EICHEL: " + values[0]);
		System.out.println("ROSE: " + values[1]);
		System.out.println("SCHILTE: " + values[2]);
		System.out.println("SCHELLE: " + values[3]);
		System.out.println("----------------");
		System.out.println();
		*/
		
		// play an allowed card
		Card card = bot.getNextCard();
		System.out.println("played card");
		System.out.println("---------------");
		System.out.println(card.getColor() + " " + card.getValue());
		System.out.println("---------------");
		
		int[] deck4 = {card1ID,card2ID,card3ID,card.getId()};
		bot.setDeck(deck4);
		
		//second turn
		bot.setActivePlayerID(0);
		int[] deck = {};
		bot.setDeck(deck);
		
		// play an allowed card
		card = bot.getNextCard();
		System.out.println("played card 2");
		System.out.println("---------------");
		System.out.println(card.getColor() + " " + card.getValue());
		System.out.println("---------------");
		

	}
	
	private static int[] generateFixedHand() {
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
		int[] testHand = new int[9];
		for(int i=0;i<9;i++) {
			testHand[i] = allCards.remove(0);
		}
		Arrays.sort(testHand);
		return testHand;
	}

	private static int[] generateRandomHand() {
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

		int[] testHand = new int[9];
		for(int i=0;i<9;i++) {
			testHand[i] = allCards.remove(r.nextInt(allCards.size()));
		}
		Arrays.sort(testHand);
		return testHand;
	}
	

}
