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
import shared.RelativeSeat;
import shared.Seat;
import shared.client.ClientModel;

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
			// Fatal Error => Should not happen
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
	public void drawDeck(Graphics g, ClientModel model, Dimension carpetSize, Dimension cardSize) {
		int index = 0; int x = 0; int y = 0;
		int xOffset = -cardSize.width/2;
		int yOffset = -cardSize.height/2;
		CardList deck = model.getDeck();
		if(deck != null) {
			for(Card c : deck) {
				if(c != null) {
					try {
						img = Gui.pictureFactory.getPicture(c, cardSize);
						switch(model.getDeckCardOrientation(c)) {
						case BOTTOM:
							x = carpetSize.getSize().width/2 + xOffset;
							y = carpetSize.getSize().height*2/3 + yOffset;
							g.drawImage(img, x, y, null);
							break;
						case RIGHT:
							x = carpetSize.getSize().width*2/3 + xOffset;
							y = carpetSize.getSize().height/2 + yOffset;
							g.drawImage(img, x, y, null);
							break;
						case TOP:
							x = carpetSize.getSize().width/2 + xOffset;
							y = carpetSize.getSize().height*1/3 + yOffset;
							g.drawImage(img, x, y, null);
							break;
						case LEFT:
							x = carpetSize.getSize().width*1/3 + xOffset;
							y = carpetSize.getSize().height/2 + yOffset;
							g.drawImage(img, x, y, null);
							break;
						default:
							System.err.println("Error in CarpetDrawer. Unknown Orientation of Card!");
							break;
						}
						index++;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
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

	public void drawPlayer(Graphics g, Player player, Player thisPlayer, Dimension carpetSize, Dimension cardSize) {
		//Only draw player if not null
		if(player != null) {
			//Set up text to be drawn
			String text = player.getName();
			if(text == null) {
				text = "No name";
			}
			text = getString(g, text, carpetSize.width/4);	//Reduce text to given length that it fits in graphics
			
			int stringWidth = g.getFontMetrics().stringWidth(text);
			int stringHeight = g.getFontMetrics().getHeight();

			Dimension cardSizeInv = new Dimension(cardSize.height, cardSize.width); //For images on the left/right
			
			int nOfCards = 0;
			try {
				nOfCards = player.getCards().size();
			}catch(NullPointerException ex) {
				//Do nothing => let cards be 0
			}

			/*
			 * DRAW PLAYERS
			 * Draws player according to their given seat.
			 */
			RelativeSeat seat = RelativeSeat.NOTATTABLE;
			try {
				seat = player.getSeat().getRelativeSeat(thisPlayer.getSeat());
			}
			catch(NullPointerException ex) {
				ex.printStackTrace();
			}

			switch(seat) {
			case BOTTOM:
				img = Gui.pictureFactory.getScaledPicture(imgSouth, cardSize);
				for(int i = 0; i < nOfCards; i++) {
					if(player.getCards().get(i).isUnknown()) {
						g.drawImage(img, (carpetSize.width + (nOfCards + 1)*cardSize.width/2)/2 - i*cardSize.width/2 - cardSize.width, 
								carpetSize.height - cardSize.height, null);
					}
				}
				g.drawString(text, 
						(carpetSize.width - stringWidth)/2, 
						carpetSize.height - gap - cardSize.height);

				break;
			case LEFT:
				img = Gui.pictureFactory.getScaledPicture(imgWest, cardSizeInv);
				for(int i = 0; i < nOfCards; i++) {
					g.drawImage(img, 0, 
							(carpetSize.height - (nOfCards + 1)*cardSizeInv.height/2)/2 + i*cardSizeInv.height/2, null);
				}
				g.drawString(text, 
						gap + cardSizeInv.width, 
						(carpetSize.height + stringHeight)/2 + cardSize.height + gap);
			
				break;
			case NOTATTABLE:
				break;
			case TOP:
				img = Gui.pictureFactory.getScaledPicture(imgNorth, cardSize);
				for(int i = 0; i < nOfCards; i++) {
					g.drawImage(img, (carpetSize.width + (nOfCards + 1)*cardSize.width/2)/2 - i*cardSize.width/2 - cardSize.width, 
							0, null);
				}
				g.drawString(text, 
						(carpetSize.width - stringWidth)/2, 
						gap + stringHeight + cardSize.height);
				
				break;
			case RIGHT:
				img = Gui.pictureFactory.getScaledPicture(imgEast, cardSizeInv);
				for(int i = 0; i < nOfCards; i++) {
					g.drawImage(img, carpetSize.width - cardSizeInv.width, 
							(carpetSize.height + (nOfCards + 1)*cardSizeInv.height/2)/2 - i*cardSizeInv.height/2 - cardSizeInv.height, null);
				}
				g.drawString(text, 
						(carpetSize.width - stringWidth - gap - cardSizeInv.width), 
						(carpetSize.height + stringHeight)/2 + cardSize.height + gap);
				
				break;
			default:
				break;

			}
		}	
	}
	
	private String getString(Graphics g, String s, int max) {
		String sub;
		for(int i = 0; i < s.length(); i++) {
			sub = s.substring(0, i);

			if( g.getFontMetrics().stringWidth(sub) > max) {
				return sub + "~";	//String needs to be shorted
			}
		}
		return s;	//String lies within size
	}
}
