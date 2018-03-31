package gui.playingView;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import client.ViewObserver;
import gui.Gui;
import shared.Card;
import shared.CardList;

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
		try {
			this.setIcon(new ImageIcon(Gui.pictureFactory.getPicture(card, this.getPreferredSize())));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(ViewObserver vo : observers) {
					vo.btnCardClick(card);
				}
			}		
		});
	}
	public boolean hasCard() {
		if(this.card == null) {
			return false;
		}
		return true;
	}
}
