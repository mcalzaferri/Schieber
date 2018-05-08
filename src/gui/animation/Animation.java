package gui.animation;

import java.awt.Graphics;

public abstract class Animation {
	public static final int tickRate = 30; //30ms
	protected int progress;
	private int duration;
	AnimationListener listener;
	/** Creats a new Animation which will last for the given duration
	 * @param duration Number of ticks this animation will last
	 * @param listener Listener that will be notified on events. Can be null
	 */
	public Animation(int duration, AnimationListener listener) {
		this.duration = duration;
		this.progress = 0;
		this.listener = listener;
	}
	
	public Animation(int duration) {
		this(duration,null);
	}
	
	public void tick() {
		if(progress < duration) {
			progress++;
		}
		if(hasFinished() && listener != null) {
			listener.animationFinished();
		}
	}
	
	public boolean hasFinished() {
		return progress >= duration;
	}
	
	public abstract void paint(Graphics g);
}
