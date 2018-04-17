package shared;

import ch.ntb.jass.common.entities.SeatEntity;

public enum Seat{
	//Constants
	NOTATTABLE(0),
	CLIENT(1),
	LEFTENEMY(2),
	PARTNER(3),
	RIGHTENEMY(4);
	
	//Static fields
	private final int id;
	private static SeatEntity clientSeat;
	
	//Constructors
	private Seat(int id) {
		this.id = id;
	}
	
	//Static methods
	/** This method returns the Seat which is bound to the specified id.
	 * @param id The id of the Seat that should be returned
	 * @return The Seat which is bound to the given id
	 */
	public static Seat getById(int id) {
		switch(id) {
		case 1:
			return CLIENT;
		case 2:
			return LEFTENEMY;
		case 3:
			return PARTNER;
		case 4:
			return RIGHTENEMY;
		default:
			return NOTATTABLE;
		}
	}
	
	public static Seat getBySeatNr(int seatNr) {
		if(seatNr == 0 || clientSeat.seatNr == 0) {
			return Seat.NOTATTABLE;
		}else {
			return getById(1 + ((seatNr + clientSeat.seatNr - 2) % 4));
		}
	}
	
	//Methods
	/** This method will calculate the actual Seat this player is sitting at.
	 * @return The actual position of this player at the table
	 */
	public SeatEntity getSeatEntity() {
		SeatEntity ret = new SeatEntity();
		if(this == Seat.NOTATTABLE || clientSeat.seatNr == 0) {
			ret.seatNr = 0;
		}else {
			ret.seatNr = 1 + ((id + clientSeat.seatNr - 2) % 4);
		}
		return ret;
	}

	
	//Getters and setters
	/** Use this method to change the actual location of the player at the table (Even tho the value of the player will always be PLAYER or NOTATTABLE)
	 * This will update all occurences of Seat.
	 * Usually this method should be called when the teams are defined and therefor the location of the players is broadcasted.
	 * @param clientSeat The new value for the players actual position at the table
	 */
	public static void setClientSeat(SeatEntity clientSeat) {
		Seat.clientSeat = clientSeat;
	}
	/** This method will return the current SeatEntity that is bound to the Seat enum. (Which contains the actual position of the player at the table)
	 * @return The players current SeatNr
	 */
	public static SeatEntity getClientSeat() {
		return Seat.clientSeat;
	}
	/** Returns the id of this seat
	 * @return The id of this seat
	 */
	public int getId() {
		return id;
	}
}
