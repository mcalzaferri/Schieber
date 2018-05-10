package gui.animation;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.Timer;

import gui.playingView.CarpetDrawer;
import gui.playingView.CarpetPane;
import gui.playingView.ViewableCard;
import shared.Card;
import shared.RelativeSeat;
import shared.client.ClientModel;

public class AnimationRegion extends JComponent {
	private static final long serialVersionUID = -8199381015785514955L;
	public static final int DEALER = 0;
	public static final int BOTTOMPLAYER = 1;
	public static final int RIGHTPLAYER = 2;
	public static final int TOPPLAYER = 3;
	public static final int LEFTPLAYER = 4;
	public static final int DECK = 5;
	public static final int HAND = 6;
	@SuppressWarnings("unused")
	private ClientModel model;
	
	private ArrayList<Animation> animations;
	private Timer animationTimer;
	
	public AnimationRegion(ClientModel model) {
		this.model = model;
		initializeComponents();
	}
	
	private void initializeComponents() {
		//Set up animations
		initializeAnimations();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		paintAnimation(g);
	}
	
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
	

	private void initializeAnimations() {
		animations = new ArrayList<>();
		AnimationRegion caller = this;
		animationTimer = new Timer(Animation.tickRate, new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!animations.isEmpty()) {
					paintImmediately(0, 0, caller.getWidth(), caller.getHeight());
					Collection<Animation> finishedAnimations = new ArrayList<>();
					for(Animation animation : animations) {
						if(animation.hasFinished()) {
							finishedAnimations.add(animation);
						}
					}
					animations.removeAll(finishedAnimations);
				}
			}
		});
		animationTimer.start();
	}
	
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

	private AnimationProperty getCardAnimationProperty(int location, int locationPos, int cardCount) {
		AnimationProperty ap = null;
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
		case BOTTOMPLAYER:
		case LEFTPLAYER:
		case RIGHTPLAYER:
		case TOPPLAYER:
			RelativeSeat seat = RelativeSeat.getById(location);
			ap = new AnimationProperty(CarpetDrawer.getPlayerCardLocation(seat, Math.max(cardCount,locationPos), locationPos, CarpetPane.minCarpetSize, CarpetPane.minCoverSize), //Point
					CarpetPane.minCoverSize, //Dimension
					(location -1)*-90);		//Rotation
			break;
		}
		return ap;
	}
	
	public void showAnimation(Animation animation) {
		animations.add(animation);
	}
	
	public boolean animationisRunning() {
		return !animations.isEmpty();
	}
}
