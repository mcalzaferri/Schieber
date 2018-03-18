package shared;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player /*extends EntityPlayer*/{
	private int id;
	private String name;
	private int score;
	private List<Card> cards;	//cards on hand
	private InetSocketAddress address;

	public Player(String ip, int port) {
		address = new InetSocketAddress(ip, port);
	}

	public InetSocketAddress getSocketAddress() {
		return address;
	}

	public void addScore(int s) {
		score += s;
	}

	public int getScore() {
		return score;
	}

	public void resetScore() {
		score = 0;
	}

	public int getId() {
		return id;
	}

	public void putCards(Card[] cs) {
		cards = Arrays.asList(cs);
	}

	public void popCard(Card c) {
		cards.remove(c);
	}
}
