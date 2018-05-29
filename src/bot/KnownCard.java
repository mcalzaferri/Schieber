package bot;

import shared.Card;

public class KnownCard {
	
	private Card card;
	private int playerID;
	private boolean played;

	public KnownCard(Card c, int pid, boolean pl) {
		card = c;
		playerID = pid;
		played = pl;
	}

	public Card getCard() {
		return card;
	}

	public int getPlayer() {
		return playerID;
	}

	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	

	
	

}
