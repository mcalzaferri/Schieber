package shared;

import ch.ntb.jass.common.entities.WeisTypeEntity;

public enum WeisType {
	//				ID,	score
	STOECK(			1,	20),
	DREIBLATT(		2,	20),
	VIERBLATT(		3,	50),
	FUENFBLATT(		4,	100),
	VIERGLEICHE(	5,	100),
	VIERNELL(		6,	150),
	SECHSBLATT(		7,	150),
	VIERBAUERN(		8,	200),
	SIEBENBLATT(	9,	200),
	ACHTBLATT(		10,	250),
	NEUNBLATT(		11,	300);
	
	//Datenfelder
	private final int id;
	private final int score;
	
	//Konstruktoren
	WeisType(int id, int score){
		this.id = id;
		this.score = score;
	}
		
	//Methoden
	public static WeisType getByEntity(WeisTypeEntity entity) {
		return getWeisTypeById(entity.getId());
	}
	
	public static WeisType getWeisTypeById(int id) {
		switch(id) {
		case 1:
			return STOECK;
		case 2:
			return DREIBLATT;
		case 3:
			return VIERBLATT;
		case 4:
			return FUENFBLATT;
		case 5:
			return VIERGLEICHE;
		case 6:
			return VIERNELL;
		case 7:
			return SECHSBLATT;
		case 8:
			return VIERBAUERN;
		case 9:
			return SIEBENBLATT;
		case 10:
			return ACHTBLATT;
		case 11:
			return NEUNBLATT;
		}
		return null;
	}
		
	public WeisTypeEntity getEntity() {
		return WeisTypeEntity.getById(id);
	}
	//Getter und Setter
	public int getId() {
		return id;
	}
	
	public int getScore() {
		return score;
	}
}
