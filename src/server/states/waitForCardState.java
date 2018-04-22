package server.states;

import java.io.IOException;

import ch.ntb.jass.common.entities.SeatEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.ChosenWiisMessage;
import ch.ntb.jass.common.proto.player_messages.PlaceCardMessage;
import ch.ntb.jass.common.proto.server_info_messages.EndOfRoundInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.NewTurnInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.StichInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.TurnInfoMessage;
import ch.ntb.jass.common.proto.server_messages.ResultMessage;
import ch.ntb.jass.common.proto.server_messages.WrongCardMessage;
import shared.Card;
import shared.Player;

public class waitForCardState extends GameState {
	/**
	 * @see GameState#act()
	 */
	@Override
	public void act() throws IOException {
		// Request card from player
		NewTurnInfoMessage ntMsg = new NewTurnInfoMessage();
		ntMsg.nextPlayer = logic.nextPlayer();
		ntMsg.selectWeis = logic.inFirstRun();
		broadcast(ntMsg);
	}

	/**
	 * @see GameState#handleMessage(Player, ToServerMessage)
	 */
	@Override
	public boolean handleMessage(Player sender, ToServerMessage msg)
			throws IOException {
		if (msg instanceof PlaceCardMessage) {
			PlaceCardMessage pcMsg = (PlaceCardMessage)msg;
			if (logic.placeCard(new Card(pcMsg.card))) {
				sendResultMsg(ResultMessage.Code.OK, "", sender);
				if (logic.isRunOver()) {
					StichInfoMessage siMsg = new StichInfoMessage();
					siMsg.laidCard = pcMsg.card;
					siMsg.player = sender;
					siMsg.playerWhoWonStich = logic.getRunWinner();
					broadcast(siMsg);
				} else {
					TurnInfoMessage tiMsg = new TurnInfoMessage();
					tiMsg.laidCard = pcMsg.card;
					tiMsg.player = sender;
					broadcast(tiMsg);
				}

				if (logic.isRoundOver()) {
					EndOfRoundInfoMessage eorMsg = new EndOfRoundInfoMessage();
					boolean gameOver = logic.isGameOver();
					eorMsg.gameOver = gameOver;
					eorMsg.score = logic.getScore();
					if (gameOver) {
						PlayerMovedToLobbyInfoMessage pmtlMsg = new PlayerMovedToLobbyInfoMessage();
						pmtlMsg.player = logic.getPlayer(SeatEntity.SEAT1);
						broadcast(pmtlMsg);
						pmtlMsg.player = logic.getPlayer(SeatEntity.SEAT2);
						broadcast(pmtlMsg);
						pmtlMsg.player = logic.getPlayer(SeatEntity.SEAT3);
						broadcast(pmtlMsg);
						pmtlMsg.player = logic.getPlayer(SeatEntity.SEAT4);
						broadcast(pmtlMsg);

						// start new game
						logic.init();
						stateMachine.changeState(new LobbyState());
					} else {
						stateMachine.changeState(new StartRoundState());
					}
				} else {
					// Request next card
					act();
				}
			} else {
				sendResultMsg(ResultMessage.Code.FAILURE,
						"You placed an invalid card.", sender);
				WrongCardMessage wcMsg = new WrongCardMessage();
				wcMsg.wrongCard = pcMsg.card;
				send(wcMsg, sender);
			}
		} else if (msg instanceof ChosenWiisMessage) {
			//TODO: handle wiis stuff
		}
		return true;
	}
}
