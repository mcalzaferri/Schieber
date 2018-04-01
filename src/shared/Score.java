package shared;

import java.util.Dictionary;

import ch.ntb.jass.common.entities.ScoreEntity;

public class Score {
	//Dictionary of <Team, Score>
		//Use get((teamId) key) to obtain the score of one team
		private Dictionary<Integer, Integer> scores;
		
		public Score(ScoreEntity entity) {
			//TODO this.scores = entity.scores;
		}
		
		public int getScore(Team team) {
			return scores.get((Integer)team.getTeamId());
		}
}
