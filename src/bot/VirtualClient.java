package bot;

import shared.Card;
import shared.CardList;
import shared.Player;
import shared.Score;
import shared.Team;
import shared.Trump;
import shared.Weis;
import shared.client.AbstractClient;
import shared.proto.ChooseGameModeMessage;
import shared.proto.JoinGameMessage;
import shared.proto.Message;
import shared.proto.PlaceCardMessage;
import shared.proto.ToPlayerMessage;

public class VirtualClient extends AbstractClient {

	BotCommunication com;
	BotIntelligence ki = new BotIntelligence();
	Boolean active = true;
	
//TODO: fkaiser@lgassner: remove? will be handled in com-class, won't it?	
//	Thread receiver = new Thread(){
//        public void run(){
//            while(true){
//                try{  	
//                    communication.waitForMessage();
//                    Message message = communication.receive();
//                    ToPlayerMessage answer = ki.receive(message);
//                    communication.broadcast(answer);
//                    
//                    // Test
//                    System.out.println("still active");
//                   
//                } catch(Exception e){System.out.println("Exception: " + e.toString());}
//            }
//        }
//    };
	
	public VirtualClient(BotCommunication communication) {
		super();
		this.com = communication;
		communication.send(new JoinGameMessage(this));
		//receiver.start();
	}
	
	@Override
	public void setTrump(Trump trump) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveCardToDeck(AbstractClient virtualClient, Card card) {
		com.send(new PlaceCardMessage(this, card));
	}

	@Override
	public void updateDeck(CardList deck) {
		ki.setDeck(deck);
	}

	@Override
	public void updateHand(CardList hand) {
		ki.setHand(hand);
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
		//disable bot
		this.active = false;
		
		//TODO: fkaiser@lgassner: remove if not needed (if communication is handled outside this class then receiver is not needed)
		///this.receiver.interrupt();
		
		//destroy intelligence = reset bot
		this.ki = null;
	}

	@Override
	public void moveCardToDeck(Player source, Card card) {
		//TODO: fkaiser: check if needed for bots? discussed with Maurus "for animation reasosons"
	}

}
