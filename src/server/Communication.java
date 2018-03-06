package server;

import shared.proto.*;
import shared.*;

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
