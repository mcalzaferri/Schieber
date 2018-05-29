package gui.playingView;

import java.awt.Dimension;

import javax.swing.JFrame;
import gui.ClientModelTest;

public class CarpetTest extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3610469874530936829L;

	public static void main(String[] args) {
		new CarpetTest();
	}
	
	public CarpetTest() {
		this.setSize(new Dimension(500, 500));
		this.setMinimumSize(new Dimension(500, 500));
		this.add(new CarpetPane(new ClientModelTest(), null));
		this.setVisible(true);
	}
}
