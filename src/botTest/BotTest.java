package botTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import bot.BotIntelligence;
import bot.IntelligenceRandom;
import shared.Card;
import shared.Trump;

public class BotTest {

	public static void main(String[] args) {
		
		BotIntelligence bot = new IntelligenceRandom();
		
		// Generate Random Hand
		Random r = new Random();
		ArrayList<Integer> allCards = new ArrayList<>();
		for(int i = 10; i <= 18; i++) { // add all EICHEL
			allCards.add(i);
		}
		for(int i = 20; i <= 28; i++) { // add all ROSE
			allCards.add(i);
		}
		for(int i = 30; i <= 38; i++) { // add all SCHILTE
			allCards.add(i);
		}
		for(int i = 40; i <= 48; i++) { // add all SCHELLE
			allCards.add(i);
		}
		
		int[] testHand = new int[9];
		for(int i=0;i<9;i++) {
			testHand[i] = allCards.remove(r.nextInt(allCards.size()));
		}
		Arrays.sort(testHand);
		
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
		
		// select Trump
		Trump trump = bot.selectTrump(false);
		System.out.println("Trump");
		System.out.println("---------------");
		System.out.println(trump);
		System.out.println("----------------");
		System.out.println();
		bot.setTrump(trump);
		
		// generate deck (some played cards)
		final int testDeckSize = 3;
		int[] deck = new int[testDeckSize];
		for(int i=0;i<testDeckSize;i++) {
			deck[i] = allCards.remove(r.nextInt(allCards.size()));
		}
		
		// print Deck
		System.out.println("Cards on Table");
		System.out.println("---------------");
		for(int id : deck) {
			Card card = Card.getCardById(id);
			System.out.println(card.getColor() + " " + card.getValue());
		}
		System.out.println("----------------");
		System.out.println();
		
		bot.setDeck(deck);
		ArrayList<Card> allowedCards = bot.getAllowedCards();
		
		// print allowed Cards
		System.out.println("Allowed Cards");
		System.out.println("---------------");
		for(Card card : allowedCards) {
			System.out.println(card.getColor() + " " + card.getValue());
		}
		System.out.println("----------------");
		System.out.println();
		
		// print values
		System.out.println("Cards on Hand by Color");
		System.out.println("---------------");
	    int[] values = bot.getValueOnHand();
		System.out.println("EICHEL: " + values[0]);
		System.out.println("ROSE: " + values[1]);
		System.out.println("SCHILTE: " + values[2]);
		System.out.println("SCHELLE: " + values[3]);
		System.out.println("----------------");
		System.out.println();
		
		// play an allowed card
		Card card = bot.getNextCard();
		System.out.println("played card");
		System.out.println("---------------");
		System.out.println(card.getColor() + " " + card.getValue());
		System.out.println("---------------");

	}

}
