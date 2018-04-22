package server.states;

import java.io.IOException;

import ch.ntb.jass.common.entities.TargetScoreEntity;
import ch.ntb.jass.common.entities.TeamEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.GameStartedInfoMessage;
import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import shared.Player;

public class LobbyState extends GameState {
	@Override
	public boolean handleMessage(Player sender, ToServerMessage msg) throws IOException {
		if(msg instanceof JoinTableMessage) {
			JoinTableMessage jtMsg = (JoinTableMessage) msg;
			if(!logic.addPlayerToTable(sender, jtMsg.preferedSeat)) {
				sendResultMsg(ResultMessage.Code.FAILURE,
						"Chosen seat is occupied. Please choose a free seat.",
						sender);
			} else {
				sendResultMsg(ResultMessage.Code.OK, "", sender);
			}
		} else if(msg instanceof ChangeStateMessage) {
			ChangeStateMessage csMsg = (ChangeStateMessage) msg;

			sender.setReady(csMsg.isReady);

			if (logic.allPlayersReady()) {
				GameStartedInfoMessage gsMsg = new GameStartedInfoMessage();
				gsMsg.targetScore = TargetScoreEntity.TO_1000;
				gsMsg.teams = new TeamEntity[]{logic.getTeam1(), logic.getTeam2()};
				broadcast(gsMsg);
				stateMachine.changeState(new StartGameState());
			}
		} else {
			return false;
		}
		return true;
	}
}
