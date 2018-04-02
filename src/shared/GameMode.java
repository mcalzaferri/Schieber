package shared;

public enum GameMode {
	SCHIEBEN(1,0),
	TRUMPF(2,1),
	OBENABE(3,3),
	UNEUFE(4,3);
	
	//Datenfelder
	private final int id;
	private final int scoreMultiplicator;
	
	//Konstruktoren
	GameMode(int id, int scoreMultiplicator){
		this.id = id;
		this.scoreMultiplicator = scoreMultiplicator;
	}
	
	//Methoden
	public static GameMode getById(int id) {
		switch(id) {
		case 1:
			return SCHIEBEN;
		case 2:
			return TRUMPF;
		case 3:
			return OBENABE;
		case 4:
			return UNEUFE;
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
