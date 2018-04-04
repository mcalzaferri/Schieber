package gui.playingView;

import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

import shared.Trump;
import shared.client.ClientModel;

public class BlackBoardPaneTest extends JFrame{
	private BlackBoardPane pane;
	public static void main(String[] args) {
		BlackBoardPaneTest t = new BlackBoardPaneTest();
		String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
	            .getAvailableFontFamilyNames();

	      for (String fontName : fontNames)
	         System.out.println(fontName);
	}
	
	public BlackBoardPaneTest() {
		ClientModel cm = new ClientModel();
		cm.setTrump(Trump.EICHEL);
		this.pane = new BlackBoardPane(cm, null);
		this.add(pane);
		this.setVisible(true);
		this.setSize(500, 500);
	}
}
