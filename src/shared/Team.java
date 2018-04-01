package shared;

import ch.ntb.jass.common.entities.TeamEntity;

public class Team {
	private Player[] players;
	private int teamId;
	
	public Team(TeamEntity entity) {
		players = new Player[2];
		for(int i = 0; i <= 1; i++) {
			players[i] = new Player(entity.players[i]);
		}
		teamId = entity.teamId;
	}
	
	public Player getPlayer(int index) {
		return players[index];
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public int getTeamId() {
		return teamId;
	}
}
