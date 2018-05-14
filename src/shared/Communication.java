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
	protected int port;
	private final int bufferSize = 60000;
	private byte[] receiveBuffer;
	private DatagramSocket socket;
	private DatagramPacket receivePacket;
	private ObjectMapper objectMapper;

	public Communication() {
		this(defaultListenPort);
	}

	/**
	 * Use specific listen port.
	 * @param port listen port
	 */
	public Communication(int port) {
		receiveBuffer = new byte[bufferSize];
		receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		objectMapper = new ObjectMapper();
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
		try {
			socket.receive(receivePacket);
		}catch(SocketException se) {
			System.err.println(se.getMessage());
		}
		

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
		if(!socket.isClosed()) {
			socket.send(packet);
		}else {
			System.err.println("Can't send packet, socket is closed");
		}
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

	public int getListenPort() {
		return port;
	}
}
