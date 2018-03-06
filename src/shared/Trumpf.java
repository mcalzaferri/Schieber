package shared;

public enum Trumpf {
	SCHIEBEN(GameMode.SCHIEBEN,null),
	EICHEL(GameMode.TRUMPF,CardColor.EICHEL),
	ROSE(GameMode.TRUMPF,CardColor.ROSE),
	SCHILTE(GameMode.TRUMPF,CardColor.SCHILTE),
	SCHELLE(GameMode.TRUMPF,CardColor.SCHELLE),
	OBENABE(GameMode.OBENABE,null),
	UNEUFE(GameMode.UNEUFE,null);
	
	//Datenfelder
	private GameMode gameMode;
	private CardColor cardColor;
	
	//Konstruktoren
	Trumpf(GameMode gameMode, CardColor cardColor){
		this.gameMode = gameMode;
		this.cardColor = cardColor;
	}
	
	//Methoden
	public int getScoreMultiplicator(){
		if(gameMode == GameMode.TRUMPF) {
			return gameMode.getScoreMultiplicator() * cardColor.getScoreMultiplicator();
		}else {
			return gameMode.getScoreMultiplicator();
		}
	}
	
	//Getter und Setter
	public GameMode getGameMode() {
		return gameMode;
	}
	
	public CardColor getCardColor() {
		return cardColor;
	}
}
