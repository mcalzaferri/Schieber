package server;


import ch.ntb.jass.common.proto.ToPlayerMessage;
import shared.Player;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ServerCommunication {
	public static final int defaultListenPort = 65000;
	private int listenPort = defaultListenPort;
	private ServerSocket socket;
	private List<ClientConnection> connections;
	private final int playerIdStart = 1;
	/** used to generate unique player IDs */
	private int nextPlayerId = playerIdStart;

	/**
	 * Use specific listen port
	 */
	public ServerCommunication() {
		connections = new ArrayList<>();
	}

	/**
	 * Open server socket
	 */
	public void open() {
		try {
			socket = new ServerSocket(listenPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wait for and accept network connections
	 * Does not return as long as the socket is open.
	 * @param messageHandler msg handler to use for client messages
	 */
	public void accept(SchieberMessageHandler messageHandler) {
		while (true) {
			Socket clientSocket;

			try {

				clientSocket = socket.accept();

			} catch (SocketException e) {
				close();
				System.out.println("Server stopped accepting clients. (" +
						e.getMessage() + ")");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			InetSocketAddress playerAddr = new InetSocketAddress(
					clientSocket.getInetAddress(), clientSocket.getPort());
			Player player = new Player(playerAddr, nextPlayerId++);

			ClientConnection con = new ClientConnection(clientSocket,
					player, messageHandler, this);

			synchronized (connections) {
				connections.add(con);
			}

			// Start thread that handles this players messages
			new Thread(con).start();
		}
	}

	/**
	 * Close server socket and all client connections
	 */
	public void close() {
		try {
			socket.close();
			synchronized (connections) {
				for (ClientConnection con : connections) {
					con.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a message to the specified player
	 * @param msg message to send
	 * @param player recipient
	 */
	public void send(ToPlayerMessage msg, Player player) {
		synchronized (connections) {
			for (ClientConnection con : connections) {
				if (con.getPlayer().equals(player)) {
					con.send(msg);
					return;
				}
			}
		}
		System.err.println(player + " has no associated connection");
	}

	/**
	 * Send a message to all players
	 * @param msg message to broadcast
	 */
	public void broadcast(ToPlayerMessage msg) {
		synchronized (connections) {
			for (ClientConnection con : connections) {
				con.send(msg);
			}
		}
	}

	/**
	 * Remove client connection
	 * This function is called by a client connection when the client
	 * disconnects.
	 */
	public void removeConnection(ClientConnection con) {
		synchronized (connections) {
			connections.remove(con);
		}
		System.out.println(con + " closed");
	}


	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	public int getListenPort() {
		return listenPort;
	}
}
