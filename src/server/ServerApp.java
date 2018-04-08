package server;

import java.io.IOException;

import server.states.GameState;
import server.states.LobbyState;

public class ServerApp {
	static public void main(String[] args)
			throws ClassNotFoundException, IOException {
		GameLogic logic = new GameLogic();
		MessageHandler msgHandler = new MessageHandler(logic);

		GameState.init(msgHandler, logic);
		msgHandler.changeState(new LobbyState());

		// TODO: check if this actually works:
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Schieber server is shutting down...");
				msgHandler.stop();
			}
		});

		msgHandler.run();
	}
}
