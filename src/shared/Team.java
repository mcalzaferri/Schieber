package shared;

import java.util.Dictionary;

import ch.ntb.jass.common.entities.TeamEntity;

public class Team {
	private Player[] players;
	private int teamId;
	
	//Constructors
	public Team(Player[] players, int teamId) {
		this.players = players;
		this.teamId = teamId;
	}
	
	public Team(TeamEntity entity, Dictionary<Integer,Player> dictionary) {
		players = new Player[2];
		for(int i = 0; i <= 1; i++) {
			if(dictionary != null && dictionary.get(entity.players[i].id) != null)
				players[i] = dictionary.get(entity.players[i].id);
			else
				players[i] = new Player(entity.players[i]);
		}
	}
	
	public Team(TeamEntity entity) {
		this(entity, null);
	}
	
	
	//Methods
	public void removePlayer(int id) {
		for(int i = 0; i <= 1; i++) {
			if(players[i].getId() == id) {
				players[i] = null;
				return;
			}
		}
	}
	
	public TeamEntity getEntity() {
		TeamEntity team = new TeamEntity();
		team.players = players;
		team.teamId = teamId;
		return team;
	}
	
	
	//Getters and Setters
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
