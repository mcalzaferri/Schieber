package gui.animation;

import java.awt.Color;
import java.awt.Graphics;

public class TestAnimation extends Animation {

	public TestAnimation() {
		super(100000000);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.pink);
		//Draw some balls
		g.fillOval(100, progress * 10 % 500, 50, 50);
		g.fillOval(150, progress * 10 % 500, 50, 50);
	}

}
