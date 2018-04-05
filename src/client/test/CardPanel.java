package client.test;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
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
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.add(colorComboBox);
		this.add(valueComboBox);
	}
	
	public Card getCard() {
		return new Card((CardColor)colorComboBox.getSelectedItem(), (CardValue)valueComboBox.getSelectedItem());
	}
	
	public void setCard(Card card) {
		colorComboBox.setSelectedItem(card.getColor());
		valueComboBox.setSelectedItem(card.getValue());
	}
}
