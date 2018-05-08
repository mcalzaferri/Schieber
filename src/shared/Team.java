package shared;

import java.util.Map;

import ch.ntb.jass.common.entities.TeamEntity;

public class Team {
	private Player[] players;
	private int teamId;
	
	//Constructors
	public Team(Player[] players, int teamId) {
		this.players = players;
		this.teamId = teamId;
	}
	
	public Team(TeamEntity entity, Map<Integer,Player> dictionary) {
		players = new Player[2];
		teamId = entity.teamId;
		for(int i = 0; i <= 1; i++) {
			if(dictionary != null && dictionary.get(entity.players[i].id) != null)
				players[i] = dictionary.get(entity.players[i].id);
			else
				players[i] = new Player(entity.players[i]);
		}
	}
	
	/** Use this constructor to cast a TeamEntity into a Team
	 * @param entity The entity to be cast
	 */
	public Team(TeamEntity entity) {
		this(entity, null);
	}
	
	//static methods
	/** Returns the team which contains the player. If none of the teams contain this player null is returned.
	 * @param teams An array of teams which is checked
	 * @param player The player to search in the teams
	 * @return the players team or null if this player is not in a team.
	 */
	public static Team getTeamThatContainsPlayer(Team[] teams, Player player) {
		for(Team team : teams) {
			if(team.contains(player))
				return team;
		}
		return null;
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
	/** Use this method to get the underlying entity of this class
	 * @return An entity representing this class
	 */
	public TeamEntity getEntity() {
		TeamEntity team = new TeamEntity();
		team.players = Player.getEntities(players);
		team.teamId = teamId;
		return team;
	}
	
	public boolean contains(Player player) {
		for(Player p : players) {
			if(p.equals(player))
				return true;
		}
		return false;
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
