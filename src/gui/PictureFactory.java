package gui;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import shared.Card;
import shared.Trump;

public class PictureFactory {
	public BufferedImage getPicture(Card card) throws IOException {
		String path = "res\\cards\\" + card.getColor().toString() + card.getValue().getDefaultValue() + ".png";
		return this.loadImage(path);
		
	}
	public BufferedImage getPicture(Card card, Dimension size) throws IOException {
		return this.getScaledPicture(this.getPicture(card), size);
		
	}
	public BufferedImage getPicture(Trump trump) throws IOException {
		String path = "res\\modes\\" + trump.toString() + ".png";
		return this.loadImage(path);
	}
	public BufferedImage getPicture(Trump trump, Dimension size) throws IOException {
		return this.getScaledPicture(this.getPicture(trump), size);
	}
	private BufferedImage getScaledPicture(BufferedImage original, Dimension size) {
		//Draws original picture into new picture using graphics object
		int newWidth = size.width;
		int newHeigth = size.height;
		BufferedImage newImage = new BufferedImage(newWidth, newHeigth, BufferedImage.TYPE_INT_RGB);

		Graphics g = newImage.createGraphics();
		g.drawImage(original, 0, 0, newWidth, newHeigth, null);
		g.dispose();
		return newImage;
	}
	private BufferedImage loadImage(String path) throws IOException {	        
    	path = new File(path).getAbsolutePath();
    	URL url = new File(path).toURI().toURL();
        return  ImageIO.read(url);
    }
	
}
