package client.test;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import shared.Card;
import shared.CardColor;
import shared.CardValue;

public class CardPanel extends JPanel {
	private static final long serialVersionUID = 8621203944155884873L;
	String description;
	JComboBox<CardColor> colorComboBox;
	JComboBox<CardValue> valueComboBox;
	JLabel nullLabel;
	
	public CardPanel(String text) {
		super();
		description = text;
		initialComponents();
	}
	
	private void initialComponents() {
		colorComboBox = new JComboBox<>(CardColor.values());
		valueComboBox = new JComboBox<>(CardValue.values());
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
	
	public Card getCard() {
		return new Card((CardColor)colorComboBox.getSelectedItem(), (CardValue)valueComboBox.getSelectedItem());
	}
	
	public void setCard(Card card) {
		if(card != null) {
			colorComboBox.setSelectedItem(card.getColor());
			valueComboBox.setSelectedItem(card.getValue());
			colorComboBox.setVisible(true);
			valueComboBox.setVisible(true);
			nullLabel.setVisible(false);
		}else {
			colorComboBox.setVisible(false);
			valueComboBox.setVisible(false);
			nullLabel.setVisible(true);
		}
	}
	
	public void setEditable(boolean value) {
		colorComboBox.setEditable(value);
		valueComboBox.setEditable(value);
	}
	
}
