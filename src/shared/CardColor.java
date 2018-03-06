package shared;

public enum CardColor {
	EICHEL(1),
	ROSE(1),
	SCHILTE(2),
	SCHELLE(2);
	
	private final int ScoreMultiplicator;
	
	public CardColor(int ScoreMultiplicator){
		this.ScoreMultiplicator = ScoreMultiplicator;	
	}
}
