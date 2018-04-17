package shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ntb.jass.common.proto.Message;

public class Communication {
	protected int port = 65000;	// default listen port
	private final int bufferSize = 60000;
	private byte[] receiveBuffer;
	private DatagramSocket socket;
	private DatagramPacket receivePacket;
	private ObjectMapper objectMapper;

	public Communication() {
		receiveBuffer = new byte[bufferSize];
		receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

		objectMapper = new ObjectMapper();
	}

	/**
	 * Use specific listen port.
	 * @param port listen port
	 */
	public Communication(int port) {
		this();
		this.port = port;
	}

	/**
	 * Blocking receive.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @return The received message.
	 */
	public InternalMessage internalReceive()
			throws IOException, ClassNotFoundException {
		receivePacket.setLength(receiveBuffer.length);

		// wait for message, blocking call
		socket.receive(receivePacket);
		System.out.println("Packet received");

		try {
			InternalMessage msg = new InternalMessage();
			msg.message = objectMapper.readValue(receivePacket.getData(), Message.class);
			msg.senderAddress = (InetSocketAddress)receivePacket.getSocketAddress();
			return msg;
		} catch(JsonParseException e) {
			System.err.println("Failed to parse received json.");
			return null;
		}
	}

	/**
	 * Send message to peer.
	 * @param msg Message to send.
	 * @throws IOException
	 */
	public void send(Message msg, InetSocketAddress peerAddress) throws IOException {
		String jsonString = objectMapper.writeValueAsString(msg);

		// create datagram packet
		DatagramPacket packet = new DatagramPacket(jsonString.getBytes("UTF-8"),
				jsonString.getBytes().length, peerAddress);

		// send packet
		socket.send(packet);
	}

	/**
	 * Open socket.
	 * @throws SocketException
	 */
	public void open() throws SocketException {
		socket = new DatagramSocket(port);
	}

	/**
	 * Close socket.
	 */
	public void close() {
		if (socket != null) {
			socket.close();
		}
	}

	/**
	 * Set receive timeout for socket.
	 * @param tmo timeout in milliseconds
	 * @throws SocketException
	 */
	public void setReceiveTimeout(int tmo) throws SocketException {
		socket.setSoTimeout(tmo);
	}

	public void setListenPort(int port) {
		this.port = port;
	}
}
