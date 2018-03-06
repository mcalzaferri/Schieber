package shared;

public enum CardColor {
	EICHEL(1),
	ROSE(1),
	SCHILTE(2),
	SCHELLE(2);
	
	//Datenfelder
	private final int scoreMultiplicator;
	
	//Konstruktoren
	CardColor(int scoreMultiplicator){
		this.scoreMultiplicator = scoreMultiplicator;	
	}
	
	//Methoden
	
	//Getter und Setter
	public int getScoreMultiplicator(){
		return scoreMultiplicator;
	}
	
}
