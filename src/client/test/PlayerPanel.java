package client.test;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import shared.Player;

public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 4529969236622381523L;
	JLabel description;
	JComboBox<Player> playerBox;
	JCheckBox isBotBox;
	JCheckBox isReadyBox;
	
	public PlayerPanel(String text, Player[] players) {
		super();
		description = new JLabel(text);
		initialComponents(players);
	}
	
	private void initialComponents(Player[] players) {
		playerBox = new JComboBox<>(players);
		isBotBox = new JCheckBox("isBot");
		isReadyBox = new JCheckBox("isReady");
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(description);
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
