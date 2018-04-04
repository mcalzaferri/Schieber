package gui.playingView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import client.ViewObserver;
import gui.Gui;
import shared.Card;

public class ViewableCard extends JButton{
	private Card card;
	private ArrayList<ViewObserver> observers;
	private BufferedImage img;
	public final Dimension minCardSize = new Dimension(100, 160);
	
	public ViewableCard(Card card, ArrayList<ViewObserver> observers) {
		super();
		this.card = card;	
		this.observers = observers;
		this.img = null;
		try {
			this.img = Gui.pictureFactory.getPicture(card);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.initButton();
	}

	private void initButton() {
		this.setMinimumSize(minCardSize);
		this.setPreferredSize(minCardSize);
		
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(ViewObserver vo : observers) {
					vo.btnCardClick(card);
				}
			}		
		});
	}
	@Override
	public void paint(Graphics g) {
		try {
			g.drawImage(Gui.pictureFactory.getScaledPicture(img, this.getSize()), 0, 0, null);
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
