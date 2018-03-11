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
<<<<<<< HEAD
	NEUNBLATT(300),
	VIERGLEICHE(100),
	VIERNELL(150),
	VIERBAUERN(200);
=======
	NEUNBLATT(300);
>>>>>>> bb8d70a8076ee5751478b4184fe1bd1a6af88db2
	
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
