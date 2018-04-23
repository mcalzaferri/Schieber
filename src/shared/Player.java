package shared;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.SeatEntity;

public class Player extends PlayerEntity {
	private Seat seat;				//player seat number (0 to 4) (0 means not at the table)
	private boolean isReady;
	private List<Card> cards;	//cards on hand
	private InetSocketAddress address;

	//Constructors
	public Player(InetSocketAddress address, String name, Seat seat, boolean isBot, boolean isReady, int id) {
		setSocketAddress(address);
		setSeat(seat);
		setBot(isBot);
		setReady(isReady);
		setId(id);
		cards = null;
		if (name.isEmpty() && address != null) {
			setName(address.toString());
		} else {
			setName(name);
		}
	}

	public Player(InetSocketAddress address, String name, Seat seat) {
		this(address,name,seat,false,false,0);
	}

	public Player(PlayerEntity entity) {
		this(null, entity.name,Seat.getBySeatNr(entity.seat.getSeatNr()),entity.isBot,false,entity.id);
	}

	//Methods
	public void update(PlayerEntity entity) {
		if(id == entity.id) {
			setSeatNr(entity.seat.getSeatNr());
			setName(entity.name);
			setBot(entity.isBot);
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
	
	private void setSocketAddress(InetSocketAddress address) {
		this.address = address;
	}

	public void putCards(Card[] cs) {
		cards = Arrays.asList(cs);
	}

	public void popCard(Card c) {
		cards.remove(c);
	}
	
	public Seat getSeat() {
		return seat;
	}
	
	public void setSeat(Seat seat) {
		this.seat = seat;
		super.seat = SeatEntity.getBySeatNr(seat.getSeatNr());
	}

	public void setSeatNr(int nr) {
		setSeat(Seat.getBySeatNr(nr));
	}

	public int getSeatNr() {
		return seat.getSeatEntity().getSeatNr();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public boolean isBot() {
		return isBot;
	}
	
	private void setBot(boolean isBot) {
		this.isBot = isBot;
	}

	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public boolean isAtTable() {
		return (seat != null && seat != Seat.NOTATTABLE);
	}
}
