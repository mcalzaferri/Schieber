package gui.animation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MovePictureAnimation extends Animation {
	private BufferedImage animatedImage;
	private AnimationProperty start;
	private AnimationProperty end;
	public MovePictureAnimation(BufferedImage animatedImage, int duration, AnimationProperty start, AnimationProperty end, AnimationListener listener) {
		super(duration,listener);
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
	protected Object doGetAnimatedObject() {
		return animatedImage;
	}
	
	private Point getCurrentLocation(double progress) {
		Point p = new Point();
		p.setLocation(start.getPoint().x + (progress * (end.getPoint().x - start.getPoint().x)),
				start.getPoint().y + (progress * (end.getPoint().y - start.getPoint().y)));
		return p;
	}
	
	private PrecisionDimension getCurrentSize(double progress) {
		PrecisionDimension d = new PrecisionDimension(
		(start.getDimension().width + (progress * (end.getDimension().width - start.getDimension().width))), 
				(start.getDimension().height + (progress * (end.getDimension().height - start.getDimension().height))));
		return d;
	}
	
	private double getCurrentRotation(double progress) {
		return Math.toRadians(start.getRotation() + (progress * (end.getRotation() - start.getRotation())));
	}

	private AffineTransform getCurrentTransform(double progress) {
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		Point currentP = getCurrentLocation(progress);
		at.translate(currentP.getX(), currentP.getY());
		PrecisionDimension pictureScale = getPictureScale(progress);
		at.scale(pictureScale.width, pictureScale.height);
		double currentR = getCurrentRotation(progress);
		double xTranslation = 0;
		double yTranslation = 0;
		if(currentR >= 0) {
			xTranslation += Math.abs(Math.sin(currentR)*animatedImage.getHeight());
			if(currentR <= (Math.PI/2)) {
				//Zwischen 0 und 90Grad
				
			}else {
				//Zwischen 90 und 180Grad
				xTranslation += Math.abs(Math.cos(currentR)*animatedImage.getWidth());
				yTranslation += Math.abs(Math.cos(currentR)*animatedImage.getHeight());
			}
		}else {
			yTranslation += Math.abs(Math.sin(currentR)*animatedImage.getWidth());
			if(-currentR <= (Math.PI/2)) {
				//Zwischen -0 und -90 Grad
			}else {
				//Zwischen -90 und -180 Grad
				xTranslation += Math.abs(Math.cos(currentR)*animatedImage.getWidth());
				yTranslation += Math.abs(Math.cos(currentR)*animatedImage.getHeight());
			}
		}
		at.translate(xTranslation, yTranslation);
		at.rotate(getCurrentRotation(progress));
		return at;
	}
	
	private PrecisionDimension getPictureScale(double progress) {
		PrecisionDimension currentD = getCurrentSize(progress);
		double widthScale = currentD.width / animatedImage.getWidth();
		double heightScale = currentD.height / animatedImage.getHeight() ;
		PrecisionDimension d = new PrecisionDimension(widthScale,heightScale);
		return d;
	}
}
