package shared.client;

public enum GameState {
	IDLE, //Game has not yet startet
	STARTED, //Game has begun
	TURNACTIVE, //Turn is currently active
	TURNOVER, //Turn is over
	PLAYOVER, //4 Turns are over TODO find better name
	ROUNDOVER, //Round is over
	GAMEOVER //Game is over
}
