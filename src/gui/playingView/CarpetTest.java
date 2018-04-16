package gui.playingView;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

import gui.ClientModelTest;

public class CarpetTest extends JFrame{

	public static void main(String[] args) {
		CarpetTest ct = new CarpetTest();
	}
	
	public CarpetTest() {
		this.setSize(new Dimension(500, 500));
		this.setMinimumSize(new Dimension(500, 500));
		this.add(new CarpetPane(new ClientModelTest(), null));
		this.setVisible(true);
	}
}
