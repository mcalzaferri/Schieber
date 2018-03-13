package shared;

public class Weis implements Comparable<Weis>{
	//Datenfelder
	private final WeisType type;
	private final Card originCard;
	private final Trumpf trumpf;
	

	//Konstruktoren	
	public Weis(WeisType type, Card originCard, Trumpf trumpf) {
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
