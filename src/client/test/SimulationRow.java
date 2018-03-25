package client.test;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class SimulationRow extends JPanel{
	private static final long serialVersionUID = 2520228745606367089L;
	private SimulationRowListener listener;
	public JTextField dataField;
	public JButton button;
	
	public SimulationRow(String buttonName) {
		super(new FlowLayout());
		dataField = new JTextField();
		dataField.setPreferredSize(new Dimension(200, 30));
		button = new JButton(buttonName);
		this.add(dataField);
		this.add(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listener != null) {
					listener.actionPerformed(dataField.getText());
				}
			}
		});
	}
	
	public void setSimulationRowListener(SimulationRowListener listener) {
		this.listener = listener;
	}
}
