package gui;

import java.util.ArrayList;

import javax.swing.JFrame;

import client.ViewObserver;

public class GameOverViewTest {
	public static void main(String[] args)
	{	
		JFrame f = new JFrame();
		GameOverView g = new GameOverView(new ArrayList<ViewObserver>());
		f.add(g.getContent());
		f.pack();
		f.setVisible(true);
	}
}

