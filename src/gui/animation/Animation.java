package gui.animation;

import java.awt.Graphics;

public abstract class Animation {
	public static final int tickRate = 1; //30ms
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
		this.duration = duration;
		this.scale = 1;
		this.listener = listener;
		this.startTime = 0;
		started = false;
		finished = false;
	}
	
	public Animation(int duration) {
		this(duration,null);
	}
	
	public boolean hasFinished() {
		return finished;
	}
	
	protected abstract void doPaint(Graphics g, double progress);
	
	public final void paintAnimation(Graphics g) {
		if(!started) {
			startTime = System.currentTimeMillis();
			animationStarted();
			started = true;
		}
		if(started && !finished) {
			double progress = getProgress();
			doPaint(g, progress);
		}
		if(getElapsedTime() >= duration && !finished) {
			animationFinished();
			finished = true;
		}
		
	}
	
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
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	/** Returns the progress of this animation
	 * @return Value from 0 to 1. Beginning at 0 when animation starts and finishing at 1 when animation stopps.
	 */
	public double getProgress() {
		return Math.min(((double)(getElapsedTime()) / (double)duration) * 200% 1.0,1.0);
	}
	
	protected int getElapsedTime() {
		return (int)(System.currentTimeMillis() - startTime);
	}
}
