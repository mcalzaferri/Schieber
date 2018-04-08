package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.server_info_messages.*;
import shared.Player;

public class StartGameState extends GameState {
	@Override
	public boolean handle(Player sender, ToServerMessage msg) throws IOException {
		logic.createDeck();
		msgHandler.broadcastMessage(new NewRoundInfoMessage());
		msgHandler.handOutCards();
		// TODO:
//		msgHandler.changeState(new waitForTrumpState);
		return true;
	}
}
