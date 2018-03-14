package gui;

import java.awt.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import client.*;

public abstract class View extends JFrame{
	protected ArrayList<ViewObserver> observers;
	
	public View(ArrayList<ViewObserver> observers) {
		super();
		this.observers = observers;
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
}
