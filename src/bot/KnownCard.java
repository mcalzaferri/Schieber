package bot;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.Player;

public class KnownCard {
	
	private Card card;
	private Player player;
	private boolean played;

	public KnownCard(Card c, Player p, boolean pl) {
		card = c;
		player = p;
		played = pl;
	}

	public Card getCard() {
		return card;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	

	
	

}
