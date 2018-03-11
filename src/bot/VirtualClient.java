package bot;

import shared.Card;
import shared.CardList;
import shared.Player;
import shared.Score;
import shared.Team;
import shared.Trumpf;
import shared.Weis;
import shared.client.AbstractClient;
import shared.proto.ChooseGameModeMessage;
import shared.proto.JoinGameMessage;
import shared.proto.Message;
import shared.proto.ToPlayerMessage;

public class VirtualClient extends AbstractClient {

	BotCommunication communication;
	BotIntelligence ki;
	Boolean active = true;
	
	Thread receiver = new Thread(){
        public void run(){
            while(true){
                try{
                	
                    communication.waitForMessage();
                    Message message = communication.receive();
                    ToPlayerMessage answer = ki.receive(message);
                    communication.broadcast(answer);
                    
                    // Test
                    System.out.println("still active");
                   
                } catch(Exception e){System.out.println("Exception: " + e.toString());}
            }
        }
    };
	
	public VirtualClient(BotCommunication communication) {
		super();
		this.communication = communication;
		communication.send(new JoinGameMessage());
		receiver.start();
		
	}
	
	@Override
	public void setTrumpf(Trumpf trumpf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveCardToDeck(Player source, Card card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDeck(CardList deck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateHand(CardList hand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publishWeis(Weis weis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publishStich(Player winner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScore(Score score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endGame(Team winner) {
		// TODO Auto-generated method stub
		
	}

}
