package server;

import java.io.IOException;

public class ServerApp {
	static public void main(String[] args)
			throws ClassNotFoundException, IOException {
		GameLogic logic = new GameLogic();
		MessageHandler msgHandler = new MessageHandler(logic);

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
