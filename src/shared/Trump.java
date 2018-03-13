package shared;

public enum Trump {
	SCHIEBEN(GameMode.SCHIEBEN,null),
	EICHEL(GameMode.TRUMPF,CardColor.EICHEL),
	ROSE(GameMode.TRUMPF,CardColor.ROSE),
	SCHILTE(GameMode.TRUMPF,CardColor.SCHILTE),
	SCHELLE(GameMode.TRUMPF,CardColor.SCHELLE),
	OBENABE(GameMode.OBENABE,null),
	UNEUFE(GameMode.UNEUFE,null);
	
	//Datenfelder
	private final GameMode gameMode;
	private final CardColor trumpColor;
	
	//Konstruktoren
	Trump(GameMode gameMode, CardColor trumpColor){
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
	
	//Getter und Setter
	public GameMode getGameMode() {
		return gameMode;
	}
	
	public CardColor getTrumpfColor() {
		return trumpColor;
	}
}
