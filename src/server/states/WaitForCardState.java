package server.states;

import java.io.IOException;
import ch.ntb.jass.common.entities.*;
import ch.ntb.jass.common.entities.ScoreEntity;
import ch.ntb.jass.common.proto.player_messages.ChosenWiisMessage;
import ch.ntb.jass.common.proto.player_messages.PlaceCardMessage;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.WrongCardMessage;
import server.MoveStatus;
import server.exceptions.ClientErrorException;
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
		com.broadcast(ntMsg);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessage(Player sender, PlaceCardMessage msg) throws ClientErrorException, IOException {
		if (sender != logic.getCurrentPlayer()) {
			throw (new ClientErrorException("It's not your turn!"));
		}

		MoveStatus moveStatus = logic.placeCard(new Card(msg.card));

		TurnInfoMessage tiMsg = null;

		switch (moveStatus) {
			case INVALID:
				WrongCardMessage wcMsg = new WrongCardMessage();
				wcMsg.wrongCard = msg.card;
				com.send(wcMsg, sender);
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

		tiMsg.laidCard = msg.card;
		tiMsg.player = sender.getEntity();

		if (moveStatus.equals(MoveStatus.OK)) {
			com.broadcast(tiMsg);

			// Request next card
			act();
			return;
		}

		StichInfoMessage siMsg = (StichInfoMessage) tiMsg;
		siMsg.playerWhoWonStich = logic.getRunWinner().getEntity();

		if (moveStatus.equals(MoveStatus.RUNOVER)) {
			com.broadcast(siMsg);

			// Request next card
			act();
			return;
		}

		EndOfRoundInfoMessage eorMsg = (EndOfRoundInfoMessage) siMsg;
		eorMsg.score = new ScoreEntity();
		eorMsg.score.scores = logic.getScores();

		if (moveStatus.equals(MoveStatus.ROUNDOVER)) {
			com.broadcast(eorMsg);

			// start new round
			stateMachine.changeState(new StartRoundState());
			return;
		}

		EndOfGameInfoMessage eogMsg = (EndOfGameInfoMessage) eorMsg;
		eogMsg.teamThatWon = logic.getGameWinner();

		if (moveStatus.equals(MoveStatus.GAMEOVER)) {
			com.broadcast(eogMsg);

			// start new game
			stateMachine.changeState(new LobbyState());
			return;
		}

		System.err.println("Unhandled move status");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessage(Player sender, ChosenWiisMessage msg) throws IOException, ClientErrorException {
		//Check if in first round.
		if (!logic.inFirstRun()) {
			throw(new ClientErrorException("No Weis after the first round has finished."));
		}

		//Check if player does not declare Weise more than once.
		if (logic.getDeclaredWeise().containsKey(sender)) {
			throw(new ClientErrorException("You already declared your Weis. PlayerID: " + sender.getId()));
		}
		 			
		WeisEntity[] weise = ((ChosenWiisMessage) msg).wiis;
		 
		//Check if Weise are valid.
		if (!logic.weiseAreValid(sender, weise)) {
			throw(new ClientErrorException("The Weis doesn't match your deck. PlayerID: " + sender.getId()));				
		}

 		logic.setDeclaredWeise(sender, weise);
 
		//Set score
		if (logic.getCardCounter() == 3) {
			logic.addWeisToScoreBoard();
		}
 
		WiisInfoMessage wiMsg = new WiisInfoMessage();
		wiMsg.player = sender.getEntity();
		wiMsg.wiis = weise;
		com.broadcast(wiMsg);
	}
}
