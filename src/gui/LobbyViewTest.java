package gui;

import java.util.ArrayList;
import javax.swing.JFrame;

import client.ViewObserver;

public class LobbyViewTest {
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		ArrayList<ViewObserver> observers = new ArrayList<>();
		ClientModelTest model = new ClientModelTest();
		LobbyView l = new LobbyView(observers, model);

		f.add(l.getContent());
		f.pack();
		f.setVisible(true);
	}
}
