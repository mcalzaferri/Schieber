package server;

import java.io.IOException;
import shared.Communication;
import shared.InternalMessage;
import shared.Player;
import ch.ntb.jass.common.proto.*;

public class ServerCommunication extends Communication {
	public ServerCommunication() {
		super();
	}

	public void waitForMessage(){
		
	}
	
	public Message getMessage(){
		return null;
	}

	public void send(Player player, Message msg) throws IOException {
		super.send(player.getSocketAddress(), msg);
	}

	public InternalMessage receive() throws ClassNotFoundException, IOException {
		return super.internalReceive();
	}
}
