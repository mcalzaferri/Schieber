package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import server.exceptions.ClientErrorException;
import server.exceptions.InvalidMessageDataException;
import server.exceptions.UnhandledMessageException;
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
		start(Integer.parseInt(args[0]));
	}

	static public void start(int listenPort) throws  ClassNotFoundException, IOException {
		ServerApp app = new ServerApp(listenPort);

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
		System.out.println("waiting for Message...");
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
		if (sender != null) {
			System.out.println(" from " + sender.getName());
		} else {
			System.out.println();
		}

		try {
			// let state machine handle the message
			stateMachine.handleMessage(sender, iMsg);
			sendResultMsg(ResultMessage.Code.OK, "you fine 👌",
					iMsg.senderAddress);
		} catch (UnhandledMessageException e) {
			System.err.println("unhandled message, current state = "
					+ stateMachine.getCurrentState().getClass().getSimpleName());
		} catch (InvalidMessageDataException e) {
			sendResultMsg(ResultMessage.Code.PROTOCOL_ERROR,
					e.getMessage(), iMsg.senderAddress);
		} catch (ClientErrorException e) {
			sendResultMsg(ResultMessage.Code.FAILURE, e.getMessage(),
					iMsg.senderAddress);
		} catch (NullPointerException e) {
			sendResultMsg(ResultMessage.Code.PROTOCOL_ERROR,
					"Something went wrong while processing the data you sent." +
					" The server will just assume it's your fault and continue 😂.",
					iMsg.senderAddress);
			e.printStackTrace();
		}
	}

	private void sendResultMsg(ResultMessage.Code errorCode,
			String errorText, InetSocketAddress playerAddr) throws IOException {
		ResultMessage resMsg = new ResultMessage();
		resMsg.code = errorCode;
		resMsg.message = errorText;
		if(errorCode != ResultMessage.Code.OK) {
			System.err.println("client error (" + errorText + ")");
		}
		com.send(resMsg, playerAddr);
		System.out.println("sent result: " + errorCode);
	}

	public void stop() {
		running = false;
		com.close();
	}
}
