package server;

import shared.*;
import ch.ntb.jass.common.proto.*;

public class ServerCommunication extends Communication {
	public ServerCommunication(int port) {
		super(null, port);
	}
	public Message receive() {
		return null;
	}
	public void broadcast(ToPlayerMessage m){
		
	}
	public void send(ToPlayerMessage m, Player p){
		
	}
	public void open() {
		
	}
	public void close() {
		
	}
}
