package shared;

public enum WeisType {
	STOECK(20),
	DREIBLATT(20),
	VIERBLATT(50),
	FUENFBLATT(100),
	VIERGLEICHE(100),
	VIERNELL(150),
	SECHSBLATT(150),
	VIERBAUERN(200),
	SIEBENBLATT(200),
	ACHTBLATT(250),
	NEUNBLATT(300);
	
	//Datenfelder
	private final int score;
	
	//Konstruktoren
	WeisType(int score){
		this.score = score;
	}
		
	//Methoden
		
	//Getter und Setter
	public int getScore() {
		return score;
	}
}
