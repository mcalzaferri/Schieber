package gui.playingView;

import javax.swing.JFrame;

public class MessageBoardTest {
	public static void main(String[] args) {
		MessageBoard mb = new MessageBoard();
		for(int i = 0; i < 100; i++) {
			mb.publisch(20, "This is test number " + i);
		}
		JFrame f = new JFrame();
		f.add(mb);
		f.setSize(300, 300);
		f.setVisible(true);
	}
}
