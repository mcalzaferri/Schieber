package server.states;

import java.io.IOException;

import ch.ntb.jass.common.entities.ScoreEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.ChosenWiisMessage;
import ch.ntb.jass.common.proto.player_messages.PlaceCardMessage;
import ch.ntb.jass.common.proto.server_info_messages.EndOfRoundInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.NewTurnInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.PlayerMovedToLobbyInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.StichInfoMessage;
import ch.ntb.jass.common.proto.server_info_messages.TurnInfoMessage;
import ch.ntb.jass.common.proto.server_messages.WrongCardMessage;
import server.exceptions.ClientErrorException;
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
		ntMsg.nextPlayer = logic.getCurrentPlayer().getEntity();
		ntMsg.selectWeis = logic.inFirstRun();
		broadcast(ntMsg);
	}

	/**
	 * @throws UnhandledMessageException
	 * @throws ClientErrorException
	 * @see GameState#handleMessage(Player, ToServerMessage)
	 */
	@Override
	public void handleMessage(Player sender, ToServerMessage msg)
			throws IOException, UnhandledMessageException, ClientErrorException {
		if (msg instanceof PlaceCardMessage) {
			if (sender != logic.getCurrentPlayer()) {
				throw(new ClientErrorException("It's not your turn!"));
			}

			PlaceCardMessage pcMsg = (PlaceCardMessage)msg;

			switch(logic.placeCard(new Card(pcMsg.card))) {
			case NOTALLOWED: {
				WrongCardMessage wcMsg = new WrongCardMessage();
				wcMsg.wrongCard = pcMsg.card;
				send(wcMsg, sender);
				break;
			}
			case OK: {
				TurnInfoMessage tiMsg = new TurnInfoMessage();
				tiMsg.laidCard = pcMsg.card;
				tiMsg.player = sender.getEntity();
				broadcast(tiMsg);
				// Request next card
				act();
				break;
			}
			case RUNOVER: {
				StichInfoMessage siMsg = new StichInfoMessage();
				siMsg.laidCard = pcMsg.card;
				siMsg.player = sender.getEntity();
				siMsg.playerWhoWonStich = logic.getRunWinner().getEntity();
				broadcast(siMsg);
				// Request next card
				act();
				break;
			}
			case ROUNDOVER: {
				EndOfRoundInfoMessage eorMsg = new EndOfRoundInfoMessage();
				eorMsg.gameOver = false;
				ScoreEntity score = new ScoreEntity();
				score.scores = logic.getScores();
				eorMsg.score = score;
				broadcast(eorMsg);
				stateMachine.changeState(new StartRoundState());
				break;
			}
			case GAMEOVER: {
				EndOfRoundInfoMessage eorMsg = new EndOfRoundInfoMessage();
				eorMsg.gameOver = true;
				ScoreEntity score = new ScoreEntity();
				score.scores = logic.getScores();
				eorMsg.score = score;
				stateMachine.changeState(new StartRoundState());

				Player p;
				PlayerMovedToLobbyInfoMessage pmtlMsg = new PlayerMovedToLobbyInfoMessage();

				// move all players to lobby
				for (Seat s : Seat.values()) {
					p = logic.getPlayer(s);
					p.setSeat(Seat.NOTATTABLE);
					pmtlMsg.player = p.getEntity();
					broadcast(pmtlMsg);
				}

				// start new game
				stateMachine.changeState(new LobbyState());
				break;
			}
			case INVALID: {
				throw(new ClientErrorException("You placed an invalid card!"));
			}}
		} else if (msg instanceof ChosenWiisMessage) {
			//TODO: handle wiis stuff
		} else {
			throw(new UnhandledMessageException());
		}
	}
}
