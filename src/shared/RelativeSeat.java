package shared;

public enum RelativeSeat {
	NOTATTABLE(0),
	BOTTOM(1),
	RIGHT(2),
	TOP(3),
	LEFT(4);
	
	private final int id;
	
	private RelativeSeat(int id) {
		this.id = id;
	}
	
	public static RelativeSeat getById(int id) {
		switch(id) {
		case 1:
			return BOTTOM;
		case 4:
			return LEFT;
		case 3:
			return TOP;
		case 2:
			return RIGHT;
		default:
			return NOTATTABLE;
		}
	}
	
	public int getId() {
		return id;
	}
}
