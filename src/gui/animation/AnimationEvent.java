package gui.animation;

/** Event that can be fired during an Animation. It contains some information about the Animation.
 *
 */
public class AnimationEvent {
	private Object animatedObject;
	
	/** Creates a new AnimationEvent containing the given data.
	 * @param animatedObject Some data that represent the animated object.
	 */
	public AnimationEvent(Object animatedObject) {
		this.animatedObject = animatedObject;
	}
	
	/**
	 * @return The represented object of the animation
	 */
	public Object getAnimatedObject() {
		return animatedObject;
	}
}
