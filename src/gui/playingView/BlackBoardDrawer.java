package gui.playingView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import gui.Gui;
import shared.Trump;

public class BlackBoardDrawer {
	private Rectangle bounds;
	private Font font;
	private double scale;
	public final double titleFactor = 1.5;
	
	public BlackBoardDrawer() {
		this.bounds = new Rectangle();
		this.font = new Font("MV Boli" ,Font.PLAIN, 36);
		this.scale = 1;
	}
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	public void drawText(Graphics g, String[] text) {

		int x = (int)(scale*bounds.x);
		int y = (int)(scale*(bounds.y + this.font.getSize()*this.titleFactor));
		for(int i = 0; i < text.length; i++) {
			if(i == 0) {
				//Draw title
				Font titleFont = this.getTitleFont();
				g.setFont(titleFont);
				g.setColor(Color.WHITE);
				g.drawString(text[i], x, y);
				y += titleFont.getSize();
			}
			else {
				//Draw text
				Font f = this.getNormalFont();
				g.setFont(f);
				g.setColor(Color.white);
				g.drawString(text[i], x, y);
				y += f.getSize();
			}
		}
	}
	
	public void drawTrump(Graphics g, Trump trump) {
		BufferedImage img;
		try {
			g.setFont(this.getNormalFont());
			g.setColor(Color.WHITE);
			g.drawString(trump.toString(), (int)(scale*bounds.x), (int)(scale*(bounds.y + bounds.height/2)));
			
			Dimension d = new Dimension((int)(scale*50), (int)(scale*70));
			img = Gui.pictureFactory.getPicture(trump, d);
			g.drawImage(img, (int)(scale*(bounds.x + bounds.width/2) - d.getWidth()/2), (int)(scale*(bounds.y + bounds.height/2) - d.getHeight()/2), null);
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Font getNormalFont() {
		return new Font(this.font.getFontName(), this.font.getStyle(), (int)(this.font.getSize() * scale));
	}
	
	private Font getTitleFont() {
		return new Font(this.font.getFontName(), Font.BOLD, (int)(this.font.getSize()*this.titleFactor*scale));
	}
}
