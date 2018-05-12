package gui.animation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class TestAnimation extends Animation {
	public TestAnimation() {
		super(100000000);
	}

	public void doPaint(Graphics g) {
		g.setColor(Color.pink);
		//Draw some balls
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform at = g2d.getTransform();
		g2d.translate(100, getProgress() * 10 % 500);
		g2d.fillOval(0, 0, 50, 50);
		g2d.translate(50,0);
		g2d.fillOval(0, 0, 50, 50);
		g2d.setTransform(at);
	}

	@Override
	protected Object doGetAnimatedObject() {
		return null;
	}

	@Override
	protected void doPaint(Graphics g, double progress) {
		// TODO Auto-generated method stub
		
	}
}
