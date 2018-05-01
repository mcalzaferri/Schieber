package server.states;

import java.io.IOException;

import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.ChosenWiisMessage;
import ch.ntb.jass.common.proto.player_messages.PlaceCardMessage;
import ch.ntb.jass.common.proto.server_info_messages.EndOfRoundInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.NewTurnInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.StichInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.TurnInfoMessage;
import ch.ntb.jass.common.proto.server_messages.WrongCardMessage;
import server.exceptions.UnhandledMessageException;
import shared.Card;
import shared.Player;
import shared.Seat;

public class WaitForCardState extends GameState {
	/**
	 * @see GameState#act()
	 */
	@Override
	public void act() throws IOException {
		// Request card from player
		NewTurnInfoMessage ntMsg = new NewTurnInfoMessage();
		ntMsg.nextPlayer = logic.nextPlayer().getEntity();
		ntMsg.selectWeis = logic.inFirstRun();
		broadcast(ntMsg);
	}

	/**
	 * @throws UnhandledMessageException
	 * @see GameState#handleMessage(Player, ToServerMessage)
	 */
	@Override
	public void handleMessage(Player sender, ToServerMessage msg)
			throws IOException, UnhandledMessageException {
		if (msg instanceof PlaceCardMessage) {
			PlaceCardMessage pcMsg = (PlaceCardMessage)msg;
			if (logic.placeCard(new Card(pcMsg.card))) {
				if (logic.isRunOver()) {
					StichInfoMessage siMsg = new StichInfoMessage();
					siMsg.laidCard = pcMsg.card;
					siMsg.player = sender.getEntity();
					siMsg.playerWhoWonStich = logic.getRunWinner().getEntity();
					broadcast(siMsg);
				} else {
					TurnInfoMessage tiMsg = new TurnInfoMessage();
					tiMsg.laidCard = pcMsg.card;
					tiMsg.player = sender.getEntity();
					broadcast(tiMsg);
				}

				if (logic.isRoundOver()) {
					EndOfRoundInfoMessage eorMsg = new EndOfRoundInfoMessage();
					boolean gameOver = logic.isGameOver();
					eorMsg.gameOver = gameOver;
					eorMsg.score = logic.getScore().getEntity();
					if (gameOver) {
						PlayerMovedToLobbyInfoMessage pmtlMsg = new PlayerMovedToLobbyInfoMessage();
						pmtlMsg.player = logic.getPlayer(Seat.SEAT1).getEntity();
						broadcast(pmtlMsg);
						pmtlMsg.player = logic.getPlayer(Seat.SEAT2).getEntity();
						broadcast(pmtlMsg);
						pmtlMsg.player = logic.getPlayer(Seat.SEAT3).getEntity();
						broadcast(pmtlMsg);
						pmtlMsg.player = logic.getPlayer(Seat.SEAT4).getEntity();
						broadcast(pmtlMsg);

						// start new game
						stateMachine.changeState(new LobbyState());
					} else {
						stateMachine.changeState(new StartRoundState());
					}
				} else {
					// Request next card
					act();
				}
			} else {
				WrongCardMessage wcMsg = new WrongCardMessage();
				wcMsg.wrongCard = pcMsg.card;
				send(wcMsg, sender);
			}
		} else if (msg instanceof ChosenWiisMessage) {
			//TODO: handle wiis stuff
		} else {
			throw(new UnhandledMessageException());
		}
	}
}
