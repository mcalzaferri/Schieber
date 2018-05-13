package server.states;

import java.io.IOException;
import ch.ntb.jass.common.entities.*;
import ch.ntb.jass.common.entities.ScoreEntity;
import ch.ntb.jass.common.proto.ToServerMessage;
import ch.ntb.jass.common.proto.player_messages.ChosenWiisMessage;
import ch.ntb.jass.common.proto.player_messages.PlaceCardMessage;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.WrongCardMessage;
import server.MoveStatus;
import server.exceptions.ClientErrorException;
import server.exceptions.UnhandledMessageException;
import shared.Card;
import shared.Player;

public class WaitForCardState extends GameState {
	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessage(Player sender, ToServerMessage msg)
			throws IOException, UnhandledMessageException, ClientErrorException {

		if (msg instanceof PlaceCardMessage) {
			if (sender != logic.getCurrentPlayer()) {
				throw(new ClientErrorException("It's not your turn!"));
			}

			PlaceCardMessage pcMsg = (PlaceCardMessage)msg;

			MoveStatus moveStatus = logic.placeCard(new Card(pcMsg.card));

			TurnInfoMessage tiMsg = null;

			switch(moveStatus) {
				case INVALID:
					throw (new ClientErrorException("You placed an invalid card!"));
				case NOTALLOWED:
					WrongCardMessage wcMsg = new WrongCardMessage();
					wcMsg.wrongCard = pcMsg.card;
					send(wcMsg, sender);
					return;
				case OK:
					tiMsg = new TurnInfoMessage();
					break;
				case RUNOVER:
					tiMsg = new StichInfoMessage();
					break;
				case ROUNDOVER:
					tiMsg = new EndOfRoundInfoMessage();
					break;
				case GAMEOVER:
					tiMsg = new EndOfGameInfoMessage();
					break;
				default:
					System.err.println("Unhandled move status");
			}

			tiMsg.laidCard = pcMsg.card;
			tiMsg.player = sender.getEntity();

			if (moveStatus.equals(MoveStatus.OK)) {
				broadcast(tiMsg);

				// Request next card
				act();
				return;
			}

			StichInfoMessage siMsg = (StichInfoMessage) tiMsg;
			siMsg.playerWhoWonStich = logic.getRunWinner().getEntity();

			if (moveStatus.equals(MoveStatus.RUNOVER)) {
				broadcast(siMsg);

				// Request next card
				act();
				return;
			}

			EndOfRoundInfoMessage eorMsg = (EndOfRoundInfoMessage) siMsg;
			eorMsg.score = new ScoreEntity();
			eorMsg.score.scores = logic.getScores();

			if (moveStatus.equals(MoveStatus.ROUNDOVER)) {
				broadcast(eorMsg);

				// start new round
				stateMachine.changeState(new StartRoundState());
				return;
			}

			EndOfGameInfoMessage eogMsg = (EndOfGameInfoMessage) eorMsg;
			eogMsg.teamThatWon = logic.getGameWinner();

			if (moveStatus.equals(MoveStatus.GAMEOVER)) {
				broadcast(eogMsg);

				// start new game
				stateMachine.changeState(new LobbyState());
				return;
			}

			System.err.println("Unhandled move status");
			return;
		}

		if (msg instanceof ChosenWiisMessage) {
			// TODO REV: i'd check for errors first and then do the rest
			// like:
//			if (!logic.inFirstRun()) {
//				throw(new ClientErrorException("No Weis after the first round has finished."));
//			}
//
//			if (logic.getDeclaredWeise().containsKey(sender)) {
//				throw(new ClientErrorException("You already declared your Weis. PlayerID: " + sender.getId()));
//			}
			// makes the code allot more readable IMO
			// if you disagree just delete this comment


			//Check if in first round.
			if (logic.inFirstRun()) {

				//Check if player does not declare Weise more than once.
				if (!logic.getDeclaredWeise().containsKey(sender)) {
					WeisEntity[] weise = ((ChosenWiisMessage) msg).wiis;

					//Check if Weise are valid.
					if (logic.weiseAreValid(sender, weise)) {
						logic.setDeclaredWeise(sender, weise);

						//Set score
						if (logic.getCardCounter() == 3) {
							logic.addWeisToScoreBoard();
						}

						WiisInfoMessage wiMsg = new WiisInfoMessage();
						wiMsg.player = sender.getEntity();
						wiMsg.wiis = weise;
						broadcast(wiMsg);
					} else {
						throw(new ClientErrorException("The Weis doesn't match your deck. PlayerID: " + sender.getId()));
					}
				} else {
					throw(new ClientErrorException("You already declared your Weis. PlayerID: " + sender.getId()));
				}
			} else {
				throw(new ClientErrorException("No Weis after the first round has finished."));
			}

			return;
		}

		throw(new UnhandledMessageException());
	}
}
