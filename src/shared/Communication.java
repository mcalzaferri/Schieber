package shared;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ntb.jass.common.proto.Message;

public class Communication {
	public final static int defaultListenPort = 65000;
	protected int port = defaultListenPort;
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
	public InternalMessage receive()
			throws IOException, ClassNotFoundException {
		receivePacket.setLength(receiveBuffer.length);

		// wait for message, blocking call
		socket.receive(receivePacket);
		System.out.println("Packet received");

		InternalMessage msg = new InternalMessage();
		msg.senderAddress = (InetSocketAddress)receivePacket.getSocketAddress();

		try {
			msg.message = objectMapper.readValue(receivePacket.getData(), Message.class);
		} catch(JsonParseException e) {
			System.err.println("Failed to parse received json.");
		}

		return msg;
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
