package gui.animation;


/** Listener which can be added to animations to get notify when events occur.
 *
 */
public interface AnimationListener {
	/** An event that is fired when the animation starts and is painted for the first time.
	 * @param ae An event containing some information about the animation
	 */
	public abstract void animationStarted(AnimationEvent ae);
	
	/** An event that is fired when the animation has reached its final state.
	 * @param ae An event containing some information about the animation
	 */
	public abstract void animationFinished(AnimationEvent ae);
}
