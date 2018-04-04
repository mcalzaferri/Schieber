package client.test;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import shared.Card;
import shared.CardColor;
import shared.CardValue;

public class CardPanel extends JPanel {
	private static final long serialVersionUID = 8621203944155884873L;
	JLabel description;
	JComboBox<CardColor> colorComboBox;
	JComboBox<CardValue> valueComboBox;
	
	public CardPanel(String text) {
		super();
		description = new JLabel(text);
		initialComponents();
	}
	
	private void initialComponents() {
		colorComboBox = new JComboBox<>(CardColor.values());
		valueComboBox = new JComboBox<>(CardValue.values());
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(description);
		this.add(colorComboBox);
		this.add(valueComboBox);
	}
	
	public Card getCard() {
		return new Card((CardColor)colorComboBox.getSelectedItem(), (CardValue)valueComboBox.getSelectedItem());
	}
}
