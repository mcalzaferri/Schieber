package server;

import java.io.IOException;

import server.states.GameState;
import server.states.LobbyState;
import server.states.StateMachine;

/**
 * Main entry point of the schieber server.
 * After everything has been initialized the server waits for messages and
 * hands them over to the state machine.
 * When an exception occurs result messages with the error text of the
 * exception are sent out to the clients.
 */
public class ServerApp {
	protected ServerCommunication com;
	protected StateMachine stateMachine;
	protected GameLogic logic;

	public ServerApp() throws IOException {
		com = new ServerCommunication();
		logic = new GameLogic();

		// init state machine
		stateMachine = new StateMachine();
		GameState.init(stateMachine, com, logic);
		stateMachine.changeState(new LobbyState());
	}

	public ServerApp(int listenPort) throws IOException {
		this();
		com.setListenPort(listenPort);
	}

	/**
	 * Start the schieber server
	 * @param args listen port
	 */
	static public void main(String[] args) throws IOException {
//		start(Integer.parseInt(args[0]));
		start(ServerCommunication.defaultListenPort);
	}

	/**
	 * Start the schieber server
	 * @param listenPort port to listen on
	 */
	static public void start(int listenPort) throws IOException {
		ServerApp app = new ServerApp(listenPort);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Schieber server is shutting down...");
			app.stop();
		}));

		// Start receiving messages
		app.run();
	}

	/**
	 * Wait for clients and handle their messages
	 */
	public void run() {
		com.open();
		com.accept(stateMachine, stateMachine);
	}

	/**
	 * Shutdown the server
	 */
	public void stop() {
		com.close();
	}
}
