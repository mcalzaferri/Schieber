package client.test;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.ntb.jass.common.entities.*;


public class CardPanel extends JPanel {
	private static final long serialVersionUID = 8621203944155884873L;
	String description;
	JComboBox<CardColorEntity> colorComboBox;
	JComboBox<CardValueEntity> valueComboBox;
	JLabel nullLabel;
	
	public CardPanel(String text) {
		super();
		description = text;
		initialComponents();
	}
	
	private void initialComponents() {
		colorComboBox = new JComboBox<>(CardColorEntity.values());
		valueComboBox = new JComboBox<>(CardValueEntity.values());
		colorComboBox.setMaximumSize(new Dimension(200, 30));
		valueComboBox.setMaximumSize(new Dimension(200, 30));
		nullLabel = new JLabel("            null            ");
		nullLabel.setVisible(false);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.add(colorComboBox);
		this.add(valueComboBox);
		this.add(nullLabel);
	}
	
	public CardEntity getCard() {
		CardEntity card = new CardEntity();
		card.color = (CardColorEntity)colorComboBox.getSelectedItem();
		card.value = (CardValueEntity)valueComboBox.getSelectedItem();
		return card;
	}
	
	public void setCard(CardEntity card) {
		if(card != null) {
			colorComboBox.setSelectedItem(card.color);
			valueComboBox.setSelectedItem(card.value);
			colorComboBox.setVisible(true);
			valueComboBox.setVisible(true);
			nullLabel.setVisible(false);
		}else {
			colorComboBox.setVisible(false);
			valueComboBox.setVisible(false);
			nullLabel.setVisible(true);
		}
	}
	
	@Override
	public void setEnabled(boolean value) {
		colorComboBox.setEnabled(value);
		valueComboBox.setEnabled(value);
	}
	
}
