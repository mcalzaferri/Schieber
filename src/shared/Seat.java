package shared;

import ch.ntb.jass.common.entities.SeatEntity;

public enum Seat{
	//Constants
	NOTATTABLE(0),
	SEAT1(1),
	SEAT2(2),
	SEAT3(3),
	SEAT4(4);
	
	//Static fields
	private final int seatNr;
	
	//Constructors
	private Seat(int seatNr) {
		this.seatNr = seatNr;
	}
	
	//Static methods
	/** This method returns the Seat which is bound to the specified seatNr.
	 * @param seatNr The seatNr of the Seat that should be returned
	 * @return The Seat which is bound to the given seatNr
	 */
	public static Seat getBySeatNr(int seatNr) {
		switch(seatNr) {
		case 1:
			return SEAT1;
		case 2:
			return SEAT2;
		case 3:
			return SEAT3;
		case 4:
			return SEAT4;
		default:
			return NOTATTABLE;
		}
	}
	
	//Methods
	public SeatEntity getSeatEntity() {
		return SeatEntity.getBySeatNr(seatNr);
	}
	
	/** This method returns the relative position of the player. The relative position is relative to the clients position.
	 * Only use this method for display purposes.
	 * @return The relative Position to the client. (If the client is not at the table BOTTOM is SEAT1 ...)
	 */
	public RelativeSeat getRelativeSeat(Seat clientSeat) {
		if(clientSeat == null || clientSeat == Seat.NOTATTABLE) {
			return RelativeSeat.getById(getSeatNr());
		}else {
			return RelativeSeat.getById(1 + ((getSeatNr() - clientSeat.getSeatNr()+4) % 4));
		}
	}

	
	//Getters and setters
	/** Returns the id of this seat
	 * @return The id of this seat
	 */
	public int getSeatNr() {
		return seatNr;
	}
}
