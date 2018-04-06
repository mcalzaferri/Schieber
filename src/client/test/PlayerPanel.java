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

import shared.Player;

public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 4529969236622381523L;
	String description;
	JComboBox<Player> playerBox;
	JCheckBox isBotBox;
	JCheckBox isReadyBox;
	JLabel nullLabel;
	Player[] players;
	
	public PlayerPanel(String text, Player[] players) {
		super();
		description = text;
		this.players = players;
		initialComponents();
	}
	
	private void initialComponents() {
		playerBox = new JComboBox<>(players);
		isBotBox = new JCheckBox("isBot");
		isReadyBox = new JCheckBox("isReady");
		nullLabel = new JLabel("            null            ");
		playerBox.setMaximumSize(new Dimension(200, 30));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.add(playerBox);
		this.add(isBotBox);
		this.add(isReadyBox);
		this.add(nullLabel);
		nullLabel.setVisible(false);
	}
	
	public Player getPlayer() {
		Player p = (Player)playerBox.getSelectedItem();
		p.isBot = isBotBox.isSelected();
		p.setReady(isReadyBox.isSelected());
		return p;
	}
	
	public void setPlayer(Player player) {
		if(player != null) {
			playerBox.setBackground(Color.WHITE);
			playerBox.setVisible(true);
			isBotBox.setVisible(true);
			isReadyBox.setVisible(true);
			nullLabel.setVisible(false);
			Player p = null;
			for(int i = 0; i < players.length; i++) {
				if(players[i].id == player.id) {
					p = players[i];
					break;
				}
			}
			if(p != null) {
				playerBox.setSelectedItem(p);
				isBotBox.setSelected(p.isBot);
				isReadyBox.setSelected(p.isReady());
				return;
			}else {
				nullLabel.setText("            invalid id            ");
			}
		}else {
			nullLabel.setText("            null            ");
		}
		playerBox.setVisible(false);
		isBotBox.setVisible(false);
		isReadyBox.setVisible(false);
		nullLabel.setVisible(true);
		
	}
	
	public void setEditable(boolean value) {
		playerBox.setEditable(value);
		isBotBox.setEnabled(value);
		isReadyBox.setEnabled(value);
	}
	
	
}
