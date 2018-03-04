package server;

import shared.*;

public class Player {
	private int id;
	private String name;
	private int score;
	private Card[] cards;	//cards on hand
	public void addScore(int s) {
	}
	public int getScore() {
		return score;
	}
	public void resetScore() {
		score = 0;
	}
	public int getId() {
		return id;
	}
}
