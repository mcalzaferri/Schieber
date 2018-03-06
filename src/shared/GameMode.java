package shared;

public enum GameMode {
	SCHIEBEN(0),
	TRUMPF(1),
	OBENABE(3),
	UNEUFE(3);
	
	//Datenfelder
	private final int scoreMultiplicator;
	
	//Konstruktoren
	GameMode(int scoreMultiplicator){
		this.scoreMultiplicator = scoreMultiplicator;
	}
	
	//Methoden
	
	//Getter und Setter
	public int getScoreMultiplicator(){
		return scoreMultiplicator;
	}
	
	
}
