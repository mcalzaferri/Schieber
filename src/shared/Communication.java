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

import ch.ntb.jass.common.proto.Message;

public class Communication {
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
		System.out.println("Packet received");

		// deserialize message (will be replaced with json deserialization later)
		ObjectInputStream objIn = new ObjectInputStream(
				new ByteArrayInputStream(receivePacket.getData()));
		InternalMessage msg = null;
		try {
			msg.message = (Message)objIn.readObject();
			msg.senderAddress = (InetSocketAddress)receivePacket.getSocketAddress();
			return msg;
		} catch(InvalidClassException e) {
			System.out.println("Invalid object received.");
			return null;
		}
	}

	/**
	 * Send message to peer.
	 * @param msg Message to send.
	 * @throws IOException
	 */
	public void send(Message msg, InetSocketAddress peerAddress) throws IOException {
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
