package gui.playingView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import client.shared.ClientModel;
import gui.Gui;
import gui.PictureFactory.Pictures;
import shared.Card;
import shared.CardList;
import shared.Player;
import shared.RelativeSeat;

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
	
	/**
	 * Draws the current deck in the middle of the carpet.
	 * 
	 * @param g Components graphics object
	 * @param deck Deck to be displayed
	 * @param carpetSize Size of the component representing the carpet
	 * @param cardSize Size for the decks cards
	 */
	public void drawDeck(Graphics g, ClientModel model, Dimension carpetSize, Dimension cardSize) {
		CardList deck = model.getDeck();
		Point cardLocation;
		Image img;
		if(deck != null) {
			for(Card c : deck) {
				if(c != null) {
					try {
						img = Gui.pictureFactory.getPicture(c, cardSize);
						cardLocation = getDeckCardLocation(model.getDeckCardOrientation(c), carpetSize, cardSize);
						g.drawImage(img, cardLocation.x, cardLocation.y, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	public static Point getDeckCardLocation(RelativeSeat seat, Dimension carpetSize, Dimension cardSize) {
		Point p = new Point();
		int xOffset = -cardSize.width/2;
		int yOffset = -cardSize.height/2;
		switch(seat) {
		case BOTTOM:
			p.x = carpetSize.getSize().width/2 + xOffset;
			p.y = carpetSize.getSize().height*2/3 + yOffset;
			break;
		case RIGHT:
			p.x = carpetSize.getSize().width*2/3 + xOffset;
			p.y = carpetSize.getSize().height/2 + yOffset;
			break;
		case TOP:
			p.x = carpetSize.getSize().width/2 + xOffset;
			p.y = carpetSize.getSize().height*1/3 + yOffset;
			break;
		case LEFT:
			p.x = carpetSize.getSize().width*1/3 + xOffset;
			p.y = carpetSize.getSize().height/2 + yOffset;
			break;
		default:
			System.err.println("Error in CarpetDrawer. Unknown Orientation of Card!");
			break;
		}
		return p;
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
			text = getString(g, text, carpetSize.width/3);	//Reduce text to given length that it fits in graphics
			
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
			Image img;
			Image invImg;
			switch(seat) {
			case BOTTOM:
				img = Gui.pictureFactory.getScaledPicture(imgSouth, cardSize);
				invImg = Gui.pictureFactory.getScaledPicture(imgEast, cardSizeInv);
				g.drawString(text, 
						(carpetSize.width - stringWidth)/2, 
						carpetSize.height - gap - cardSize.height);

				break;
			case LEFT:
				img = Gui.pictureFactory.getScaledPicture(imgWest, cardSizeInv);
				invImg = Gui.pictureFactory.getScaledPicture(imgSouth, cardSize);
				g.drawString(text, 
						gap + cardSizeInv.width, 
						(carpetSize.height + stringHeight)/2 + cardSize.height + gap);
			
				break;
			case NOTATTABLE:
				return;
			case TOP:
				img = Gui.pictureFactory.getScaledPicture(imgNorth, cardSize);
				invImg = Gui.pictureFactory.getScaledPicture(imgWest, cardSizeInv);
				g.drawString(text, 
						(carpetSize.width - stringWidth)/2, 
						gap + stringHeight + cardSize.height);
				
				break;
			case RIGHT:
				img = Gui.pictureFactory.getScaledPicture(imgEast, cardSizeInv);
				invImg = Gui.pictureFactory.getScaledPicture(imgNorth, cardSize);
				g.drawString(text, 
						(carpetSize.width - stringWidth - gap - cardSizeInv.width), 
						(carpetSize.height + stringHeight)/2 + cardSize.height + gap);
				
				break;
			default:
				return;

			}
			Point cardP;
			//Now draw cards
			for(int i = 0; i < nOfCards; i++) {
				cardP = getPlayerCardLocation(seat, nOfCards, i, carpetSize, cardSize);
				if(player.getCards().get(i).isUnknown()) {
					g.drawImage(img, cardP.x, cardP.y, null);
				}
			}
			
			//Now draw the players card stack (The stichs he won)
			nOfCards = player.getCardsOnStack().size();
			for(int i = 0; i < nOfCards; i++) {
				cardP = getPlayerCardStackLocation(seat, nOfCards, i, carpetSize, cardSize);
				g.drawImage(invImg, cardP.x, cardP.y, null);
			}
		}	
	}
	
	public static Point getPlayerCardLocation(RelativeSeat seat, int nOfCards, int cardNr, Dimension carpetSize, Dimension cardSize) {
		Point p = new Point();
		Dimension cardSizeInv = new Dimension(cardSize.height, cardSize.width); //For images on the left/right
		switch(seat) {
		case BOTTOM:
			p.setLocation((carpetSize.width + (nOfCards + 1)*cardSize.width/2)/2 - cardNr*cardSize.width/2 - cardSize.width, carpetSize.height - cardSize.height);
			break;
		case LEFT:
			p.setLocation(0, (carpetSize.height - (nOfCards + 1)*cardSizeInv.height/2)/2 + cardNr*cardSizeInv.height/2);
			break;
		case NOTATTABLE:
			break;
		case TOP:
			p.setLocation((carpetSize.width + (nOfCards + 1)*cardSize.width/2)/2 - cardNr*cardSize.width/2 - cardSize.width, 0);
			break;
		case RIGHT:
			p.setLocation(carpetSize.width - cardSizeInv.width, (carpetSize.height + (nOfCards + 1)*cardSizeInv.height/2)/2 - cardNr*cardSizeInv.height/2 - cardSizeInv.height);
			break;
		default:
			break;
		}
		return p;
	}
	
	public static Point getPlayerCardStackLocation(RelativeSeat seat, int nOfCards, int cardNr, Dimension carpetSize, Dimension cardSize) {
		Point p = new Point();
		int gap = cardSize.width / 4;
		switch(seat) {
		case BOTTOM:
			p.setLocation((carpetSize.width*4)/6, -gap + carpetSize.height - cardSize.width*2 + (cardNr*cardSize.width)/(Math.max(nOfCards - 1,1)));
			break;
		case LEFT:
			p.setLocation(gap + (cardSize.width) - (cardNr*cardSize.width)/(Math.max(nOfCards - 1,1)),(carpetSize.height*4)/6);
			break;
		case NOTATTABLE:
			break;
		case TOP:
			p.setLocation((carpetSize.width*2)/6 - cardSize.height,gap + (cardSize.width) - (cardNr*cardSize.width)/(Math.max(nOfCards - 1,1)));
			break;
		case RIGHT:
			p.setLocation(-gap + (carpetSize.width - cardSize.width*2) + (cardNr*cardSize.width)/(Math.max(nOfCards - 1,1)),(carpetSize.height*2)/6 - cardSize.height);
			break;
		default:
			break;
		}
		return p;
	}
	
	private String getString(Graphics g, String s, int max) {
		String sub;
		for(int i = 0; i < s.length(); i++) {
			sub = s.substring(0, i);

			if( g.getFontMetrics().stringWidth(sub) > max) {
				//String needs to be shorted
				if(sub.length() > 1) {
					return sub.substring(0, sub.length()-2) + "~";
				}
				return sub + "~";	
			}
		}
		return s;	//String lies within size
	}
}
