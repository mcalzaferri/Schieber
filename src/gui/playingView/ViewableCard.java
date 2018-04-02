package gui.playingView;

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
	public final Dimension minCardSize = new Dimension(100, 160);
	
	public ViewableCard(Card card, ArrayList<ViewObserver> observers) {
		super();
		this.card = card;	
		this.observers = observers;		
		this.initButton();
	}

	private void initButton() {
		this.setPreferredSize(minCardSize);
		this.setMinimumSize(minCardSize);
		
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
		super.paint(g);
		try {
			BufferedImage img = Gui.pictureFactory.getPicture(card, this.getSize());
			g.drawImage(img, 0, 0, null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public boolean hasCard() {
		if(this.card == null) {
			return false;
		}
		return true;
	}
}
