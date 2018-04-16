package gui;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import client.ViewEnumeration;
import client.ViewObserver;
import shared.Player;

public class LobbyViewTest {
	
	public static void main(String[] args)
	{
		ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();
		ClientModelTest model = new ClientModelTest();
		LobbyView l = new LobbyView(ViewEnumeration.WEISVIEW,observers,model);
		l.setVisible(true);
	}
}
