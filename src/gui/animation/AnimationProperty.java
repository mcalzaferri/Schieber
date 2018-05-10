package gui.animation;

import java.awt.Dimension;
import java.awt.Point;

public class AnimationProperty {
	private Point point;
	private Dimension dimension;
	private int rotation; //-180..180 Grad
	
	public AnimationProperty(int x, int y, int height, int width, int rotation) {
		this(new Point(x,y), new Dimension(height,width),rotation);
	}
	public AnimationProperty(Point point, Dimension dimension, int rotation) {
		this.setPoint(point);
		this.setDimension(dimension);
		this.rotation = ((rotation + 540) % 360) -180 ;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public Dimension getDimension() {
		return dimension;
	}
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
}
