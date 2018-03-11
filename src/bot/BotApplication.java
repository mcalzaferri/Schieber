package bot;

public class BotApplication {

	private static BotCommunication communication;
	private static final int port = 5555;
	private static VirtualClient client;
	
	
	public static void main(String[] args) {
		communication = new BotCommunication(port);
		client = new VirtualClient(communication);
		
		while(client.active) {
		
		}

	}

}
