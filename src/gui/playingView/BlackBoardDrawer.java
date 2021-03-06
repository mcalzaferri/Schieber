package gui.playingView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import gui.Gui;
import shared.Trump;

/**
 * Draws content over a graphics object onto a component. The class supports
 * title, text and trump to be drawn.
 * 
 * @author mstieger
 *
 */
public class BlackBoardDrawer {
	private Rectangle bounds;
	private int titleOffset;
	private int gap;
	
	public BlackBoardDrawer(int gap) {
		this.bounds = new Rectangle();
		this.titleOffset = 0;
		this.gap = gap;
	}
	/**
	 * Sets the drawers bounds
	 * 
	 * @param bounds Bounds within the content is drawn
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * Draws a title onto the graphics object by using the graphics font. The title
	 * is drawn underlined at the top of the drawers bounds.
	 * 
	 * @param g Graphics object of component
	 * @param title Title to be drawn onto component
	 */
	public void drawTitle(Graphics g, String title) {
		titleOffset = (g.getFontMetrics().getHeight());
		g.drawString(title, bounds.x, bounds.y + titleOffset);		
		g.drawLine(bounds.x, bounds.y + titleOffset, 
				bounds.x + g.getFontMetrics().stringWidth(title), bounds.y + titleOffset);
		
	}
	
	/**
	 * Draws text with the given font onto the component by using the graphics object.
	 * Each line of the text array therefore resembles a line of text on the component.
	 * This method should always be called after the drawTitle method if a title is needed.
	 * 
	 * @param g Graphics object of component
	 * @param text Text to be drawn onto component (each element resembles a line)
	 */
	public void drawText(Graphics g, String[] text) {
		String[] strings;
		int x = bounds.x;
		int y = bounds.y + titleOffset + g.getFontMetrics().getHeight();
		
		//Draw text
		for(int i = 0; i < text.length; i++) {
			strings = text[i].split(";");
			if(strings.length >= 2) {
				g.drawString(strings[0], x, y);	//Draw text
				g.drawString(strings[1], bounds.x + bounds.width - g.getFontMetrics().stringWidth(strings[1]), y);	//Draw score			
			}
			y += g.getFontMetrics().getHeight();
		}
		
		titleOffset = 0;
	}
	private BufferedImage img;
	private Trump trump;
	/**
	 * Draw the trump with its text and picture at the bottom the drawers bounds.
	 * 
	 * @param g Graphics object of component
	 * @param trump Actual trump
	 * @param size Size of the trump picture
	 */
	public void drawTrump(Graphics g, Trump trump, Dimension size) {
			
		if(trump != null) {
			if(img == null || this.trump != trump) {
				this.trump = trump;
				try {
					img = Gui.pictureFactory.getPicture(trump);
				} catch (IOException e) {
					// Fatal error => Should not happen
					e.printStackTrace();
				}
			}
			g.drawImage(Gui.pictureFactory.getScaledPicture(img, size), 
					bounds.x + g.getFontMetrics().stringWidth(trump.toString()) + gap, 
					bounds.y + bounds.height - g.getFontMetrics().getHeight(), null);
			
			g.drawString(trump.toString(), bounds.x, 
					bounds.y + bounds.height);
		}					
	}
}
