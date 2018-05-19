package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.HandOutCardsMessage;
import shared.Card;
import shared.Player;

public class StartRoundState extends GameState {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void act() throws IOException {
		logic.initRound();
		com.broadcast(new NewRoundInfoMessage());

		// hand out cards
		for (Player p : logic.getPlayers()) {
			HandOutCardsMessage msg = new HandOutCardsMessage();
			msg.cards = Card.getEntities(p.getCards().toArray());
			com.send(msg, p);
		}

		stateMachine.changeState(new WaitForTrumpState());
	}
}
