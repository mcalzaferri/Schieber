package server;

import ch.ntb.jass.common.proto.Message;
import server.exceptions.ClientErrorException;
import server.exceptions.InvalidMessageDataException;
import server.exceptions.UnhandledMessageException;
import shared.Player;

import java.io.IOException;

public interface SchieberMessageHandler {
	void handleMessage(Player sender, Message msg) throws
			IOException, InvalidMessageDataException, UnhandledMessageException,
			ClientErrorException;
}
