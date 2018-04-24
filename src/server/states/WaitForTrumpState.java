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
	
	private boolean schiebenAlreadyChosen;
	
	public WaitForTrumpState() throws IOException{	
		schiebenAlreadyChosen = false;
		ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();		
		send(ctMsg, logic.getPlayer(logic.getRandomSeat()));
	}
	
	/**
	 * @see GameState#handleMessage(Player, ToServerMessage)
	 */
	public boolean handleMessage(Player sender, ToServerMessage msg) throws IOException{
		if(msg instanceof ChosenTrumpMessage){
			ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();
			
			//Sends request to choose trump back to the sender
			if(schiebenAlreadyChosen){
				send(ctMsg, sender);
			}
			//Sends request to choose trump to the teammember
			else if(((ChosenTrumpMessage) msg).trump.equals(Trump.SCHIEBEN)){
				broadcast(msg);	
				send(ctMsg, logic.getTeamMember(sender));
				schiebenAlreadyChosen = true;
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
