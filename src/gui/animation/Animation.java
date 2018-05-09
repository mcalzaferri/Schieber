package gui.animation;

import java.awt.Graphics;

public abstract class Animation {
	public static final int tickRate = 30; //30ms
	protected int tickCount;
	private int duration;
	protected AnimationListener listener;
	
	/** Creats a new Animation which will last for the given duration
	 * @param duration Duration in ms. After this time the animation will finish.
	 * @param listener Listener that will be notified on events. Can be null
	 */
	public Animation(int duration, AnimationListener listener) {
		this.duration = duration;
		this.tickCount = 0;
		this.listener = listener;
	}
	
	public Animation(int duration) {
		this(duration,null);
	}
	
	public void tick() {
		if(tickCount <= 0) {
			animationStarted();
		}
		if(tickCount < (duration / Animation.tickRate)) {
			tickCount++;
		}
		if(hasFinished()) {
			animationFinished();
		}
	}
	
	public boolean hasFinished() {
		return tickCount >= (duration / Animation.tickRate);
	}
	
	public abstract void paint(Graphics g);
	
	private void animationStarted() {
		if(listener != null) {
			listener.animationStarted(new AnimationEvent(getAnimatedObject()));
		}
	}
	private void animationFinished() {
		if(listener != null) {
			listener.animationFinished(new AnimationEvent(getAnimatedObject()));
		}
	}
	protected abstract Object getAnimatedObject();
	
	/** Returns the progress of this animation
	 * @return Value from 0 to 1. Beginning at 0 when animation starts and finishing at 1 when animation stopps.
	 */
	public double getProgress() {
		return (double)(tickCount * tickRate) / (double)duration;
	}
}
