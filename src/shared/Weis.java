package shared;

import ch.ntb.jass.common.entities.CardEntity;
import ch.ntb.jass.common.entities.WeisEntity;
import ch.ntb.jass.common.entities.WeisTypeEntity;

public class Weis{
	//Datenfelder
	private final WeisType type;
	private final Card originCard;
	private final WeisEntity entity;
	

	//Konstruktoren	
	public Weis(WeisType type, Card originCard) {
		this.type = type;
		this.originCard = originCard;
		entity = new WeisEntity();
		entity.type = WeisTypeEntity.getById(type.getId());
		entity.originCard = CardEntity.getById(originCard.getId());
	}
	/** Use this constructor to cast a WeisEntity into a Weis
	 * @param entity The entity to be cast
	 */
	public Weis(WeisEntity entity) {
		this(WeisType.getByEntity(entity.type),new Card(entity.originCard));
	}
	
	//Static methods
	/** Use this method to cast an Array of WeisEntities into an Array of Weis
	 * @param wiis the Array to be cast
	 * @return The newly created Weis Array
	 */
	public static WeisEntity[] getEntities(Weis[] wiis) {
		WeisEntity[] we;
		if(wiis != null) {
			we = new WeisEntity[wiis.length];
			for(int i = 0; i < wiis.length; i++){
				we[i] = wiis[i].getEntity();
			}
		}else {
			we = null;
		}
		return we;
	}
	
	//Methoden
	/** Returns -1 if the given weis o is higher then this weis
	 * 	Returns 0 if the given weis is the same value as this weis
	 *  Returns 1 if the given weis o is smaller then this weis
	 * @param o
	 * @param trump
	 * @return
	 */
	public int compareTo(Weis o, Trump trump) {
		if(type.compareTo(o.getType()) != 0) {
			return type.compareTo(o.getType());
		}else if(originCard.getValue().compareTo(o.getOriginCard().getValue()) != 0){
			return originCard.getValue().compareTo(o.getOriginCard().getValue());
		}else {
			if(trump.getTrumpfColor() != null && originCard.getColor() != o.getOriginCard().getColor()) {
				if(originCard.getColor() == trump.getTrumpfColor()) {
					return 1;
				}else if(o.getOriginCard().getColor() == trump.getTrumpfColor()) {
					return -1;
				}
			}
			return 0;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Weis) {
			//Use default trump obenabe as this doesnt matter anyways
			return compareTo((Weis)obj, Trump.OBENABE) == 0;
		}else {
			return super.equals(obj);
		}
		
	}
	
	@Override
	public String toString() {
		switch(type) {
		case DREIBLATT:
		case VIERBLATT:
		case FUENFBLATT:
		case SECHSBLATT:
		case SIEBENBLATT:
		case ACHTBLATT:
		case NEUNBLATT:
		case STOECK:
			return type.toString() + " starting with " + originCard.toString();
		case VIERGLEICHE:
			return type.toString() + " of " + originCard.getValue().toString();
		case VIERBAUERN:
		case VIERNELL:
			return type.toString();
		default:
			return super.toString();
		}
	}
	
	//Getter und Setter
	public WeisEntity getEntity() {
		return entity;
	}
	
	public WeisType getType() {
		return type;
	}

	public Card getOriginCard() {
		return originCard;
	}
}
