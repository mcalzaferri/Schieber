package server;

import java.io.IOException;
import java.net.SocketException;

import server.states.GameState;
import server.states.LobbyState;
import server.states.StateMachine;
import shared.Communication;
import shared.InternalMessage;
import shared.Player;

public class ServerApp {
	private boolean running;
	protected Communication com;
	protected StateMachine stateMachine;
	protected GameLogic logic;

	static public void main(String[] args) throws ClassNotFoundException, IOException {
		ServerApp app;
		try {
			app = new ServerApp(Integer.parseInt(args[0]));
		} catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
			app = new ServerApp();
		}

		// TODO: check if this actually works:
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			@Override
//			public void run() {
//				System.out.println("Schieber server is shutting down...");
//				app.stop();
//			}
//		});

		// Start receiving messages
		app.run();
	}

	public ServerApp() throws IOException {
		com = new Communication();
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

	public void run() throws ClassNotFoundException, IOException {
		running = true;
		com.open();
		while (running) {
			handleMessage();
		}
	}

	protected void handleMessage() throws ClassNotFoundException, IOException {
		System.out.println("Waiting for Message...");
		InternalMessage iMsg = com.receive();

		// try to get player from game
		// this will be null if the player is new
		Player sender = logic.getPlayer(iMsg.senderAddress);

		// log received message
		System.out.print("received ");
		if (iMsg.message != null) {
			System.out.print(iMsg.message.getClass().getSimpleName());
		} else {
			System.out.print("invalid message");
		}
		if(sender != null) {
			System.out.println(" from " + sender.getName());
		} else {
			System.out.println();
		}

		// let state machine handle the message
		if(!stateMachine.handleMessage(sender, iMsg)) {
			//TODO: send ResultMessage with generic error message
			System.err.println("Unhandled message, current state = "
					+ stateMachine.getCurrentState().getClass().getSimpleName());
		}
	}

	public void stop() {
		running = false;
		com.close();
	}

	/*
	 * Functions for testing purposes
	 */

	public void setReceiveTimeout(int tmo) throws SocketException {
		com.setReceiveTimeout(tmo);
	}

	public void setState(GameState state) throws IOException {
		stateMachine.changeState(state);
	}

	public void open() throws SocketException {
		com.open();
	}
}
