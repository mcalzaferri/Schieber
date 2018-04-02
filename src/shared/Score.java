package shared;

import ch.ntb.jass.common.entities.ScoreEntity;

public class Score extends ScoreEntity{
	//Dictionary of <Team, Score>
		//Use get((teamId) key) to obtain the score of one team
		
		public Score(ScoreEntity entity) {
			//TODO this.scores = entity.scores;
		}
		
		public int getScore(Team team) {
			return 0;//TODO scores.get((Integer)team.getTeamId());
		}
}
