package gui;

import java.util.ArrayList;

import javax.swing.JFrame;

import client.ViewObserver;

public class WeisViewTest {
		
	public static void main(String[] args)
	{
		ArrayList<ViewObserver> observers = new ArrayList<>();
		ClientModelTest model = new ClientModelTest();
		WeisView w = new WeisView(model, observers);

		JFrame f = new JFrame();
		f.add(w.getContent());
		f.pack();
		f.setVisible(true);
	}
}
