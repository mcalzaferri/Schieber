package gui;

import java.util.ArrayList;

import javax.swing.JFrame;

import client.ViewEnumeration;
import client.ViewObserver;

public class SelectHostViewTest {
	
	public static void main(String[] args)
	{
		ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();
		SelectHostView s = new SelectHostView(observers);

		JFrame f = new JFrame();
		f.add(s.getContent());
		f.pack();
		f.setVisible(true);
	}
}
