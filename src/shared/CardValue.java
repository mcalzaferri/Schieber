package shared;

public enum CardValue {
	//(Defaultvalue, Trumpfvalue, GeneralScore, TrumpfScore, UneufeScore, ObenabeScore)
	SECHS(6,6,0,0,0,11),
	SIEBEN(7,7,0,0,0,0),
	ACHT(8,8,0,0,0,8),
	NEUN(9,15,0,14,0,0),
	ZEHN(10,10,10,10,10,10),
	UNDER(11,16,2,20,2,2),
	OBER(12,12,3,3,3,3),
	KOENIG(13,13,4,4,4,4),
	ASS(14,14,11,11,11,0);
	
	private final int DefaultValue;
	private final int TrumpfValue;
	private final int GeneralScore;
	private final int TrumpfScore;
	private final int UneufeScore;
	private final int ObenabeScore;
	
	public CardValue(int DefaultValue, int TrumpfValue, int GeneralScore, int TrumpfScore, int UneufeScore, int ObenabeScore){
		this.DefaultValue = DefaultValue;
		this.TrumpfValue = TrumpfValue;
		this.GeneralScore = GeneralScore;
		this.TrumpfScore = TrumpfScore;
		this.UneufeScore = UneufeScore;
		this.ObenabeScore = ObenabeScore;
	}
}
