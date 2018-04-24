package client;

import java.io.IOException;
import java.net.InetSocketAddress;

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

	public ClientCommunication() {
		super();
	}

	public ClientCommunication(int listenPort) {
		super(listenPort);
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
		// TODO what do now?
	}

	/**
	 * Connect to server.
	 * When connecting to the server the id is always 0 as the server has to calculate the id first.
	 * @param serverAddress Address of the Game Server
	 * @param username The username the connecting player prefers
	 * @param isBot True if a bot wants to connect, false otherwise
	 * @return returns the errocode or 0 if connected successfully
	 * @throws Exception 
	 */
	public void connect(InetSocketAddress serverAddress, String username, boolean isBot) throws Exception {
		this.serverAddress = serverAddress;
		JoinLobbyMessage msg = new JoinLobbyMessage();
		msg.playerData = new PlayerEntity();
		msg.playerData.isBot = isBot;
		msg.playerData.name = username;
		send(msg);
		//Wait until Result is received
		boolean loop = true;
		while(loop) {
			//Wait until result of msg is sent by server
			try {
				InternalMessage imsg = receive();
				if(imsg.message instanceof ResultMessage) {
					if(((ResultMessage)imsg.message).code == Code.OK) {
						loop = false;
					}else {
						throw new Exception(((ResultMessage)imsg.message).message);
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		
		//Start receive Thread after connection has been established
		new Thread(this).start();
	}
	
	@Override
	public void close() {
		run = false;
		super.close();
	}

	@Override
	public void run() {
		run = true;
		while(run) {
			try {
				InternalMessage msg = receive();
				handleReceivedMessage(msg);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
