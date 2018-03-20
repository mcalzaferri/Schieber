package client;

import client.ClientController;
import gui.ClientModelTest;
import gui.Gui;
import shared.client.ClientModel;
import shared.*;

public class MainTest {

	public static void main(String[] args) {
		ClientModel model = new ClientModelTest();
		Gui gui = new Gui(model);
		
		ClientController controller = new ClientController(new ClientCommunicationInterface() {
			
			@Override
			public void publishChosenTrump(Trump trump) {
				System.out.println("publishChosenTrumpf");
				
			}
			
			@Override
			public void publishChosenCard(Card card) {
				System.out.println("publishChosenCard");
				
			}
			
			@Override
			public void disconnect() {
				System.out.println("disconnect");
				
			}
			
			@Override
			public boolean connect(ServerAddress serverAddress) {
				System.out.println("connect");
				return false;
			}
		}, model, gui);
		
	}

}


