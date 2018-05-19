package client.shared;

import ch.ntb.jass.common.proto.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public abstract class Communication {
	protected Socket socket;
	private BufferedReader socketReader;
	private PrintWriter socketWriter;
	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
	}

	/**
	 * Open TCP connection to peer
	 */
	public void openSocket(InetSocketAddress peerAddress) {
		try {
			socket = new Socket(peerAddress.getHostName(), peerAddress.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open streams for sending/receiving messages
	 */
	public void openStream() {
		try {
			socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketWriter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send message to peer
	 */
	public void send(Message msg) {
		try {
			synchronized (objectMapper) {
				socketWriter.print(objectMapper.writeValueAsString(msg) + '\n');
			}
			socketWriter.flush();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wait for message from peer
	 * @return received message or null if something went wrong
	 */
	public Message receive() throws IOException {
		try {
			String json = socketReader.readLine();
			synchronized (objectMapper) {
				return objectMapper.readValue(json, Message.class);
			}
		} catch (JsonParseException e) {
			System.err.println("Failed to parse received JSON.");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Close peer connection
	 */
	public void close() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set socket receive timeout
	 */
	public void setReceiveTimeout(int tmo) {
		try {
			socket.setSoTimeout(tmo);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
