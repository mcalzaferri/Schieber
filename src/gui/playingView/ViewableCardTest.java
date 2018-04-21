package gui.playingView;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import gui.ClientModelTest;

public class ViewableCardTest extends JFrame implements ActionListener{
	public static void main(String[] args) {
		ViewableCardTest t = new ViewableCardTest();
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
