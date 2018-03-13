package shared.proto;

import shared.client.AbstractClient;

public class JoinGameMessage extends ToServerMessage {
	
	AbstractClient player;
	
	public JoinGameMessage(AbstractClient player)
	{
		this.player = player;
	}
	
}
