package gui.animation;

import java.awt.Dimension;
import java.awt.Point;

/** Describes a position with detailed information about size and orientation. 
 * This class should be used to define positions in an animation path.
 */
public class AnimationProperty {
	private Point point;
	private Dimension dimension;
	private int rotation; //-180..180 Degree
	
	/** Creates a new Object of this class, initialized with the given parameters.
	 * @param x The x value of the position
	 * @param y The y value of the position
	 * @param height The height which the animation should have at the given position
	 * @param width The width which the animation should have at the given position
	 * @param rotation The orientation which the animation should have at the given position
	 */
	public AnimationProperty(int x, int y, int height, int width, int rotation) {
		this(new Point(x,y), new Dimension(height,width),rotation);
	}
	
	/** Creates a new Object of this class, initialized with the given parameters.
	 * @param point The initial position of this property.
	 * @param dimension The dimensions which the animation should have at the given position
	 * @param rotation The orientation which the animation should have at the given position
	 */
	public AnimationProperty(Point point, Dimension dimension, int rotation) {
		this.setPoint(point);
		this.setDimension(dimension);
		this.rotation = ((rotation + 540) % 360) -180 ; //Scale rotation value between -180 and 180 degree
	}

	/** Returns the value of the specified field of this object
	 * @return The value of the specified field
	 */
	public int getRotation() {
		return rotation;
	}
	
	/** Sets the value of the specified field to the given value.
	 * @param rotation The value the specified field should be set to
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	/** Returns the value of the specified field of this object
	 * @return The value of the specified field
	 */
	public Point getPoint() {
		return point;
	}
	
	/** Sets the value of the specified field to the given value.
	 * @param rotation The value the specified field should be set to
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	
	/** Returns the value of the specified field of this object
	 * @return The value of the specified field
	 */
	public Dimension getDimension() {
		return dimension;
	}
	
	/** Sets the value of the specified field to the given value.
	 * @param rotation The value the specified field should be set to
	 */
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
}
