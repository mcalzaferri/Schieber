package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.GameStartedInfoMessage;
import shared.Player;

public class LobbyState extends GameState {
	@Override
	public boolean handleMessage(Player sender, ToServerMessage msg) throws IOException {
		if(msg instanceof JoinTableMessage) {
			JoinTableMessage jtm = (JoinTableMessage) msg;
			logic.addPlayerToTable(sender, jtm.preferedSeat.seatNr);
		} else if(msg instanceof ChangeStateMessage) {
			ChangeStateMessage csm = (ChangeStateMessage) msg;

			sender.setReady(csm.isReady);

			if (logic.allPlayersReady()) {
				GameStartedInfoMessage gsim = new GameStartedInfoMessage();
				// TODO:
				//gsim.teams = ;
				broadcast(gsim);
				stateMachine.changeState(new StartGameState());
			}
		} else {
			return false;
		}
		return true;
	}
}
