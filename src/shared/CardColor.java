package shared;

public enum CardColor {
	EICHEL(1),
	ROSE(1),
	SCHILTE(2),
	SCHELLE(2);
	
	private final int TrumpfMultiplicator;
	
	public CardColor(int TrumpfMultiplicator){
		this.TrumpfMultiplicator = TrumpfMultiplicator;	
	}
}
