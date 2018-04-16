package gui.playingView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import gui.Gui;
import gui.PictureFactory.Pictures;
import shared.Card;
import shared.CardList;
import shared.Player;

/**
 * This class draws the content of the carpet onto a given component
 * by using its graphics object. The contents are the current deck, each
 * players hidden hand and their names.
 * 
 * @author mstieger
 *
 */
public class CarpetDrawer {
	private int gap;
	private BufferedImage imgNorth;
	private BufferedImage imgEast;
	private BufferedImage imgSouth;
	private BufferedImage imgWest;
	
	/**
	 * Creates a new CarpetDrawer and initializes its data
	 * 
	 * @param gap Gap in pixels between the users hand and the name
	 */
	public CarpetDrawer(int gap) {
		this.gap = gap;
		try {
			this.imgNorth = Gui.pictureFactory.getPicture(Pictures.CoverNorth);
			this.imgEast = Gui.pictureFactory.getPicture(Pictures.CoverEast);
			this.imgSouth = Gui.pictureFactory.getPicture(Pictures.CoverSouth);
			this.imgWest = Gui.pictureFactory.getPicture(Pictures.CoverWest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Image img;
	/**
	 * Draws the current deck in the middle of the carpet.
	 * 
	 * @param g Components graphics object
	 * @param deck Deck to be displayed
	 * @param carpetSize Size of the component representing the carpet
	 * @param cardSize Size for the decks cards
	 */
	public void drawDeck(Graphics g, CardList deck, Dimension carpetSize, Dimension cardSize) {
		int index = 0; int x = 0; int y = 0;
		int xOffset = -cardSize.width/2;
		int yOffset = -cardSize.height/2;
		
		for(Card c : deck) {				
			try {
				img = Gui.pictureFactory.getPicture(c, cardSize);
				switch(index) {
				case 0:
					x = carpetSize.getSize().width/2 + xOffset;
					y = carpetSize.getSize().height*2/3 + yOffset;
					g.drawImage(img, x, y, null);
					break;
				case 1:
					x = carpetSize.getSize().width*2/3 + xOffset;
					y = carpetSize.getSize().height/2 + yOffset;
					g.drawImage(img, x, y, null);
					break;
				case 2:
					x = carpetSize.getSize().width/2 + xOffset;
					y = carpetSize.getSize().height*1/3 + yOffset;
					g.drawImage(img, x, y, null);
					break;
				case 3:
					x = carpetSize.getSize().width*1/3 + xOffset;
					y = carpetSize.getSize().height/2 + yOffset;
					g.drawImage(img, x, y, null);
					break;
				}
				index++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Draws a player, represented by his hand and name onto a component
	 * 
	 * @param g Components graphics object
	 * @param position Position of the player (North, East, South or West)
	 * @param player Instance of player class
	 * @param carpetSize Size of the component representing the carpet
	 * @param cardSize Size of the hidden cards of the player
	 */
	public void drawPlayer(Graphics g, String position, Player player, Dimension carpetSize, Dimension cardSize) {
		int stringWidth = g.getFontMetrics().stringWidth(player.getName());
		int stringHeight = g.getFontMetrics().getHeight();
		Dimension cardSizeInv = new Dimension(cardSize.height, cardSize.width); //For images on the left/right
		
		int nOfCards = 5;	// TODO Get number of cards in players hand
		
		//Draw name and hand at specified position
		if(position.equals("North")){
			img = Gui.pictureFactory.getScaledPicture(imgNorth, cardSize);
			for(int i = 0; i < nOfCards; i++) {
				g.drawImage(img, (carpetSize.width + (nOfCards + 1)*cardSize.width/2)/2 - i*cardSize.width/2 - cardSize.width, 
						0, null);
			}
			g.drawString(player.getName(), (carpetSize.width - stringWidth)/2, gap + stringHeight + cardSize.height);
			
		}else if(position.equals("East")) {
			img = Gui.pictureFactory.getScaledPicture(imgEast, cardSizeInv);
			for(int i = 0; i < nOfCards; i++) {
				g.drawImage(img, carpetSize.width - cardSizeInv.width, 
						(carpetSize.height + (nOfCards + 1)*cardSizeInv.height/2)/2 - i*cardSizeInv.height/2 - cardSizeInv.height, null);
			}
			g.drawString(player.getName(), (carpetSize.width - stringWidth - gap - cardSizeInv.width), (carpetSize.height + stringHeight)/2);
			
		}else if(position.equals("South")) {
			img = Gui.pictureFactory.getScaledPicture(imgSouth, cardSize);
			for(int i = 0; i < nOfCards; i++) {
				g.drawImage(img, (carpetSize.width - (nOfCards + 1)*cardSize.width/2)/2 + i*cardSize.width/2 , 
						carpetSize.height - cardSize.height, null);
			}
			g.drawString(player.getName(), (carpetSize.width - stringWidth)/2, carpetSize.height - gap - cardSize.height);
			
		}else if(position.equals("West")) {
			img = Gui.pictureFactory.getScaledPicture(imgWest, cardSizeInv);
			for(int i = 0; i < nOfCards; i++) {
				g.drawImage(img, 0, 
						(carpetSize.height - (nOfCards + 1)*cardSizeInv.height/2)/2 + i*cardSizeInv.height/2, null);
			}
			g.drawString(player.getName(), gap + cardSizeInv.width, (carpetSize.height + stringHeight)/2);
		}
	}
	
}
