package gui.animation;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.Timer;

import gui.playingView.CarpetDrawer;
import gui.playingView.CarpetPane;
import gui.playingView.ViewableCard;
import shared.Card;
import shared.RelativeSeat;

/** Region on the gui where animations can be rendered on. 
 * This is a completly invisible component. Only if animations are running, those are painted on top of everything.
 */
public class AnimationRegion extends JComponent {
	private static final long serialVersionUID = -8199381015785514955L;
	//Some constants which represent possible start and end locations of animations.
	public static final int DEALER = 0;
	public static final int BOTTOMPLAYERHAND = 1;
	public static final int RIGHTPLAYERHAND = 2;
	public static final int TOPPLAYERHAND = 3;
	public static final int LEFTPLAYERHAND = 4;
	public static final int DECK = 5;
	public static final int HAND = 6;
	public static final int BOTTOMPLAYERSTACK = 11;
	public static final int RIGHTPLAYERSTACK = 12;
	public static final int TOPPLAYERSTACK = 13;
	public static final int LEFTPLAYERSTACK = 14;
	
	//Vector instead of ArrayList as ArrayList is not threadsafe
	private Vector<Animation> animations;
	private Timer animationTimer;
	
	public AnimationRegion() {
		initializeComponents();
	}
	
	/** Internal method to initialize all components in this class.
	 * 
	 */
	private void initializeComponents() {
		//Set up animations
		initializeAnimations();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		paintAnimation(g);
	}
	
	/** Paints all running animations on top of the Graphics object.
	 * @param g The Graphics object to paint on
	 */
	public void paintAnimation(Graphics g) {
		if(g != null) {
			//Draw animations
			for(Animation animation : animations) {
				double scale = this.getSize().getWidth()/CarpetPane.minCarpetSize.getWidth();
				animation.setScale(scale);
				animation.paintAnimation(g);
			}
		}
	}
	

	/** Internal method to initialize all required fields for animations.
	 * 
	 */
	private void initializeAnimations() {
		animations = new Vector<>();
		AnimationRegion caller = this;
		animationTimer = new Timer(Animation.tickRate, new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!animations.isEmpty()) {
					paintImmediately(0, 0, caller.getWidth(), caller.getHeight());
				}
			}
		});
		animationTimer.start();
	}
	
	/** Starts a new CardAnimation specified by the given parameters
	 * @param card The card which should be displayed
	 * @param duration The duration of this animation in ms
	 * @param source Start position of this animation. See constants of this class for possible values
	 * @param sourcePos Position of the card at the specified start position (Not all positions require this information)
	 * @param sourceCount Total number of cards at the specified start position (Not all positions require this information)
	 * @param destination End position of this animation. See constants of this class for possible values
	 * @param destinationPos Position of the card at the specified end position (Not all positions require this information)
	 * @param destinationCount Total number of cards at the specified end position (Not all positions require this information)
	 * @param listener Optional listener which will be called when an AnimationEvent occurs
	 */
	public void showMoveCardAnimation(Card card, int duration, int source, int sourcePos, int sourceCount, int destination, int destinationPos, int destinationCount , AnimationListener listener) {
		try {
			showAnimation(new MoveCardAnimation(card, duration, 
					getCardAnimationProperty(source, sourcePos, sourceCount),
					getCardAnimationProperty(destination, destinationPos, destinationCount),listener));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Translates the given paramters to a more usefull AnimationProperty
	 * @param location The position of the returned AnimationPropery. See constants of this class for possible values
	 * @param locationPos Position of the card at the specified position. (Not all positions require  this information)
	 * @param cardCount Total number of cards at the specified position. (Not all positions require this information)
	 * @return
	 */
	private AnimationProperty getCardAnimationProperty(int location, int locationPos, int cardCount) {
		AnimationProperty ap = null;
		RelativeSeat seat;
		switch(location) {
		case DEALER:
			ap = new AnimationProperty(new Point(-100,-100), 
					CarpetPane.minCardSize, 0);
			break;
		case DECK:
			ap = new AnimationProperty(CarpetDrawer.getDeckCardLocation(
					RelativeSeat.getById(locationPos),CarpetPane.minCarpetSize, CarpetPane.minCardSize), 
					CarpetPane.minCardSize, 0);	
			break;
		case HAND:
			//Now the hard part. Calculate exactly the same position as the card in the players hand
			//All cards are not overlapping
			if(ViewableCard.minCardSize.width*cardCount <= CarpetPane.minCarpetSize.width) {
				ap = new AnimationProperty(new Point(
						(CarpetPane.minCarpetSize.width - ViewableCard.minCardSize.width*cardCount)/2 + ViewableCard.minCardSize.width*locationPos,
						CarpetPane.minCarpetSize.height), 
						ViewableCard.minCardSize, 
						0);
			}
			//Horizontally overlap components
			else {
				ap = new AnimationProperty(new Point(
						((CarpetPane.minCarpetSize.width - ViewableCard.minCardSize.width)/(cardCount-1))*locationPos,
						CarpetPane.minCarpetSize.height),
						ViewableCard.minCardSize, 
						0);
			}
			break;
		case BOTTOMPLAYERHAND:
		case LEFTPLAYERHAND:
		case RIGHTPLAYERHAND:
		case TOPPLAYERHAND:
			seat = RelativeSeat.getById(location);
			ap = new AnimationProperty(CarpetDrawer.getPlayerCardLocation(seat, Math.max(cardCount,locationPos), locationPos, CarpetPane.minCarpetSize, CarpetPane.minCoverSize), //Point
					CarpetPane.minCoverSize, //Dimension
					(location -1)*-90);		//Rotation
			break;
		case BOTTOMPLAYERSTACK:
		case LEFTPLAYERSTACK:
		case RIGHTPLAYERSTACK:
		case TOPPLAYERSTACK:
			seat = RelativeSeat.getById(location - 10);
			ap = new AnimationProperty(CarpetDrawer.getPlayerCardStackLocation(seat, Math.max(cardCount,locationPos), locationPos, CarpetPane.minCarpetSize, CarpetPane.minCoverSize), //Point
					CarpetPane.minCoverSize, //Dimension
					(location -10)*-90);		//Rotation
		}
		return ap;
	}
	
	/** Adds the given animation to the list of running animations.
	 * @param animation The animation which should be rendered.
	 */
	public void showAnimation(Animation animation) {
		animations.add(animation);
	}
	
	/** Checks if one or more animations are running.
	 * @return True if at least one animation has not yet finished. False otherwise.
	 */
	public boolean animationIsRunning() {
		for(Animation a : animations) {
			if(!a.hasFinished()) {
				return true;
			}
		}
		return false;
	}
	
	/** Removes all animations which are in the finished state.
	 * 
	 */
	public void removeFinishedAnimations() {
		if(!animations.isEmpty()) {
			Collection<Animation> finishedAnimations = new ArrayList<>();
			for(Animation animation : animations) {
				if(animation.hasFinished()) {
					finishedAnimations.add(animation);
				}
			}
			animations.removeAll(finishedAnimations);
		}
	}
}
