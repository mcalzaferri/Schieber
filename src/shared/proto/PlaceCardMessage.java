package shared.proto;

import shared.*;
import shared.client.AbstractClient;

public class PlaceCardMessage extends ToServerMessage {
	
	public AbstractClient player;
	public Card card;
	
	
	public PlaceCardMessage(AbstractClient player, Card card)
	{
		this.player = player;
		this.card = card;
	}
	
}
