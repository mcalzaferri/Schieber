package server.states;

import java.io.IOException;
import java.net.SocketException;

import bot.BotApplication;
import ch.ntb.jass.common.entities.TargetScoreEntity;
import ch.ntb.jass.common.entities.TeamEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.GameStartedInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerChangedStateMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToTableInfoMessage;
import server.exceptions.ClientErrorException;
import server.exceptions.UnhandledMessageException;
import shared.Player;
import shared.Seat;

public class LobbyState extends GameState {

	/**
	 * @see GameState#act()
	 */
	@Override
	public void act() throws IOException {
		logic.init();
	}

	/**
	 * @throws ClientErrorException
	 * @throws UnhandledMessageException
	 * @see GameState#handleMessage(Player, ToServerMessage)
	 */
	@Override
	public void handleMessage(Player sender, ToServerMessage msg) throws IOException, ClientErrorException, UnhandledMessageException {
		if (msg instanceof JoinTableMessage) {
			JoinTableMessage jtMsg = (JoinTableMessage) msg;
			Seat seat = jtMsg.preferedSeat == null ? null : Seat.getBySeatNr(jtMsg.preferedSeat.getSeatNr());
			logic.addPlayerToTable(sender, seat);

			PlayerMovedToTableInfoMessage pmttMsg = new PlayerMovedToTableInfoMessage();
			pmttMsg.player = sender.getEntity();
			broadcast(pmttMsg);
			System.out.println("added " + sender + " to the table (" + sender.getSeat() + ")");
		} else if (msg instanceof ChangeStateMessage) {
			ChangeStateMessage csMsg = (ChangeStateMessage) msg;

			sender.setReady(csMsg.isReady);

			PlayerChangedStateMessage pcsMsg = new PlayerChangedStateMessage();
			pcsMsg.player = sender.getEntity();
			pcsMsg.isReady = csMsg.isReady;
			broadcast(pcsMsg);

			if (logic.areAllPlayersReady()) {
				GameStartedInfoMessage gsMsg = new GameStartedInfoMessage();
				gsMsg.targetScore = TargetScoreEntity.TO_1000;
				gsMsg.teams = new TeamEntity[]{logic.getTeam1(), logic.getTeam2()};
				broadcast(gsMsg);
				stateMachine.changeState(new StartRoundState());
			}
		} else if (msg instanceof FillEmptySeatsMessage) {
			// fill empty seats with bots

			Thread[] botThreads = new Thread[4 - logic.getTablePlayerCount()];

			for (int i = 0; i < botThreads.length; i++) {
				final int j = i + 1;
				botThreads[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							BotApplication.start(com.getListenPort() + j,
									"localhost", com.getListenPort());
						} catch (SocketException e) {
							e.printStackTrace();
						}
					}
				});
			}
		} else {
			throw(new UnhandledMessageException());
		}
	}
}
