package bot.test;

import java.util.ArrayList;
import java.util.Random;

import bot.IntelligenceNormal;
import shared.Card;
import shared.Trump;

/**
 * This class is used to test if the Bot correctly calculates the allowed Cards for any state of the Game.
 * Note: Manual Test!
 *
 */

public class allowedCardTest {

	static ArrayList<Integer> allCards = new ArrayList<>();
	static Random r = new Random(4);
	static IntelligenceNormal bot;
	
	public static void main(String[] args) {
		
		bot = new IntelligenceNormal();
		generateAllCards();
		ArrayList<Integer> hand = generateRandomHand();
		bot.setHand(al2A(hand));
		Trump trump = bot.selectTrump(false);
		bot.setTrump(trump);
		
		hand.remove(0);
		bot.setHand(al2A(hand));
		
		int[] deck = new int[3];
		deck[0] = allCards.remove(r.nextInt(allCards.size()));
		deck[1] = allCards.remove(r.nextInt(allCards.size()));
		deck[2] = allCards.remove(r.nextInt(allCards.size()));
		bot.setDeck(deck);
		ArrayList<Card> ac = bot.getAllowedCards();
		
		System.out.println("Hand");
		System.out.println("-----------");
		for(int i : hand) {
			Card c = new Card(i);
			System.out.println(c.getColor() + " " + c.getValue());
		}
		System.out.println("-----------");
		System.out.println();
		
		System.out.println("Trump");
		System.out.println("-----------");
		System.out.println(trump.getTrumpfColor());
		System.out.println("-----------");
		System.out.println();
		
		System.out.println("Deck");
		System.out.println("-----------");
		for(int i : deck) {
			Card c = new Card(i);
			System.out.println(c.getColor() + " " + c.getValue());
		}
		System.out.println("-----------");
		System.out.println();
		
		System.out.println("allowed");
		System.out.println("-----------");
		for(Card c : ac) {
			System.out.println(c.getColor() + " " + c.getValue());
		}
		System.out.println("-----------");
		System.out.println();
		
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
	
	private static ArrayList<Integer> generateRandomHand() {
		
		ArrayList<Integer> testHand = new ArrayList<>();
		for(int i=0;i<9;i++) {
			testHand.add(allCards.remove(r.nextInt(allCards.size())));
		}
		testHand.sort(null);
		return testHand;
	}
	
	private static int[] al2A(ArrayList<Integer> al) {
		int[] a = new int[al.size()];
		for(int i = 0; i<al.size(); i++) {
			a[i] = al.get(i);
		}
		return a;
	}
	
}
