package gui;

import java.util.ArrayList;

import javax.swing.JFrame;

import client.ViewEnumeration;
import client.ViewObserver;

public class TrumpViewTest {
	
	public static void main(String[] args)
	{
		ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();
		TrumpView t = new TrumpView(observers);
		
		JFrame f = new JFrame();
		f.add(t.getContent());
		f.pack();
		f.setVisible(true);
	}
}

