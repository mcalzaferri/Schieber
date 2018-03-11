package shared;

public class Weis implements Comparable<Weis>{
	//Datenfelder
	private final WeisType type;
	private final Card originCard;
	private final Trumpf trumpf;
	
<<<<<<< HEAD
	//Konstruktoren
	public Weis(WeisType type) {
		//Nur für VIERNELL, VIERBAUREN UND STOECK
		this.type = type;
		this.originCard = null;
	}
	
	public Weis(WeisType type, Card originCard) {
=======
	//Konstruktoren	
	public Weis(WeisType type, Card originCard, Trumpf trumpf) {
>>>>>>> bb8d70a8076ee5751478b4184fe1bd1a6af88db2
		this.type = type;
		this.originCard = originCard;
		this.trumpf = trumpf;
	}
	
	//Methoden
	@Override
	public int compareTo(Weis o) {
		if(type.compareTo(o.getType()) != 0) {
			return type.compareTo(o.getType());
		}else if(originCard.getValue().compareTo(o.getOriginCard().getValue()) != 0){
			return originCard.getValue().compareTo(o.getOriginCard().getValue());
		}else {
			if(trumpf.getTrumpfColor() != null && originCard.getColor() != o.getOriginCard().getColor()) {
				if(originCard.getColor() == trumpf.getTrumpfColor()) {
					return 1;
				}else if(o.getOriginCard().getColor() == trumpf.getTrumpfColor()) {
					return -1;
				}
			}
			return 0;
		}
	}
	
	//Getter und Setter
	public WeisType getType() {
		return type;
	}

	public Card getOriginCard() {
		return originCard;
	}
}
