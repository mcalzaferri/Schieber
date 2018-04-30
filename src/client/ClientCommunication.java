package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import ch.ntb.jass.common.proto.server_messages.ResultMessage.Code;
import shared.Communication;
import shared.InternalMessage;
import shared.client.AbstractClient;

public class ClientCommunication extends Communication implements Runnable{
	private InetSocketAddress serverAddress;
	private AbstractClient client;
	private boolean run;
	private boolean blockConnect;

	public ClientCommunication() {
		this(Communication.defaultListenPort);
	}

	public ClientCommunication(int listenPort) {
		super(listenPort);
		blockConnect = true;
	}

	// Internal methods

	/**
	 * Send message to server.
	 * @param msg Message to send.
	 * @throws IOException
	 */
	public void send(Message msg){
		try {
			super.send(msg, serverAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleReceivedMessage(InternalMessage msg) {
		// Discard messages that were not sent by the server.
		if (!serverAddress.equals(msg.senderAddress) || msg == null) {
			return;
		}
		client.handleReceivedMessage(msg.message);
	}

	// Methods for clients

	public void disconnect() {
		LeaveTableMessage msg = new LeaveTableMessage();
		send(msg);
		close();
	}

	/**
	 * Connect to server.
	 * When connecting to the server the id is always 0 as the server has to calculate the id first.
	 * @param serverAddress Address of the Game Server
	 * @param username The username the connecting player prefers
	 * @param isBot True if a bot wants to connect, false otherwise.
	 * @throws Exception if the server denies the connect request or something else went wrong
	 */
	public void connect(InetSocketAddress serverAddress, String username, boolean isBot) throws BadResultException {
		this.serverAddress = serverAddress;
		JoinLobbyMessage msg = new JoinLobbyMessage();
		msg.playerData = new PlayerEntity();
		msg.playerData.isBot = isBot;
		msg.playerData.name = username;
		send(msg);
		//Wait until Result is received
		boolean loop = blockConnect;
		while(loop) {
			//Wait until result of msg is sent by server
			try {
				InternalMessage imsg = receive();
				if(imsg.message instanceof ResultMessage) {
					if(((ResultMessage)imsg.message).code == Code.OK) {
						loop = false;
					}else {
						throw new BadResultException(((ResultMessage)imsg.message).message);
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void close() {
		run = false;
		super.close();
	}

	@Override
	public void run() {
		
		try {
			run = true;
			setReceiveTimeout(5000); //Otherwise the task will never stop
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while(run) {
			try {
				InternalMessage msg = receive();
				handleReceivedMessage(msg);
			} catch (SocketTimeoutException ste) {
				System.out.println("Receive task running");
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void setBlockConnect(boolean blockConnect) {
		this.blockConnect = blockConnect;
	}
}
