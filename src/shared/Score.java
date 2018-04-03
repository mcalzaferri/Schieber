package shared;

import java.util.Enumeration;

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
			   for (Enumeration<Integer> e = this.scores.keys(); e.hasMoreElements();) {
				   this.scores.remove(e.nextElement());
			   }
		}else {
			//There are some entities in the score -> update those values
			for (Enumeration<Integer> e = score.scores.keys(); e.hasMoreElements();) {
				Integer i = e.nextElement();
				this.scores.put(i, score.scores.get(i));
			}
		}
				
	}
		
	//Getters and Setters
	public int getScore(Team team) {
		return scores.get((Integer)team.getTeamId());
	}
}
