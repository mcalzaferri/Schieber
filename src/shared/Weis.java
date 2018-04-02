package shared;

import ch.ntb.jass.common.entities.CardEntity;
import ch.ntb.jass.common.entities.WeisEntity;
import ch.ntb.jass.common.entities.WeisTypeEntity;

public class Weis extends WeisEntity{
	//Datenfelder
	private final WeisType type;
	private final Card originCard;
	

	//Konstruktoren	
	public Weis(WeisType type, Card originCard) {
		this.type = type;
		this.originCard = originCard;
		super.type = WeisTypeEntity.getById(type.getId());
		super.originCard = CardEntity.getById(originCard.getId());
	}
	
	public Weis(WeisEntity entity) {
		this(WeisType.getByEntity(entity.type),new Card(entity.originCard));
	}
	
	//Methoden
	
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
	
	//Getter und Setter
	public WeisType getType() {
		return type;
	}

	public Card getOriginCard() {
		return originCard;
	}
}
