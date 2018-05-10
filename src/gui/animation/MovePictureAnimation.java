package gui.animation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
	protected void doPaint(Graphics g, double progress) {
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform saveAt = g2d.getTransform();
		g2d.setTransform(new AffineTransform());
		g2d.drawImage(animatedImage, getCurrentTransform(progress), null);
		g2d.setTransform(saveAt);
	}


	@Override
	protected Object getAnimatedObject() {
		return animatedImage;
	}
	
	private Point getCurrentLocation(double progress) {
		Point p = new Point();
		p.setLocation(start.getPoint().x + (progress * ((double)(end.getPoint().x - start.getPoint().x))),
				start.getPoint().y + (progress * ((double)(end.getPoint().y - start.getPoint().y))));
		return p;
	}
	
	private Dimension getCurrentSize(double progress) {
		Dimension d = new Dimension();
		d.setSize((start.getDimension().width + (progress * ((double)(end.getDimension().width - start.getDimension().width)))), 
				(start.getDimension().height + (progress * ((double)(end.getDimension().height - start.getDimension().height)))));
		return d;
	}
	
	private double getCurrentRotation(double progress) {
		return Math.toRadians(start.getRotation() + (progress * ((double)(end.getRotation() - start.getRotation()))));
	}

	private AffineTransform getCurrentTransform(double progress) {
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		Point currentP = getCurrentLocation(progress);
		at.translate(currentP.getX(), currentP.getX());
		at.rotate(getCurrentRotation(progress));
		Dimension pictureScale = getPictureScale(progress);
		at.scale(pictureScale.getWidth(), pictureScale.getHeight());
		return at;
	}
	
	private Dimension getPictureScale(double progress) {
		Dimension currentD = getCurrentSize(progress);
		double widthScale = (double)animatedImage.getWidth() / currentD.getWidth();
		double heightScale = (double)animatedImage.getHeight() / currentD.getHeight();
		Dimension d = new Dimension();
		d.setSize(widthScale, heightScale);
		return d;
	}
}
