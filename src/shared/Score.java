package shared;

import java.util.Enumeration;
import java.util.Map;

import ch.ntb.jass.common.entities.ScoreEntity;

public class Score extends ScoreEntity{
		//Dictionary of <Team, Score>
		//Use get((teamId) key) to obtain the score of one team

	//Constructors
	public Score(ScoreEntity entity) {
		this.scores = entity.scores;
	}

	//Methods
	public void updateScore(ScoreEntity score) {
		if(score == null || score.scores == null || score.scores.size() == 0) {
			//Remove all entities from this score
			this.scores.clear();
		}else {
			//There are some entities in the score -> update those values
			for(Map.Entry<Integer, Integer> s : score.scores.entrySet()) {
				this.scores.put(s.getKey(), s.getValue());
			}
		}

	}

	//Getters and Setters
	public int getScore(Team team) {
		return scores.get(team.getTeamId());
	}
}
