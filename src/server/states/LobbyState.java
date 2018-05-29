package server.states;

import java.io.IOException;
import bot.BotApplication;
import ch.ntb.jass.common.entities.TargetScoreEntity;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.GameStartedInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerChangedStateMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToTableInfoMessage;
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
			com.broadcast(pmtlMsg);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessage(Player sender, JoinTableMessage msg) throws IOException {
		Seat seat = msg.preferedSeat == null ? null : Seat.getBySeatNr(msg.preferedSeat.getSeatNr());
		logic.addPlayerToTable(sender, seat);

		if (sender.isAtTable()) {
			PlayerMovedToTableInfoMessage pmttMsg = new PlayerMovedToTableInfoMessage();
			pmttMsg.player = sender.getEntity();
			com.broadcast(pmttMsg);
			System.out.println("added " + sender + " to the table (" + sender.getSeat() + ")");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessage(Player sender, ChangeStateMessage msg) throws IOException {
		if (sender.isReady() != msg.isReady) {
			sender.setReady(msg.isReady);

			PlayerChangedStateMessage pcsMsg = new PlayerChangedStateMessage();
			pcsMsg.player = sender.getEntity();
			pcsMsg.isReady = sender.isReady();
			com.broadcast(pcsMsg);

			if (logic.areAllPlayersReady()) {
				// start game
				GameStartedInfoMessage gsMsg = new GameStartedInfoMessage();
				gsMsg.targetScore = TargetScoreEntity.TO_1000;
				gsMsg.teams = logic.getTeams();
				com.broadcast(gsMsg);
				stateMachine.changeState(new StartRoundState());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessage(Player sender, FillEmptySeatsMessage msg) {

		// fill empty seats with bots

		new Thread(() -> {
			int numBots = Math.max(0, 4 - logic.getPlayerCount());
			for (int i = 0; i < numBots; i++) {
				BotApplication.start("localhost", com.getListenPort(), null);
			}
		}).start();
	}
}
