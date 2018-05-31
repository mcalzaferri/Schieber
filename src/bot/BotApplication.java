package bot;

import java.net.InetSocketAddress;

import client.ClientCommunication;
import client.shared.ClientModel;
import shared.Seat;

/**
 * This is the starting class for the stand-alone Bot. Start it with arguments "Server Host", "Port" and "preferred Seat".
 * Preferred Seat can be omitted to have a seat assigned by the server.
 * Examples: BotApplication.jar localhost 65000 1, BotApplication.jar 192.168.0.1 65000
 * Bot will automatically try to connect to the server and if successful will play Schieber until destroyed.
 *
 */

public class BotApplication {

	private static ClientCommunication communication;

	public static void main(String[] args) {
		String serverHostname = args[0];
		int serverPort = Integer.parseInt(args[1]);
		Seat preferedSeat = null;
		try {
			int seatNr = Integer.parseInt(args[2]);
			preferedSeat = Seat.getBySeatNr(seatNr);
		}catch(Exception e) {
			System.err.println("Kein oder fehlerhafter prefered Seat angegeben");
		}
		start(serverHostname, serverPort, preferedSeat);
	}

	public static void start(String serverHostname, int serverPort, Seat preferedSeat) {
		communication = new ClientCommunication();

		ClientModel model = new ClientModel();
		//new ClientModelView(model);
		VirtualClient client = new VirtualClient(communication, model,
		//		new IntelligenceMalicious()); // start cheating bot
				new IntelligenceNormal());
		communication.setClient(client);
		client.connect(new InetSocketAddress(serverHostname, serverPort), preferedSeat);

	}

}
