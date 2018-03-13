package shared;

public class Weis implements Comparable<Weis>{
	//Datenfelder
	private final WeisType type;
	private final Card originCard;
	private final Trump trump;
	

	//Konstruktoren	
	public Weis(WeisType type, Card originCard, Trump trump) {
		this.type = type;
		this.originCard = originCard;
		this.trump = trump;
	}
	
	//Methoden
	@Override
	public int compareTo(Weis o) {
		if(type.compareTo(o.getType()) != 0) {
			return type.compareTo(o.getType());
		}else if(originCard.getValue().compareTo(o.getOriginCard().getValue()) != 0){
			return originCard.getValue().compareTo(o.getOriginCard().getValue());
		}else {
			if(trump.getTrumpfColor() != null && originCard.getColor() != o.getOriginCard().getColor()) {
				if(originCard.getColor() == trump.getTrumpfColor()) {
					return 1;
				}else if(o.getOriginCard().getColor() == trump.getTrumpfColor()) {
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
