package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import client.AbstractClientView;
import client.ViewEnumeration;
import gui.Gui.MessageType;
import shared.CardList;

public class GuiTest extends JFrame{
	private AbstractClientView gui;
	private ClientModelTest data;

	public static void main(String[] args) {
		GuiTest t = new GuiTest();
	}
	
	public GuiTest() {
		super();
		data = new ClientModelTest();
		gui = new Gui(data);
		this.initFrame();
	}

	private void initFrame() {
		this.setLayout(new FlowLayout());
		
		JComboBox box = new JComboBox(ViewEnumeration.values());

		box.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				gui.changeView(ViewEnumeration.valueOf(box.getSelectedItem().toString()));
				gui.showDialog("View changed", MessageType.INFORMATION);
				
				CardList list = new CardList();
				for(int i = 0; i < 12; i++) {
					list.add(ClientModelTest.createRandomCard());
				}
				data.setHand(list);
				gui.updateView(ViewEnumeration.PLAYVIEW);
			}
			
		});
		
		gui.showDialog("View changed", MessageType.INFORMATION);

		this.add(box);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
