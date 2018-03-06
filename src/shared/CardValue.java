package shared;

public enum CardValue {
	//(	Defaultvalue, 	Trumpfvalue, 	GeneralScore, 	TrumpfScore, 	UneufeScore, 	ObenabeScore)
	SECHS(	6,		6,		0,		0,		0,		11),
	SIEBEN(	7,		7,		0,		0,		0,		0),
	ACHT(	8,		8,		0,		0,		8,		8),
	NEUN(	9,		15,		0,		14,		0,		0),
	ZEHN(	10,		10,		10,		10,		10,		10),
	UNDER(	11,		16,		2,		20,		2,		2),
	OBER(	12,		12,		3,		3,		3,		3),
	KOENIG(	13,		13,		4,		4,		4,		4),
	ASS(	14,		14,		11,		11,		11,		0);
	
	private final int defaultValue;
	private final int trumpfValue;
	private final int generalScore;
	private final int trumpfScore;
	private final int uneufeScore;
	private final int obenabeScore;
	
	//Konstruktoren
	CardValue(int defaultValue, int trumpfValue, int generalScore, int trumpfScore, int uneufeScore, int obenabeScore){
		this.defaultValue = defaultValue;
		this.trumpfValue = trumpfValue;
		this.generalScore = generalScore;
		this.trumpfScore = trumpfScore;
		this.uneufeScore = uneufeScore;
		this.obenabeScore = obenabeScore;
	}
	//Methoden
	//TODO
	
	//Getters und Setters
	public int getDefaultValue() {
		return defaultValue;
	}

	public int getTrumpfValue() {
		return trumpfValue;
	}

	public int getGeneralScore() {
		return generalScore;
	}

	public int getTrumpfScore() {
		return trumpfScore;
	}

	public int getUneufeScore() {
		return uneufeScore;
	}

	public int getObenabeScore() {
		return obenabeScore;
	}
}
