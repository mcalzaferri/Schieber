package client.test;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import shared.Player;

public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 4529969236622381523L;
	String description;
	JComboBox<Player> playerBox;
	JCheckBox isBotBox;
	JCheckBox isReadyBox;
	
	public PlayerPanel(String text, Player[] players) {
		super();
		description = text;
		initialComponents(players);
	}
	
	private void initialComponents(Player[] players) {
		playerBox = new JComboBox<>(players);
		isBotBox = new JCheckBox("isBot");
		isReadyBox = new JCheckBox("isReady");
		playerBox.setMaximumSize(new Dimension(200, 30));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		TitledBorder title;
		title = BorderFactory.createTitledBorder(description);
		this.setBorder(title);
		this.add(playerBox);
		this.add(isBotBox);
		this.add(isReadyBox);
	}
	
	public Player getPlayer() {
		Player p = (Player)playerBox.getSelectedItem();
		p.isBot = isBotBox.isSelected();
		p.setReady(isReadyBox.isSelected());
		return p;
	}
	
	
}
