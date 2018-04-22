package server.states;

import java.io.IOException;

import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.entities.TrumpEntity;
import ch.ntb.jass.common.proto.*;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import shared.Player;

public class WaitForTrumpState extends GameState{
	
	public WaitForTrumpState() throws IOException{		
		ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();		
		//Always start with first player?
		send(ctMsg, logic.getPlayer(SeatEntity.SEAT1));
	}
	
	/**
	 * @see GameState#handleMessage(Player, ToServerMessage)
	 */
	public boolean handleMessage(Player sender, ToServerMessage msg) throws IOException{
		if(msg instanceof ChosenTrumpMessage){
			if(((ChosenTrumpMessage) msg).trump.equals(TrumpEntity.SCHIEBEN)){
				ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();
				broadcast(msg);
				//Player 3 doesn't know that he is next?
				send(ctMsg, logic.getPlayer(SeatEntity.SEAT3));
			}
			else{
				ChosenTrumpInfoMessage ctiMsg = new ChosenTrumpInfoMessage();
				
				//The trump is currently a TrumpEntity (ch.ntb.jass.common.entities) object, 
				//not a Trump (shared) object, how to cast?
				
				ctiMsg.trump = ((ChosenTrumpMessage) msg).trump;
				logic.setTrump(ctiMsg.trump);
				broadcast(ctiMsg);
				stateMachine.changeState(new waitForCardState());
			}		
		}		
		return true;
	}
}
