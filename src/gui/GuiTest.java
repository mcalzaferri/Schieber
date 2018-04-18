package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import client.AbstractClientView;
import client.ViewEnumeration;

public class GuiTest extends JFrame{
	private AbstractClientView gui;

	public static void main(String[] args) {
		GuiTest t = new GuiTest();
	}
	
	public GuiTest() {
		super();
		this.gui = new Gui(new ClientModelTest());
		this.initFrame();
	}

	private void initFrame() {
		this.setLayout(new FlowLayout());
		
		JComboBox box = new JComboBox(ViewEnumeration.values());
		JLabel label = new JLabel();
		//label.setText(gui.getCurrentView().toString());
		box.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				gui.changeView(ViewEnumeration.valueOf(box.getSelectedItem().toString()));
				//label.setText(gui.getCurrentView().toString());
			}
			
		});
		
		this.add(label);
		this.add(box);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
