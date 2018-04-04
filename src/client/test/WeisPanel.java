package client.test;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import shared.Weis;
import shared.WeisType;

public class WeisPanel extends JPanel {
	private static final long serialVersionUID = 8621203944155884873L;
	JLabel description;
	JCheckBox enableBox;
	JComboBox<WeisType> weisTypeComboBox;
	CardPanel cardPanel;
	
	public WeisPanel(String text) {
		super();
		description = new JLabel(text);
		initialComponents();
	}
	
	private void initialComponents() {
		weisTypeComboBox = new JComboBox<>(WeisType.values());
		enableBox = new JCheckBox("enableWeis");
		cardPanel = new CardPanel("originCard");
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(description);
		this.add(enableBox);
		this.add(weisTypeComboBox);
		this.add(cardPanel);
	}
	
	public Weis getWeis() {
		if(enableBox.isSelected())
			return new Weis((WeisType)weisTypeComboBox.getSelectedItem(), cardPanel.getCard());
		else
			return null;
	}
}
