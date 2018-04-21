package gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

import shared.Card;
import shared.Trump;

public class PictureFactory {
	public enum Pictures{
		Carpet, BlackBoard, CoverNorth, CoverEast, CoverSouth, CoverWest, Table;
	}
	public BufferedImage getPicture(Card card) throws IOException {
		String path = "res\\cards\\" + card.getColor().toString() + card.getValue().getDefaultValue() + ".png";
		return this.loadImage(path);
		
	}
	public Image getPicture(Card card, Dimension size) throws IOException {
		return this.getScaledPicture(this.getPicture(card), size);
		
	}
	public BufferedImage getPicture(Trump trump) throws IOException {
		String path = "res\\modes\\" + trump.toString() + ".png";
		return this.loadImage(path);
	}
	public Image getPicture(Trump trump, Dimension size) throws IOException {
		return this.getScaledPicture(this.getPicture(trump), size);
	}
	
	public BufferedImage getPicture(Pictures picture) throws IOException {
		String path = "res\\" + picture.toString() + ".png";
		return this.loadImage(path);
	}
	public Image getPicture(Pictures picture, Dimension size) throws IOException {
		return this.getScaledPicture(this.getPicture(picture), size);
	}
	public Image getScaledPicture(BufferedImage original, Dimension size) {
		if(size.width <= 0 || size.height <= 0) {
			size.width = 1; size.height = 1;	//Prevents illegal argument exception in getScaledInstance-call
		}
		return original.getScaledInstance(size.width, size.height, Image.SCALE_FAST);
	}
	
	public Image getGrayPicture(Image original) {
		ImageFilter filter = new GrayFilter(true, 50);  
		ImageProducer producer = new FilteredImageSource(original.getSource(), filter);  
		return Toolkit.getDefaultToolkit().createImage(producer); 
	}
	public BufferedImage loadImage(String path) throws IOException {	        
    	path = new File(path).getAbsolutePath();
    	URL url = new File(path).toURI().toURL();
        return  ImageIO.read(url);
    }
	
}
