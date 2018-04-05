package client.test;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

public class IntegerPanel extends JPanel{
	private static final long serialVersionUID = 5969789881559871157L;
	private String description;
	private JLabel descriptionLabel;
	private JFormattedTextField integerField;
	
	public IntegerPanel(String text) {
		super();
		
		description = text;
		initialComponents();
	}
	
	private void initialComponents() {
		NumberFormatter formatter = new NumberFormatter();
		formatter.setValueClass(Integer.class);
		integerField = new JFormattedTextField(formatter);
		integerField.setMaximumSize(new Dimension(200, 30));
		descriptionLabel = new JLabel(description);
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(descriptionLabel);
		this.add(integerField);
	}
	
	public int getInt() {
		try {
			return Integer.parseInt(integerField.getText());
		}catch(Exception e) {
			System.err.println("Failed to parse content to Integer");
		}
		return 0;
	}
}
