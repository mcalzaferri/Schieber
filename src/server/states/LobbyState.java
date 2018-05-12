package server.states;

import java.io.IOException;
import java.net.SocketException;

import bot.BotApplication;
import ch.ntb.jass.common.entities.TargetScoreEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.GameStartedInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerChangedStateMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToTableInfoMessage;
import server.exceptions.ClientErrorException;
import server.exceptions.UnhandledMessageException;
import shared.Player;
import shared.Seat;

public class LobbyState extends GameState {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void act() throws IOException {
		logic.init();

		// move all players to lobby
		PlayerMovedToLobbyInfoMessage pmtlMsg = new PlayerMovedToLobbyInfoMessage();
		for (Seat s : Seat.values()) {
			if(s.equals(Seat.NOTATTABLE)) {
				continue;
			}
			Player p = logic.getPlayer(s);
			if (p == null) {
				continue;
			}
			p.setReady(false);
			p.setSeat(Seat.NOTATTABLE);
			pmtlMsg.player = p.getEntity();
			broadcast(pmtlMsg);
		}
	}

	/**
	 * @throws ClientErrorException
	 * @throws UnhandledMessageException
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessage(Player sender, ToServerMessage msg)
			throws IOException, UnhandledMessageException {
		if (msg instanceof JoinTableMessage) {
			JoinTableMessage jtMsg = (JoinTableMessage) msg;
			Seat seat = jtMsg.preferedSeat == null ? null : Seat.getBySeatNr(jtMsg.preferedSeat.getSeatNr());
			logic.addPlayerToTable(sender, seat);

			if (sender.isAtTable()) {
				PlayerMovedToTableInfoMessage pmttMsg = new PlayerMovedToTableInfoMessage();
				pmttMsg.player = sender.getEntity();
				broadcast(pmttMsg);
				System.out.println("added " + sender + " to the table (" + sender.getSeat() + ")");
			}
			return;
		}

		if (msg instanceof ChangeStateMessage) {
			ChangeStateMessage csMsg = (ChangeStateMessage) msg;

			if (sender.isReady() != csMsg.isReady) {
				sender.setReady(csMsg.isReady);

				PlayerChangedStateMessage pcsMsg = new PlayerChangedStateMessage();
				pcsMsg.player = sender.getEntity();
				pcsMsg.isReady = sender.isReady();
				broadcast(pcsMsg);

				if (logic.areAllPlayersReady()) {
					// start game
					GameStartedInfoMessage gsMsg = new GameStartedInfoMessage();
					gsMsg.targetScore = TargetScoreEntity.TO_1000;
					gsMsg.teams = logic.getTeams();
					broadcast(gsMsg);
					stateMachine.changeState(new StartRoundState());
				}
			}
			return;
		}

		if (msg instanceof FillEmptySeatsMessage) {
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
			return;
		}

		throw(new UnhandledMessageException());
	}
}
