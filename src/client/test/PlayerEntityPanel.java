package client.test;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.SeatEntity;

public class PlayerEntityPanel extends JPanel {

	private static final long serialVersionUID = 4529969236622381523L;
	private String description;
	private JTextField playerField;
	private JCheckBox isBotBox;
	private IntegerPanel idPanel;
	private JComboBox<SeatEntity> seatBox;
	
	public PlayerEntityPanel(String text) {
		super();
		description = text;
		initialComponents();
	}
	
	private void initialComponents() {
		playerField = new JTextField();
		playerField.setMaximumSize(new Dimension(300, 30));
		seatBox = new JComboBox<>(SeatEntity.values());
		isBotBox = new JCheckBox("isBot");
		seatBox.setMaximumSize(new Dimension(200, 30));
		idPanel = new IntegerPanel("ID:");
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.add(playerField);
		this.add(isBotBox);
		this.add(seatBox);
		this.add(idPanel);
	}
	
	public PlayerEntity getPlayer() {
		PlayerEntity p = new PlayerEntity();
		p.name = playerField.getText();
		p.id = idPanel.getInt();
		p.seat = (SeatEntity)seatBox.getSelectedItem();
		p.isBot = isBotBox.isSelected();
		return p;
	}
}
