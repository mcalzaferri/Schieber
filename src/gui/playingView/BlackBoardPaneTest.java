package gui.playingView;

import javax.swing.JFrame;

import client.shared.ClientModel;
import shared.Trump;

public class BlackBoardPaneTest extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9162373993474248155L;
	private BlackBoardPane pane;
	public static void main(String[] args) {
		new BlackBoardPaneTest();
	}
	
	public BlackBoardPaneTest() {
		ClientModel cm = new ClientModel();
		cm.setTrump(Trump.EICHEL);
		this.pane = new BlackBoardPane(cm, null);
		this.add(pane);
		this.setVisible(true);
		this.setSize(500, 500);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
