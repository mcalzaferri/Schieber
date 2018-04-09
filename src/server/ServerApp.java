package server;

import java.io.IOException;
import java.net.SocketException;

import server.states.GameState;
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
		ServerApp app = new ServerApp();

		// TODO: check if this actually works:
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Schieber server is shutting down...");
				app.stop();
			}
		});

		// Start receiving messages
		app.run();
	}

	public ServerApp() throws SocketException {
		com = new Communication();
		logic = new GameLogic();

		// init state machine
		stateMachine = new StateMachine();
		GameState.init(stateMachine, com, logic);
	}

	public ServerApp(int port) throws SocketException {
		this();
		com.setListenPort(port);
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
		InternalMessage iMsg = com.internalReceive();

		// try to get player from game
		// this will be null if the player is new
		Player sender = logic.getPlayer(iMsg.senderAddress);

		// log received message
		System.out.print("received " + iMsg.message.getClass().getSimpleName());
		if(sender != null) {
			System.out.println(" from " + sender.getName());
		} else {
			System.out.println();
		}

		// let state machine handle the message
		if(!stateMachine.handleMessage(sender, iMsg)) {
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

	public void setState(GameState state) {
		stateMachine.changeState(state);
	}

	public void open() throws SocketException {
		com.open();
	}
}
