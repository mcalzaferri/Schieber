package shared;

public enum CardColor {
	EICHEL(1,1),
	ROSE(2,1),
	SCHILTE(3,2),
	SCHELLE(4,2);
	
	//Datenfelder
	private final int id;
	private final int scoreMultiplicator;
	
	//Konstruktoren
	CardColor(int id, int scoreMultiplicator){
		this.id = id;
		this.scoreMultiplicator = scoreMultiplicator;	
	}
	
	//Methoden
	public static CardColor getColorById(int id) {
		switch(id) {
			case 1:
				return EICHEL;
			case 2:
				return ROSE;
			case 3:
				return SCHILTE;
			case 4:
				return SCHELLE;
		}
		return null;
	}
	//Getter und Setter
	public int getId() {
		return id;
	}
	public int getScoreMultiplicator(){
		return scoreMultiplicator;
	}
}
