package client.test;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.ntb.jass.common.entities.SeatEntity;
import shared.Player;

public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 5790949079190636182L;
	private String description;
	private JLabel playerLabel;
	private JComboBox<SeatEntity> seatBox;
	private JCheckBox isBotBox;
	private JLabel nullLabel;
	private JCheckBox isReadyBox;
	private Player player;
	
	public PlayerPanel(String text) {
		super();
		description = text;
		initialComponents();
	}
	
	private void initialComponents() {
		playerLabel = new JLabel("Undefined");
		seatBox = new JComboBox<>(SeatEntity.values());
		seatBox.setEnabled(false);
		isBotBox = new JCheckBox("isBot");
		isBotBox.setEnabled(false);
		nullLabel = new JLabel("            null            ");
		isReadyBox = new JCheckBox("isReady");
		isReadyBox.setEnabled(false);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.add(playerLabel);
		this.add(isBotBox);
		this.add(seatBox);
		this.add(isReadyBox);
		this.add(nullLabel);
		playerLabel.setVisible(false);
		seatBox.setVisible(false);
		nullLabel.setVisible(true);
		isReadyBox.setVisible(false);
		isBotBox.setVisible(false);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		if(player != null) {
			playerLabel.setText(player.getName() + " ID: " + player.getId());
			playerLabel.setVisible(true);
			isBotBox.setSelected(player.isBot());
			isBotBox.setVisible(true);
			isReadyBox.setSelected(player.isReady());
			isReadyBox.setVisible(true);
			seatBox.setSelectedItem(player.getSeat().getSeatEntity());
			seatBox.setVisible(true);
			isBotBox.setVisible(true);
			nullLabel.setVisible(false);
		}else {
			playerLabel.setVisible(false);
			isBotBox.setVisible(false);
			isReadyBox.setVisible(false);
			seatBox.setVisible(false);
			nullLabel.setVisible(true);
		}
	}
}
