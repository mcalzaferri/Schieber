package gui;

import java.util.ArrayList;

import client.ViewEnumeration;
import client.ViewObserver;

public class GameOverViewTest {
	public static void main(String[] args)
	{
		ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();
		GameOverView g = new GameOverView(ViewEnumeration.SELECTTRUMPVIEW,observers);
		g.setVisible(true);
	}
}

