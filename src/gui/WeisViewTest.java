package gui;

import java.util.ArrayList;

import client.ViewObserver;
import client.ViewEnumeration;

public class WeisViewTest {
		
	public static void main(String[] args)
	{
		ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();
		ClientModelTest model = new ClientModelTest();
		WeisView w = new WeisView(ViewEnumeration.WEISVIEW,observers,model);
		w.setVisible(true);
	}
}
