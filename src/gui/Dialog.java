package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class Dialog extends JDialog{
	JButton confirm;
	JLabel message;
	JLabel icon;
	/**This enumeration resembles the values to configure a JOptionPane
	 * 
	 * @author mstieger
	 *
	 */
	public enum MessageType{
		/*
		 * Index values of elements must agree with the values for message types
		 * of JOptionPane
		 */
		ERROR(0), 
		INFORMATION(1), 
		WARNING(2), 
		QUESTION(3), 
		PLAIN(-1);
		
		private final int index;
		
		MessageType(int index){
			this.index = index;
		}
		
	};
	
	public Dialog(Window owner, ModalityType modalityType) {
		super(owner, "", modalityType);
		initializeComponent();
	}
	
	private void initializeComponent() {
		//Initialize components
		icon = new JLabel();
		message = new JLabel();
		confirm = new JButton("OK");
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
			
		});

		//Layout components
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		icon.setAlignmentX(CENTER_ALIGNMENT);
		content.add(icon);
		message.setAlignmentX(CENTER_ALIGNMENT);
		content.add(message);
		confirm.setAlignmentX(CENTER_ALIGNMENT);
		content.add(confirm);
	}
	
	public void showDialog(String message, MessageType type) {
		//Set message and icon
		setTitle(type.toString());
		this.message.setText(message);
		Icon i = javax.swing.UIManager.getIcon("OptionPane." + type.toString().toLowerCase() + "Icon");
		icon.setIcon(i);
		
		//Set bounds
		pack();
		setLocationRelativeTo(getOwner());	//Place in the middle of the owner window
		
		setVisible(true);
	}
}
