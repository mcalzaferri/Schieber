package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.ToServerMessage;
import server.GameLogic;
import server.MessageHandler;
import shared.Player;

public abstract class GameState {
	static protected MessageHandler msgHandler;
	static protected GameLogic logic;

	static public void init(MessageHandler mh, GameLogic l) {
		msgHandler = mh;
		logic = l;
	}

	public abstract boolean handle(Player sender, ToServerMessage msg)  throws IOException;
}
