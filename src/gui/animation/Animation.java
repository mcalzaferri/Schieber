package gui.animation;

import java.awt.Graphics;

/** Template class for several Animations. When displaying new Animations make use of the constants in this class.
 * @author Maurus Calzaferri
 *
 */
public abstract class Animation {
	public static final int tickRate = 5; //The maximum rate at which all animations are rendered. The actual render interval is usually slower due to high gui latency.
	public static final int handOutCardDuration = 500;
	public static final int layCardDuration = 200;
	public static final int cardToStackDuration = 500;
	private static final double initialDurationScale = 1.0;
	public static double durationScale = initialDurationScale; //Modify the speed of all animations
	private long startTime;
	private int duration;
	protected AnimationListener listener;
	protected double scale;
	private boolean started;
	private boolean finished;
	
	/** Creats a new Animation which will last for the given duration
	 * @param duration Duration in ms. After this time the animation will finish.
	 * @param listener Listener that will be notified on events. Can be null
	 */
	public Animation(int duration, AnimationListener listener) {
		this.duration = (int)((double)duration / durationScale);
		this.scale = 1;
		this.listener = listener;
		this.startTime = 0;
		started = false;
		finished = false;
	}
	
	/** Creates a new Animatino which will last for the given duration
	 * @param duration Duration in ms. After this time the animation will finish.
	 */
	public Animation(int duration) {
		this(duration,null);
	}
	
	/** Check if this animation has finished or not.
	 * @return True if the animation has finished. False otherwise.
	 */
	public boolean hasFinished() {
		return finished;
	}
	
	/** Template method to paint the animation on a Graphics object.
	 * @param g The Graphics object where the animation should be painted on.
	 * @param progress The current progress of this animation from 0.0 to 1.0. (0.0 = started, 1.0 = finished)
	 */
	protected abstract void doPaint(Graphics g, double progress);
	
	/** Algorithm to paint each animation. It will call other template methods inside.
	 * @param g The Graphics object where the animation should be painted on.
	 */
	public final void paintAnimation(Graphics g) {
		if(!started) {
			startTime = System.currentTimeMillis();
			animationStarted(); //Notify listener
			started = true;
		}
		if(started && !finished) {
			double progress = getProgress();
			doPaint(g, progress); //Paint the animation
		}
		if(getElapsedTime() >= duration && !finished) {
			animationFinished(); //Notify listener
			finished = true;
		}
		
	}
	
	/** Internal method to notify the AnimationListener that the animation has started.
	 * 
	 */
	private void animationStarted() {
		if(listener != null) {
			listener.animationStarted(new AnimationEvent(doGetAnimatedObject()));
		}
	}
	
	/** Internal method to notify the AnimationListener that the animation has finished
	 * 
	 */
	private void animationFinished() {
		if(listener != null) {
			listener.animationFinished(new AnimationEvent(doGetAnimatedObject()));
		}
	}
	
	/** Template method to gather some information about the animation which will be returned in an AnimationEvent
	 * @return Some data of the animation object
	 */
	protected abstract Object doGetAnimatedObject();
	
	/** Scales the content of this animation to the given scale. All subclasses have to use this when painting the animation.
	 * @param scale The new scale of this animation
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	/** Returns the progress of this animation
	 * @return Value from 0 to 1. Beginning at 0 when animation starts and finishing at 1 when animation stopps.
	 */
	public double getProgress() {
		return Math.min(((double)(getElapsedTime()) / (double)duration),1.0);
	}
	
	/** Returns the elapsed time in ms since the animation started.
	 * @return Elapsed time in ms since the animation started.
	 */
	protected int getElapsedTime() {
		return (int)(System.currentTimeMillis() - startTime);
	}
	
	/** Sets the durationScale to another value to speed up or slow down all new animations
	 * @param scale 2.0 Will run all animations twice as fast, 0.5 will slow them down
	 */
	public static void setDurationScale(double scale) {
		durationScale = Math.max(0.1, scale);
	}
	
	/** Resets the durationScale to its initial value so all animations will be played at the regular speed.
	 * 
	 */
	public static void resetDurationScale() {
		durationScale = initialDurationScale;
	}
}
