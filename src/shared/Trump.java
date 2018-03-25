package shared;

public enum Trump {
	SCHIEBEN(1, GameMode.SCHIEBEN,null),
	EICHEL(2, GameMode.TRUMPF,CardColor.EICHEL),
	ROSE(3, GameMode.TRUMPF,CardColor.ROSE),
	SCHILTE(4, GameMode.TRUMPF,CardColor.SCHILTE),
	SCHELLE(5, GameMode.TRUMPF,CardColor.SCHELLE),
	OBENABE(6, GameMode.OBENABE,null),
	UNEUFE(7, GameMode.UNEUFE,null);
	
	//Datenfelder
	private final int id;
	private final GameMode gameMode;
	private final CardColor trumpColor;
	
	//Konstruktoren
	Trump(int id, GameMode gameMode, CardColor trumpColor){
		this.id = id;
		this.gameMode = gameMode;
		this.trumpColor = trumpColor;
	}
	
	//Methoden
	public int getScoreMultiplicator(){
		if(gameMode == GameMode.TRUMPF) {
			return gameMode.getScoreMultiplicator() * trumpColor.getScoreMultiplicator();
		}else {
			return gameMode.getScoreMultiplicator();
		}
	}
	
	public static Trump getById(int id) {
		switch(id) {
		case 1:
			return SCHIEBEN;
		case 2:
			return EICHEL;
		case 3:
			return ROSE;
		case 4:
			return SCHILTE;
		case 5:
			return SCHELLE;
		case 6:
			return OBENABE;
		case 7:
			return UNEUFE;
		}
		return null;
	}
	
	//Getter und Setter
	public int getId() {
		return id;
	}
	
	public GameMode getGameMode() {
		return gameMode;
	}
	
	public CardColor getTrumpfColor() {
		return trumpColor;
	}
}
