package gui.playingView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import gui.ClientModelTest;

public class ViewableCardTest extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2468225525225384524L;

	public static void main(String[] args) {
		new ViewableCardTest();
	}
	
	public ViewableCardTest() {
		//Set up card
		ViewableCard vc = new ViewableCard(ClientModelTest.createRandomCard());
		vc.addActionListener(this);
		//Set up frame
		setLayout(new BorderLayout());
		add(vc, BorderLayout.CENTER);
		setVisible(true);
		setSize(vc.getMinimumSize());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Card clicked");
		
	}
}
