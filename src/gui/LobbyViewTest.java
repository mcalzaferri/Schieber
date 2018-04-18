package gui;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFrame;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.Player;

public class LobbyViewTest {
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();
		ClientModelTest model = new ClientModelTest();
		LobbyView l = new LobbyView(model, observers);

		f.add(l.getContent());
		f.pack();
		f.setVisible(true);
	}
}
