package gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

import shared.Card;
import shared.CardColor;
import shared.CardValue;
import shared.Trump;


public class PictureFactory {
	private HashMap<String, BufferedImage> pictures;
	
	public PictureFactory() throws IOException {
		pictures = new HashMap<>();
		
		/*
		 * By having the pictures loaded only once into an array, the time
		 * to load from file system falls away which results in much shorter
		 * method calls (in the moment only for card pictures)
		 */
		Path path;
		String name;
		for(CardValue cv : CardValue.values()) {
			for(CardColor cc : CardColor.values()) {
				name = cc.toString() + cv.getDefaultValue();
				path = Paths.get("res", "cards", name + ".png");
				pictures.put(name, loadImage(path.toString()));
			}		
		}
	}
	
	public enum Pictures{
		Carpet, BlackBoard, CoverNorth, CoverEast, CoverSouth, CoverWest, Table, Lobby;
	}
	
	public BufferedImage getPicture(Card card) throws IOException {
		//Return a deep copy of the picture in the hash map => prevents original picture in map from changing
		return deepCopy(pictures.get(card.getColor().toString() + card.getValue().getDefaultValue()));
	}
	public Image getPicture(Card card, Dimension size) throws IOException {
		return this.getScaledPicture(this.getPicture(card), size);

	}
	public BufferedImage getPicture(Trump trump) throws IOException {
		Path path = Paths.get("res", "modes", trump.toString() + ".png");
		return this.loadImage(path.toString());
	}
	public Image getPicture(Trump trump, Dimension size) throws IOException {
		return this.getScaledPicture(this.getPicture(trump), size);
	}

	public BufferedImage getPicture(Pictures picture) throws IOException {
		Path path = Paths.get("res", picture.toString() + ".png");
		return this.loadImage(path.toString());
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
	
	public static BufferedImage deepCopy(BufferedImage bi) {
	    ColorModel cm = bi.getColorModel();
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
	    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

}
