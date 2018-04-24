package shared;

import java.util.Map;

import ch.ntb.jass.common.entities.ScoreEntity;

public class Score{
	private ScoreEntity entity;

	//Constructors
	/** Use this constructor to cast a ScoreEntity into a score
	 * @param entity The entity to be cast
	 */
	public Score(ScoreEntity entity) {
		this.entity = entity;
	}

	//Methods
	public void updateScore(ScoreEntity score) {
		if(score == null || score.scores == null || score.scores.size() == 0) {
			//Remove all entities from this score
			entity.scores.clear();
		}else {
			//There are some entities in the score -> update those values
			for(Map.Entry<Integer, Integer> s : score.scores.entrySet()) {
				entity.scores.put(s.getKey(), s.getValue());
			}
		}

	}

	//Getters and Setters
	/** Use this method to get the underlying entity of this class
	 * @return An entity representing this class
	 */
	public ScoreEntity getEntity() {
		return entity;
	}
	
	public int getScore(Team team) {
		return entity.scores.get(team.getTeamId());
	}
	
	public Map<Integer,Integer> getScores(){
		return entity.scores;
	}
}
