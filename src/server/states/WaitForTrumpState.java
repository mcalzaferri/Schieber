package server.states;

import java.io.IOException;

import ch.ntb.jass.common.entities.TrumpEntity;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import server.exceptions.ClientErrorException;
import shared.Player;
import shared.Trump;

public class WaitForTrumpState extends GameState {
	private boolean schiebenAlreadyChosen;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void act() throws IOException{
		schiebenAlreadyChosen = false;
		ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();
		ctMsg.canSchieben = true;
		send(ctMsg, logic.getRoundStarter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessage(Player sender, ChosenTrumpMessage msg) throws IOException, ClientErrorException {
		ChosenTrumpInfoMessage ctiMsg = new ChosenTrumpInfoMessage();
		ctiMsg.trump = msg.trump;

		if (!schiebenAlreadyChosen && (sender == logic.getRoundStarter())) {
			broadcast(ctiMsg);
			if(msg.trump.equals(TrumpEntity.SCHIEBEN)) {
				ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();
				ctMsg.canSchieben = false;
				//Send request to choose trump to the team partner
				schiebenAlreadyChosen = true;
				send(ctMsg, logic.getPartner(sender));
			} else {
				setTrump(msg, sender);
			}
		} else if (schiebenAlreadyChosen &&
				(sender == logic.getPartner(logic.getRoundStarter()))) {

			if(msg.trump.equals(TrumpEntity.SCHIEBEN)) {
				throw(new ClientErrorException("You can't Schieben more than once!"));
			} else {
				broadcast(ctiMsg);
				setTrump(msg, sender);
			}
		} else {
			throw(new ClientErrorException("It's not your turn to choose the trump!"));
		}
	}

	private void setTrump(ChosenTrumpMessage msg, Player sender) throws IOException {
		Trump trump = Trump.getByEntity((msg).trump);
		logic.setTrump(trump);
		System.out.println(sender + " chose " + msg.trump + " as trump");
		stateMachine.changeState(new WaitForCardState());
	}
}
