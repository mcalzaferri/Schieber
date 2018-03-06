package shared;

public class Weis {
	//Datenfelder
	private final WeisType type;
	private final Card originCard;
	
	//Konstruktoren
	public Weis(WeisType type) {
		//Nur für VIERNELL, VIERBAUREN UND STOECK
		this.type = type;
		this.originCard = null;
	}
	
	public Weis(WeisType type, Card originCard) {
		this.type = type;
		this.originCard = originCard;
	}
	
	//Methoden

	
	//Getter und Setter
	public WeisType getType() {
		return type;
	}

	public Card getOriginCard() {
		return originCard;
	}
}
