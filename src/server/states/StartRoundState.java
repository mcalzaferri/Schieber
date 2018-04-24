package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.HandOutCardsMessage;
import shared.Card;
import shared.Player;

public class StartRoundState extends GameState {
	/**
	 * @see GameState#act()
	 */
	@Override
	public void act() throws IOException {
		logic.createDeck();
		broadcast(new NewRoundInfoMessage());
		handOutCards();
		
		stateMachine.changeState(new WaitForTrumpState());
	}

	private void handOutCards() throws IOException {
		for (Player p : logic.getPlayers()) {
			HandOutCardsMessage msg = new HandOutCardsMessage();
			msg.cards = Card.getEntities(logic.assignCardsToPlayer(p));
			send(msg, p);
		}
	}
}
