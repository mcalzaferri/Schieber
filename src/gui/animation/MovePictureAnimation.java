package gui.animation;

import java.awt.Dimension;
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
	public void doPaint(Graphics g) {
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
		at.scale(scale, scale);
		at.translate(start.getPoint().x + (getProgress() * ((double)(end.getPoint().x - start.getPoint().x))), 
				start.getPoint().y + (getProgress() * ((double)(end.getPoint().y - start.getPoint().y))));
		at.rotate(Math.toRadians(start.getRotation() + (getProgress() * ((double)(end.getRotation() - start.getRotation())))));
		Dimension pictureScale = getPictureScale();
		at.scale(pictureScale.getWidth(), pictureScale.getHeight());
		return at;
	}
	
	private Dimension getPictureScale() {
		int actualWidth = (int)(start.getDimension().width + (getProgress() * ((double)(end.getDimension().width - start.getDimension().width))));
		int actualHeight = (int)(start.getDimension().height + (getProgress() * ((double)(end.getDimension().height - start.getDimension().height))));
		double widthScale = (double)animatedImage.getWidth() / (double)actualWidth;
		double heightScale = (double)animatedImage.getHeight() / (double)actualHeight;
		Dimension d = new Dimension();
		d.setSize(widthScale, heightScale);
		return d;
	}
}
