package server;

import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import server.exceptions.ClientErrorException;
import server.exceptions.InvalidMessageDataException;
import server.exceptions.UnhandledMessageException;
import shared.Communication;
import shared.Player;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection extends Communication implements Runnable {
	private Player player;
	private SchieberMessageHandler messageHandler;
	private ClientConnectionClosedListener conClosedListener;


	public ClientConnection(Socket clientSocket, Player player,
	                        SchieberMessageHandler schieberMessageHandler,
	                        ClientConnectionClosedListener conClosedListener) {
		this.socket = clientSocket;
		this.player = player;
		this.messageHandler = schieberMessageHandler;
		this.conClosedListener = conClosedListener;

		openStream();
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * Wait for messages and let the message handler handle them
	 * This function exits when the connection to the client is closed.
	 */
	@Override
	public void run() {
		while (true) {

			Message msg;

			try {
				// wait for data
				msg = receive();
			} catch (SocketException e) {
				System.out.println(player + " disconnected (" + e.getMessage() + ")");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

			// log received message
//			System.out.print("received ");
//			if (msg != null) {
//				System.out.print(msg.getClass().getSimpleName());
//			} else {
//				System.out.print("invalid message");
//			}
//			System.out.println(" from " + player);

			try {

				messageHandler.handleMessage(player, msg);

				sendResultMsg(ResultMessage.Code.OK, "you fine.");

			} catch (UnhandledMessageException e) {
				System.err.println("unhandled message: "
						+ msg.getClass().getSimpleName());
			} catch (InvalidMessageDataException e) {
				sendResultMsg(ResultMessage.Code.PROTOCOL_ERROR, e.getMessage());
			} catch (ClientErrorException e) {
				sendResultMsg(ResultMessage.Code.FAILURE, e.getMessage());
			} catch (NullPointerException e) {
				sendResultMsg(ResultMessage.Code.PROTOCOL_ERROR,
						"Something went wrong while processing the data you sent." +
								" The server will just assume it's your fault and continue.");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		conClosedListener.connectionClosed(this);
	}

	/**
	 * Send a result message to the player
	 */
	private void sendResultMsg(ResultMessage.Code errorCode, String errorText) {
		ResultMessage resMsg = new ResultMessage();
		resMsg.code = errorCode;
		resMsg.message = errorText;
		if (errorCode != ResultMessage.Code.OK) {
			System.err.println("client error (" + errorText + ")");
		}
		send(resMsg);
	}

	@Override
	public String toString() {
		return "connection to " + player;
	}
}
