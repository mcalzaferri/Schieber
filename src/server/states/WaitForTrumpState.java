package server.states;

import java.io.IOException;

import ch.ntb.jass.common.entities.TrumpEntity;
import ch.ntb.jass.common.proto.*;
import ch.ntb.jass.common.proto.player_messages.*;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;
import server.exceptions.UnhandledMessageException;
import shared.Player;
import shared.Trump;

public class WaitForTrumpState extends GameState {

	private boolean schiebenAlreadyChosen;
	//TODO REV: use act method
	public WaitForTrumpState() throws IOException {
		schiebenAlreadyChosen = false;
		ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();
		ctMsg.canSchieben = true;
		send(ctMsg, logic.getRoundStarter());
	}

	/**
	 * @throws UnhandledMessageException
	 * @see GameState#handleMessage(Player, ToServerMessage)
	 */
	@Override
	public void handleMessage(Player sender, ToServerMessage msg) throws IOException, UnhandledMessageException{
		if(msg instanceof ChosenTrumpMessage) {
			//TODO REV: check first if this message is from the right sender

			//Sends request to choose trump back to the sender
			if(((ChosenTrumpMessage) msg).trump.equals(TrumpEntity.SCHIEBEN)) {
				ChooseTrumpMessage ctMsg = new ChooseTrumpMessage();
				ctMsg.canSchieben = false;

				if(schiebenAlreadyChosen) {
					//TODO REV: throw ClientErrorException
					send(ctMsg, sender);
					return;
				}

				//Send request to choose trump to the teammember
				send(ctMsg, logic.getPartner(sender));
				schiebenAlreadyChosen = true;
			} else {
				ChosenTrumpInfoMessage ctiMsg = new ChosenTrumpInfoMessage();
				ctiMsg.trump = ((ChosenTrumpMessage) msg).trump;
				Trump trump = Trump.getByEntity(((ChosenTrumpMessage) msg).trump);
				logic.setTrump(trump);
				broadcast(ctiMsg);
				System.out.println(sender + " chose "
						+ ((ChosenTrumpMessage) msg).trump + " as trump");
				stateMachine.changeState(new WaitForCardState());
			}
		} else {
			throw(new UnhandledMessageException());
		}
	}
}
