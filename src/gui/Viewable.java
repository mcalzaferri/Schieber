package gui;

import javax.swing.JPanel;

import client.ViewEnumeration;

public interface Viewable {
	public void update();
	public JPanel getContent();
	public ViewEnumeration getType();
}
