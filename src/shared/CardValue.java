package shared;

import ch.ntb.jass.common.entities.CardValueEntity;

public enum CardValue {
	//(    ID, Defaultvalue,  Trumpfvalue,  GeneralScore, TrumpfScore, UneufeScore, ObenabeScore)
	SECHS(  1,            6,            6,             0,           0,          11,            0),
	SIEBEN( 2,            7,            7,             0,           0,           0,            0),
	ACHT(   3,            8,            8,             0,           0,           8,            8),
	NEUN(   4,            9,           15,             0,          14,           0,            0),
	ZEHN(   5,           10,           10,            10,          10,          10,           10),
	UNDER(  6,           11,           16,             2,          20,           2,            2),
	OBER(   7,           12,           12,             3,           3,           3,            3),
	KOENIG( 8,           13,           13,             4,           4,           4,            4),
	ASS(    9,           14,           14,            11,          11,           0,           11);

	private final int id;
	private final int defaultValue;
	private final int trumpValue;
	private final int generalScore;
	private final int trumpScore;
	private final int uneufeScore;
	private final int obenabeScore;

	//Konstruktoren
	CardValue(int id, int defaultValue, int trumpValue, int generalScore, int trumpScore, int uneufeScore, int obenabeScore){
		this.id = id;
		this.defaultValue = defaultValue;
		this.trumpValue = trumpValue;
		this.generalScore = generalScore;
		this.trumpScore = trumpScore;
		this.uneufeScore = uneufeScore;
		this.obenabeScore = obenabeScore;
	}
	//Methoden
	public static CardValue getById(int id) {
		switch(id) {
			case 1:
				return SECHS;
			case 2:
				return SIEBEN;
			case 3:
				return ACHT;
			case 4:
				return NEUN;
			case 5:
				return ZEHN;
			case 6:
				return UNDER;
			case 7:
				return OBER;
			case 8:
				return KOENIG;
			case 9:
				return ASS;
		}
		return null;
	}

	public static CardValue getByEntity(CardValueEntity entity) {
		return getById(entity.getId());
	}

	public CardValueEntity getEntity() {
		return CardValueEntity.getById(id);
	}

	//Getters und Setters
	public int getId() {
		return id;
	}
	public int getDefaultValue() {
		return defaultValue;
	}

	public int getTrumpValue() {
		return trumpValue;
	}

	public int getGeneralScore() {
		return generalScore;
	}

	public int getTrumpScore() {
		return trumpScore;
	}

	public int getUneufeScore() {
		return uneufeScore;
	}

	public int getObenabeScore() {
		return obenabeScore;
	}
}
