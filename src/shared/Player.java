package shared;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class Player /*extends EntityPlayer*/{
	private int id;				//player number (0 to 3)
	private String name;
	private int score;
	private List<Card> cards;	//cards on hand
	private InetSocketAddress address;

	public Player(InetSocketAddress address, String name, int id) {
		this.address = address;
		this.id = id;
		score = 0;

		if (name.isEmpty()) {
			this.name = address.toString();
		} else {
			this.name = name;
		}
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

	public void putCards(Card[] cs) {
		cards = Arrays.asList(cs);
	}

	public void popCard(Card c) {
		cards.remove(c);
	}

	public int getId() {
		return id;
	}
}
