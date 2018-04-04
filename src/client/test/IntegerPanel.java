package client.test;

import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

public class IntegerPanel extends JPanel{
	private static final long serialVersionUID = 5969789881559871157L;
	private JLabel description;
	private JFormattedTextField integerField;
	
	public IntegerPanel(String text) {
		super();
		description = new JLabel(text);
		initialComponents();
	}
	
	private void initialComponents() {
		NumberFormatter formatter = new NumberFormatter();
		formatter.setValueClass(Integer.class);
		integerField = new JFormattedTextField(formatter);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(description);
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
