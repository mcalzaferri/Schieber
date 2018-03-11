package bot;

import shared.proto.*;
import shared.*;

public class BotCommunication extends Communication {
	public BotCommunication(int port) {
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
