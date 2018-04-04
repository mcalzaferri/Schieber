package gui.playingView;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import client.ViewObserver;
import gui.Gui;
import gui.PictureFactory.Pictures;
import shared.client.ClientModel;

public class BlackBoardPane extends JPanel{
	private ClientModel data;
	private ArrayList<ViewObserver> observers;
	private BlackBoardDrawer drawer;
	public final Font font = new Font("MV Boli" ,Font.PLAIN, 28);
	public final Dimension minSize = new Dimension(350, 500);
	public final Rectangle minInnerBounds = new Rectangle(50, 50, minSize.width - 100, minSize.height -100);
	
	public BlackBoardPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super();
		this.data = data;
		this.observers = observers;
		this.drawer = new BlackBoardDrawer();
		this.setPreferredSize(minSize);
		this.setMinimumSize(this.getPreferredSize());
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
			Image img = Gui.pictureFactory.getPicture(Pictures.BlackBoard, this.getSize());
			g.drawImage(img, 0, 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double scale = this.getSize().getHeight()/minSize.getHeight();
		this.drawer.setScale(scale);
		this.drawer.setFont(this.font);
		this.drawer.setBounds(minInnerBounds);
		this.drawer.drawText(g, new String[]{"Schieber", "Score Team 1", "Score Team 2"});
		this.drawer.drawTrump(g, data.getTrump());
	}
}
