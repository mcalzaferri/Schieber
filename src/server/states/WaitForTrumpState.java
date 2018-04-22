package server.states;

import java.io.IOException;

import ch.ntb.jass.common.entities.PlayerEntity;
import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.proto.*;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import shared.Player;
import shared.Seat;
import shared.Trump;

public class WaitForTrumpState extends GameState{
	
	public WaitForTrumpState() throws IOException{		
		ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();		
		send(ctMsg, logic.getPlayer(logic.getRandomSeat()));
	}
	
	/**
	 * @see GameState#handleMessage(Player, ToServerMessage)
	 */
	public boolean handleMessage(Player sender, ToServerMessage msg) throws IOException{
		if(msg instanceof ChosenTrumpMessage){
			if(((ChosenTrumpMessage) msg).trump.equals(Trump.SCHIEBEN)){
				ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();
				broadcast(msg);
				Player nextPlayer;
				
				switch(sender.getSeat().getSeatEntity()){
				case SEAT1 : 
					nextPlayer = logic.getPlayer(SeatEntity.SEAT3);
					send(ctMsg, nextPlayer);
					break;
				case SEAT2 : 
					nextPlayer = logic.getPlayer(SeatEntity.SEAT4);
					send(ctMsg, nextPlayer);
					break;
				case SEAT3 : 
					nextPlayer = logic.getPlayer(SeatEntity.SEAT1);
					send(ctMsg, nextPlayer);
					break;
				case SEAT4 : 
					nextPlayer = logic.getPlayer(SeatEntity.SEAT2); 
					send(ctMsg, nextPlayer);
					break;
				default:	
					//Exceptionhandling
					break;				
				}
			}
			else{
				ChosenTrumpInfoMessage ctiMsg = new ChosenTrumpInfoMessage();
				ctiMsg.trump = ((ChosenTrumpMessage) msg).trump;
				Trump trump = Trump.getByEntity(((ChosenTrumpMessage) msg).trump);
				logic.setTrump(trump);
				broadcast(ctiMsg);
				stateMachine.changeState(new waitForCardState());
			}		
		}		
		return true;
	}
}
