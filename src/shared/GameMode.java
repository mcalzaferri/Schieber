package shared;

public enum GameMode {
	SCHIEBEN(0),
	TRUMPF(1),
	OBENABE(3),
	UNEUFE(3);
	
	private final int ScoreMultiplicator;
	
	public GameMode(int ScoreMultiplicator){
		this.ScoreMultiplicator = ScoreMultiplicator;
	}
}
