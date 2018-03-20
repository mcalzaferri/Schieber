package gui.playingView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import gui.Gui;
import gui.PictureFactory.Pictures;
import shared.Card;
import shared.CardList;
import shared.client.ClientModel;

public class BlackBoardPane extends JPanel{
	private ClientModel data;
	private ArrayList<ViewObserver> observers;
	private Font font;
	
	public BlackBoardPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super();
		this.data = data;
		this.observers = observers;
		this.font = new Font("MV Boli" ,Font.PLAIN,36);
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(150, 100));
		this.update();
	}
	
	public void update() {
		//TODO
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		try {
			BufferedImage img = Gui.pictureFactory.getPicture(Pictures.BlackBoard, this.getPreferredSize());
			g.drawImage(img, 0, 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("Test", 30, 30);

	}
}
