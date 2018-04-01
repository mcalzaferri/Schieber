package shared;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import ch.ntb.jass.common.entities.PlayerEntity;

public class Player /*extends EntityPlayer*/{
	private int seatNr;				//player seat number (0 to 4) (0 means not at the table)
	private int id;
	private boolean isBot;
	private boolean isReady;
	private boolean isAtTable;
	private String name;
	private List<Card> cards;	//cards on hand
	private InetSocketAddress address;

	public Player(InetSocketAddress address, String name, int seatNr) {
		this.address = address;
		this.seatNr = seatNr;

		if (name.isEmpty()) {
			this.name = address.toString();
		} else {
			this.name = name;
		}
	}

	public Player(PlayerEntity entity) {
		name = entity.name;
		seatNr = entity.seat.seatNr;
		isBot = entity.isBot;
		id = entity.id;
	}
	
	public void update(PlayerEntity entity) {
		if(id == entity.id) {
			seatNr = entity.seat.seatNr;
			name = entity.name;
			isBot = entity.isBot;
		}
	}
	
	public InetSocketAddress getSocketAddress() {
		return address;
	}

	public void putCards(Card[] cs) {
		cards = Arrays.asList(cs);
	}

	public void popCard(Card c) {
		cards.remove(c);
	}

	public int getSeatNr() {
		return seatNr;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isBot() {
		return isBot;
	}
	
	public String getName() {
		return name;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public boolean isAtTable() {
		return isAtTable;
	}

	public void setAtTable(boolean isAtTable) {
		this.isAtTable = isAtTable;
	}
}
