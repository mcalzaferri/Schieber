package gui;

import java.awt.Graphics;

public abstract class Animation {
	private int progress;
	private int duration;
	/** Creats a new Animation which will last for the given duration
	 * @param duration Number of ticks this animation will last
	 */
	public Animation(int duration) {
		this.duration = duration;
		this.progress = 0;
	}
	
	public void tick() {
		if(progress < duration) {
			progress++;
		}
	}
	
	public boolean hasFinished() {
		return progress >= duration;
	}
	
	public abstract void paint(Graphics g);
}
