package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.HandOutCardsMessage;
import shared.Player;

public class StartGameState extends GameState {
	@Override
	public boolean handleMessage(Player sender, ToServerMessage msg) throws IOException {
		logic.createDeck();
		broadcast(new NewRoundInfoMessage());
		handOutCards();
		// TODO
		//msgHandler.changeState(new waitForTrumpState());
		return true;
	}

	private void handOutCards() throws IOException {
		for (Player p : logic.getPlayers()) {
			HandOutCardsMessage msg = new HandOutCardsMessage();
			msg.cards = logic.assignCardsToPlayer(p);
			send(msg, p);
		}
	}
}
