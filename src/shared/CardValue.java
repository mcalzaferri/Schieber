package shared;

public enum CardValue {
	//(	ID,		Defaultvalue, 	Trumpfvalue, 	GeneralScore, 	TrumpfScore, 	UneufeScore, 	ObenabeScore)
	SECHS(	0,		6,		6,		0,		0,		0,		11),
	SIEBEN(	1,		7,		7,		0,		0,		0,		0),
	ACHT(	2,		8,		8,		0,		0,		8,		8),
	NEUN(	3,		9,		15,		0,		14,		0,		0),
	ZEHN(	4,		10,		10,		10,		10,		10,		10),
	UNDER(	5,		11,		16,		2,		20,		2,		2),
	OBER(	6,		12,		12,		3,		3,		3,		3),
	KOENIG(	7,		13,		13,		4,		4,		4,		4),
	ASS(	8,		14,		14,		11,		11,		11,		0);
	
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
	public static CardValue getValueById(int id) {
		switch(id) {
			case 0:
				return SECHS;
			case 1:
				return SIEBEN;
			case 2:
				return ACHT;
			case 3:
				return NEUN;
			case 4:
				return ZEHN;
			case 5:
				return UNDER;
			case 6:
				return OBER;
			case 7:
				return KOENIG;
			case 8:
				return ASS;
		}
		return null;
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
