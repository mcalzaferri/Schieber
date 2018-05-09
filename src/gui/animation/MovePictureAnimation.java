package gui.animation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MovePictureAnimation extends Animation {
	private BufferedImage animatedImage;
	private AnimationProperty start;
	private AnimationProperty end;
	public MovePictureAnimation(BufferedImage animatedImage, int duration, AnimationProperty start, AnimationProperty end) {
		super(duration);
		this.animatedImage = animatedImage;
		this.start = start;
		this.end = end;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform saveAt = g2d.getTransform();
		g2d.setTransform(new AffineTransform());
		g2d.drawImage(animatedImage, getCurrentTransform(), null);
		g2d.setTransform(saveAt);
	}

	@Override
	protected Object getAnimatedObject() {
		return animatedImage;
	}

	private AffineTransform getCurrentTransform() {
		AffineTransform at = new AffineTransform();
		at.translate(start.getX() + (getProgress() * ((double)(end.getX() - start.getX()))), 
				start.getY() + (getProgress() * ((double)(end.getY() - start.getY()))));
		at.rotate(Math.toRadians(start.getRotation() + (getProgress() * ((double)(end.getRotation() - start.getRotation())))));
		return at;
	}
}
