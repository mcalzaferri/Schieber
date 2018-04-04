package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BufferedDrawer{
	private BufferedImage buffer;
	
	public BufferedDrawer(){
		this.buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	}
	
	public Graphics getGraphics(Dimension d) {
		this.buffer = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		return this.buffer.getGraphics();
	}
	
	public void drawOnGraphics(Graphics g) {
		g.drawImage(this.buffer, 0, 0, null);
	}

}
