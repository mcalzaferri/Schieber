package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_messages.LobbyStateMessage;
import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import ch.ntb.jass.common.proto.server_messages.ResultMessage.Code;
import client.shared.AbstractClient;
import shared.Communication;

public class ClientCommunication extends Communication implements Runnable {
	private AbstractClient client;
	private boolean run;
	private boolean blockConnect;

	public ClientCommunication() {
		blockConnect = true;
	}

	// Methods for clients

	public void disconnect() {
		LeaveTableMessage msg = new LeaveTableMessage();
		//Only send msg if connected
		if(socket != null && !socket.isClosed() && socket.isConnected()) {
			send(msg);
		}
		close();
	}

	/**
	 * Connect to server.
	 * When connecting to the server the id is always 0 as the server has to calculate the id first.
	 * @param serverAddress Address of the Game Server
	 * @param username The username the connecting player prefers
	 * @param isBot True if a bot wants to connect, false otherwise.
	 * @throws BadResultException if the server denies the connect request or something else went wrong
	 */
	public void connect(InetSocketAddress serverAddress, String username, boolean isBot) throws BadResultException {

		open(serverAddress);

		JoinLobbyMessage jlMsg = new JoinLobbyMessage();
		jlMsg.playerData = new PlayerEntity();
		jlMsg.playerData.isBot = isBot;
		jlMsg.playerData.name = username;
		send(jlMsg);
		//Wait until Result is received
		boolean loop = blockConnect;
		while(loop) {
			//Wait until result of msg is sent by server
			try {
				Message msg = receive();
				if(msg instanceof ResultMessage) {
					if(((ResultMessage)msg).code == Code.OK) {
						loop = false;
					} else {
						throw new BadResultException(((ResultMessage)msg).message);
					}
				}else if(msg instanceof LobbyStateMessage){
					//Assume that result is ok
					client.handleReceivedMessage(msg);
					loop = false;
				}
			} catch (IOException e) {
				throw new BadResultException(e.getMessage());
			}
		}
	}

	/**
	 * Open server connection
	 */
	public void open(InetSocketAddress serverAddress) {
		openSocket(serverAddress);
		openStream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		run = false;
		super.close();
	}

	@Override
	public void run() {

		run = true;
		setReceiveTimeout(5000); //Otherwise the task will never stop

		while(run) {
			try {
				Message msg = receive();
				client.handleReceivedMessage(msg);
			} catch (SocketTimeoutException ste) {
				//System.out.println("Receive task running"); TODO
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void setBlockConnect(boolean blockConnect) {
		this.blockConnect = blockConnect;
	}

	public void setClient(AbstractClient client) {
		this.client = client;
	}
}
