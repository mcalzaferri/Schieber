package server;

import java.io.IOException;

public class ServerApp {
	static public void main(String[] args) throws ClassNotFoundException, IOException {
		GameLogic logic = new GameLogic();
		MessageHandler msgHandler = new MessageHandler(logic);
		
		msgHandler.handleMessgages();
	}
}
