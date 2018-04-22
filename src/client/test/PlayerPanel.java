package client.test;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.ntb.jass.common.entities.PlayerEntity;

public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 4529969236622381523L;
	String description;
	JComboBox<PlayerEntity> playerBox;
	JCheckBox isBotBox;
	JLabel nullLabel;
	PlayerEntity[] players;
	
	public PlayerPanel(String text, PlayerEntity[] players) {
		super();
		description = text;
		this.players = players;
		initialComponents();
	}
	
	private void initialComponents() {
		playerBox = new JComboBox<>(players);
		isBotBox = new JCheckBox("isBot");
		nullLabel = new JLabel("            null            ");
		playerBox.setMaximumSize(new Dimension(200, 30));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.add(playerBox);
		this.add(isBotBox);
		this.add(nullLabel);
		nullLabel.setVisible(false);
	}
	
	public PlayerEntity getPlayer() {
		PlayerEntity p = (PlayerEntity)playerBox.getSelectedItem();
		p.isBot = isBotBox.isSelected();
		return p;
	}
	
	public void setPlayer(PlayerEntity player) {
		if(player != null) {
			playerBox.setBackground(Color.WHITE);
			playerBox.setVisible(true);
			isBotBox.setVisible(true);
			nullLabel.setVisible(false);
			PlayerEntity p = null;
			for(int i = 0; i < players.length; i++) {
				if(players[i].id == player.id) {
					p = players[i];
					break;
				}
			}
			if(p != null) {
				playerBox.setSelectedItem(p);
				isBotBox.setSelected(p.isBot);
				return;
			}else {
				nullLabel.setText("            invalid id            ");
			}
		}else {
			nullLabel.setText("            null            ");
		}
		playerBox.setVisible(false);
		isBotBox.setVisible(false);
		nullLabel.setVisible(true);
		
	}
	
	@Override
	public void setEnabled(boolean value) {
		playerBox.setEnabled(value);
		isBotBox.setEnabled(value);
	}
	
	
}
