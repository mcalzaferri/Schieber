package shared;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import ch.ntb.jass.common.entities.PlayerEntity;

public class Player extends PlayerEntity {
	private Seat seat;				//player seat number (0 to 4) (0 means not at the table)
	private boolean isReady;
	private boolean isAtTable;
	private List<Card> cards;	//cards on hand
	private InetSocketAddress address;

	//Constructors
	public Player(InetSocketAddress address, String name, int seatNr, boolean isBot, boolean isAtTable, boolean isReady, int id) {
		this.address = address;
		this.seat = new Seat(seatNr);
		this.isBot = isBot;
		this.isAtTable = isAtTable;
		this.isReady = isReady;
		this.id = id;
		cards = null;
		if (name.isEmpty()) {
			this.name = address.toString();
		} else {
			this.name = name;
		}
	}

	public Player(InetSocketAddress address, String name, int seatNr) {
		this(address,name,seatNr,false,false,false,0);
	}

	public Player(PlayerEntity entity) {
		this(null, entity.name,entity.seat.seatNr,entity.isBot,false,false,entity.id);
	}

	//Methods
	public void update(PlayerEntity entity) {
		if(id == entity.id) {
			seat = new Seat(entity.seat.seatNr);
			name = entity.name;
			isBot = entity.isBot;
		}
	}
	@Override
	public String toString() {
		return name;
	}

	//Getters and Setters
	public InetSocketAddress getSocketAddress() {
		return address;
	}

	public void putCards(Card[] cs) {
		cards = Arrays.asList(cs);
	}

	public void popCard(Card c) {
		cards.remove(c);
	}

	public void setSeatNr(int nr) {
		seat.seatNr = nr;
	}

	public int getSeatNr() {
		return seat.seatNr;
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
