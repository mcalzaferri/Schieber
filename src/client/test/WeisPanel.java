package client.test;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import shared.Weis;
import shared.WeisType;

public class WeisPanel extends JPanel {
	private static final long serialVersionUID = 8621203944155884873L;
	String description;
	JCheckBox enableBox;
	JComboBox<WeisType> weisTypeComboBox;
	CardPanel cardPanel;
	JLabel nullLabel;
	
	public WeisPanel(String text) {
		super();
		description = text;
		initialComponents();
	}
	
	private void initialComponents() {
		weisTypeComboBox = new JComboBox<>(WeisType.values());
		enableBox = new JCheckBox("enableWeis");
		cardPanel = new CardPanel("originCard");
		nullLabel = new JLabel("            null            ");
		nullLabel.setVisible(false);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.add(enableBox);
		this.add(weisTypeComboBox);
		this.add(cardPanel);
		this.add(nullLabel);
		
	}
	
	public Weis getWeis() {
		if(enableBox.isSelected())
			return new Weis((WeisType)weisTypeComboBox.getSelectedItem(), cardPanel.getCard());
		else
			return null;
	}
	
	public void setWeis(Weis weis) {
		if(weis != null) {
			nullLabel.setVisible(false);
			weisTypeComboBox.setVisible(true);
			cardPanel.setVisible(true);
			if(weisTypeComboBox.isEnabled())
				enableBox.setVisible(true);
			if(weis.getType() != null) {
				weisTypeComboBox.setSelectedItem(weis.getType());
			}
			if(weis.getOriginCard() != null) {
				cardPanel.setCard(weis.getOriginCard());
			}
			
		}else {
			nullLabel.setVisible(true);
			weisTypeComboBox.setVisible(false);
			cardPanel.setVisible(false);
			enableBox.setVisible(false);
		}
	}
	
	@Override
	public void setEnabled(boolean value) {
		enableBox.setVisible(value);
		weisTypeComboBox.setEnabled(value);
		cardPanel.setEnabled(value);
	}
	
}
