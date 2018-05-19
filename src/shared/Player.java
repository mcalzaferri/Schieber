package shared;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.SeatEntity;

public class Player {
	private boolean isReady;
	private CardList cards;	//cards on hand
	private CardList cardsOnStack; //cards on stack
	private InetSocketAddress address;
	private PlayerEntity entity;

	//Constructors
	public Player(InetSocketAddress address, String name, Seat seat, boolean isBot, boolean isReady, int id) {
		entity = new PlayerEntity();
		setSocketAddress(address);
		setSeat(seat);
		setBot(isBot);
		setReady(isReady);
		setId(id);
		cards = new CardList();
		cardsOnStack = new CardList();
		if (name.isEmpty() && address != null) {
			setName(address.toString());
		} else {
			setName(name);
		}
	}

	public Player(InetSocketAddress address, String name, Seat seat) {
		this(address,name,seat,false,false,0);
	}

	public Player(InetSocketAddress address, int id) {
		this(address, address.toString(), null,false,false, id);
	}

	/** Use this constructor to cast a PlayerEntity into a Player
	 * @param entity The entity to be cast
	 */
	public Player(PlayerEntity entity) {
		this(null, entity.name,Seat.getBySeatNr(entity.seat.getSeatNr()),entity.isBot,false,entity.id);
	}

	//Static Methods
	/** Use this method to cast an Array of PlayerEntities into an Array of Players
	 * @param players the Array to be cast
	 * @return The newly created Player Array
	 */
	public static PlayerEntity[] getEntities(Player[] players) {
		PlayerEntity[] pe;
		if(players != null) {
			pe = new PlayerEntity[players.length];
			for(int i = 0; i < players.length; i++) {
				pe[i] = players[i] == null ? null : players[i].getEntity();
			}
		}else {
			pe = null;
		}
		return pe;
	}

	//Methods
	public void update(PlayerEntity entity) {
		if(getId() == entity.id) {
			setSeatNr(entity.seat.getSeatNr());
			setName(entity.name);
			setBot(entity.isBot);
		}
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == null || obj == null) {
			return false;
		}else if(super.equals(obj)) {
			return true;
		}else if(obj instanceof Player){
			Player p = (Player)obj;
			return p.getId() == this.getId();
		}else if(obj instanceof PlayerEntity){
			PlayerEntity p = (PlayerEntity)obj;
			return p.id == this.getId();
		}else {
			return false;
		}
	}
	
	public boolean equals(PlayerEntity obj) {
		return this.equals((Object)obj);
	}

	//Getters and Setters
	/** Use this method to get the underlying entity of this class
	 * @return An entity representing this class
	 */
	public PlayerEntity getEntity() {
		return entity;
	}

	public InetSocketAddress getSocketAddress() {
		return address;
	}

	private void setSocketAddress(InetSocketAddress address) {
		this.address = address;
	}

	public void putCards(Card[] cs) {
		cards.updateData(cs);
	}
	
	public void putCards(ArrayList<Card> cs) {
		cards.updateData(cs);
	}

	public boolean removeCard(Card c) {
		return cards.remove(c);
	}

	public void setCards(CardList cards) {
		this.cards = cards;
	}

	public CardList getCards() {
		return cards;
	}
	
	public void addCardsToStack(Card[] cs) {
		for(Card c : cs) {
			cardsOnStack.add(c);
		}
	}
	
	public void addCardsToStack(ArrayList<Card> cs) {
		for(Card c : cs) {
			cardsOnStack.add(c);
		}
	}

	public CardList getCardsOnStack() {
		return cardsOnStack;
	}

	public Seat getSeat() {
		return entity.seat == null ? null : Seat.getBySeatNr(entity.seat.getSeatNr());
	}

	public void setSeat(Seat seat) {
		entity.seat = seat == null ? null : SeatEntity.getBySeatNr(seat.getSeatNr());
	}

	public void setSeatNr(int nr) {
		setSeat(Seat.getBySeatNr(nr));
	}

	public int getSeatNr() {
		return entity.seat.getSeatNr();
	}

	public int getId() {
		return entity.id;
	}

	public void setId(int id) {
		this.entity.id = id;
	}

	public boolean isBot() {
		return entity.isBot;
	}

	public void setBot(boolean isBot) {
		this.entity.isBot = isBot;
	}

	public String getName() {
		return entity.name;
	}

	public void setName(String name) {
		this.entity.name = name;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public boolean isAtTable() {
		return (entity.seat != null && entity.seat != SeatEntity.NOTATTABLE);
	}

	public boolean isInLobby() {
		return SeatEntity.NOTATTABLE.equals(entity.seat);
	}
}
