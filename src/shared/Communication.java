package shared;

import java.io.*;
import java.net.*;
import ch.ntb.jass.common.proto.Message;

public abstract class Communication {
	protected final int port = 65000;	// listen port
	private final int bufferSize = 60000;
	private byte[] receiveBuffer;
	private DatagramSocket socket;
	private DatagramPacket receivePacket;

	public Communication() {
		receiveBuffer = new byte[bufferSize];
		receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
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

		// deserialize message (will be replaced with json deserialization later)
		ObjectInputStream objIn = new ObjectInputStream(
				new ByteArrayInputStream(receivePacket.getData()));
		InternalMessage msg = null;
		msg.message = (Message)objIn.readObject();
		msg.SenderAddress = receivePacket.getAddress();
		return msg;
	}

	/**
	 * Send message to peer.
	 * @param msg Message to send.
	 * @throws IOException
	 */
	protected void send(InetSocketAddress peerAddress, Message msg) throws IOException {
		// serialize message (will be replaced with JSON serialization later)
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(byteStream);
		objOut.writeObject(msg);

		// create datagram packet
		DatagramPacket packet = new DatagramPacket(byteStream.toByteArray(),
				byteStream.toByteArray().length, peerAddress);

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
}
