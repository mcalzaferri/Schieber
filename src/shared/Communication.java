package shared;

import ch.ntb.jass.common.proto.Message;

public class Communication {
	private int port;
	private String ip;
	public Communication(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	public Message receive() {
		return null;
	}
	public void send(Message m){
		
	}
	public void open() {
		
	}
	public void close() {
		
	}
	
	public void waitForMessage() {
		
	}
}
