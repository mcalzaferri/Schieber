package gui;

import java.util.ArrayList;

import client.ViewEnumeration;
import client.ViewObserver;

public class SelectHostViewTest {
	
	public static void main(String[] args)
	{
		ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();
		SelectHostView s = new SelectHostView(ViewEnumeration.SELECTHOSTVIEW,observers);
		s.setVisible(true);
	}
}
