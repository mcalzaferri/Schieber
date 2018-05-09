package gui.animation;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.Timer;

import gui.Gui;
import gui.playingView.CarpetPane;
import shared.Trump;
import test.TestHelper;

public class AnimationRegion extends JComponent {
	private static final long serialVersionUID = -8199381015785514955L;
	private ArrayList<Animation> animations;
	private Timer animationTimer;
	
	public AnimationRegion() {
		initializeComponents();
	}
	
	private void initializeComponents() {
		//Set up animations
		initializeAnimations();
	}
	
	@Override
	public void paint(Graphics g) {
		if(g != null) {
			super.paintComponent(g);
			TestHelper.registerThread();
			TestHelper.printAndResetElapsedTime("Start");
			//Draw animations
			for(Animation animation : animations) {
				double scale = this.getSize().getWidth()/CarpetPane.minCarpetSize.getWidth();
				animation.setScale(scale);
				animation.doPaint(g);
			}
			TestHelper.printAndResetElapsedTime();
		}
	}
	

	private void initializeAnimations() {
		animations = new ArrayList<>();
		AnimationRegion caller = this;
		animationTimer = new Timer(Animation.tickRate, new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!animations.isEmpty()) {
					caller.repaint();
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
		try {
			showAnimation(new MovePictureAnimation(Gui.pictureFactory.getPicture(Trump.EICHEL), 50000, new AnimationProperty(0, 0, 50, 50, 0), new AnimationProperty(500, 700, 200, 200, 180)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void showAnimation(Animation animation) {
		animations.add(animation);
	}
	
	public boolean animationisRunning() {
		return !animations.isEmpty();
	}
}
