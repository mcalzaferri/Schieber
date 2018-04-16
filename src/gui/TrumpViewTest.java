package gui;

import java.util.ArrayList;


import client.ViewEnumeration;
import client.ViewObserver;

public class TrumpViewTest {
	
	public static void main(String[] args)
	{
		ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();
		TrumpView t = new TrumpView(ViewEnumeration.SELECTTRUMPVIEW,observers);
		t.setVisible(true);
	}
}

