package shared;

public enum WeisType {
	STOECK(20),
	DREIBLATT(20),
	VIERBLATT(50),
	FUENFBLATT(100),
	SECHSBLATT(150),
	SIEBENBLATT(200),
	ACHTBLATT(250),
	NEUNBLATT(300),
	VIERGLEICHE(100);
	
	//Datenfelder
	private final int score;
	
	//Konstruktoren
	WeisType(int score){
		this.score = score;
	}
		
	//Methoden
		
	//Getter und Setter
	public int getScore(CardValue cardValue) {
		if (this == VIERGLEICHE && cardValue == CardValue.NEUN) {
			return 150;
		}else if(this == VIERGLEICHE && cardValue == CardValue.UNDER) {
			return 200;
		}else {
			return score;
		}
	}
}
