package shared;

public enum GameMode {
	SCHIEBEN(0),
	TRUMPF(1),
	OBENABE(3),
	UNEUFE(3);
	
	//Datenfelder
	private final int ScoreMultiplicator;
	
	//Konstruktoren
	GameMode(int ScoreMultiplicator){
		this.ScoreMultiplicator = ScoreMultiplicator;
	}
	
	//Methoden
	
	//Getter und Setter
	public int getScoreMultiplicator(){
		return ScoreMultiplicator;
	}
	
	
}
