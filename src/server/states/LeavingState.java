package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import shared.Player;

public class LeavingState extends GameState{

	@Override
	public boolean handleMessage(Player sender, ToServerMessage msg) throws IOException {
		
		if(msg instanceof LeaveLobbyMessage) {
			
			PlayerLeftLobbyInfoMessage pllim = new PlayerLeftLobbyInfoMessage();
			broadcast(pllim);
			
			logic.removePlayer(sender);
			
		} else if(msg instanceof LeaveTableMessage) {
			
			PlayerMovedToLobbyInfoMessage pmtlim = new PlayerMovedToLobbyInfoMessage();
			broadcast(pmtlim);	
			
			//TODO: Remove player from table.
			
			EndOfRoundInfoMessage eorim = new EndOfRoundInfoMessage();
			broadcast(eorim);
			
			stateMachine.changeState(new LobbyState());			
		} else {
			return false;
		}
		return true;
	}
}
