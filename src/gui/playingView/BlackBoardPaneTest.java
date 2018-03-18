package gui.playingView;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import javax.swing.JFrame;

import client.ViewObserver;
import shared.Card;
import shared.CardList;

public class BlackBoardPaneTest extends JFrame{
	private BlackBoardPane pane;
	public static void main(String[] args) {
		BlackBoardPaneTest t = new BlackBoardPaneTest();
		String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
	            .getAvailableFontFamilyNames();

	      for (String fontName : fontNames)
	         System.out.println(fontName);
	}
	
	public BlackBoardPaneTest() {
		this.pane = new BlackBoardPane(null, null);
		this.add(pane);
		this.setVisible(true);
		this.setSize(500, 500);
	}
}
