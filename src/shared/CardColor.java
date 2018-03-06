package shared;

public enum CardColor {
	EICHEL(1),
	ROSE(1),
	SCHILTE(2),
	SCHELLE(2);
	
	//Datenfelder
	private final int ScoreMultiplicator;
	
	//Konstruktoren
	CardColor(int ScoreMultiplicator){
		this.ScoreMultiplicator = ScoreMultiplicator;	
	}
	
	//Methoden
	
	//Getter und Setter
	public int getScoreMultiplicator(){
		return ScoreMultiplicator;
	}
	
}
