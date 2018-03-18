package server;

import java.io.IOException;
import shared.Communication;
import shared.Player;
import ch.ntb.jass.common.proto.*;

public class ServerCommunication extends Communication {
	public ServerCommunication() {
		super();
	}

	public void send(Player player, Message msg) throws IOException {
		super.send(player.getSocketAddress(), msg);
	}
}
