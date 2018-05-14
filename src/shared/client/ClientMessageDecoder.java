package shared.client;

import ch.ntb.jass.common.proto.Message;
import ch.ntb.jass.common.proto.server_info_messages.*;
import ch.ntb.jass.common.proto.server_messages.*;

/** This class is for decoding messages a client has received
 * Only use this class for decoding. This way changes in the protocol can be adopted very easily.
 *
 */
public interface ClientMessageDecoder {
	public static void decodeMessage(Message msg, ClientMessageDecoder decoder) {
		if(msg == null) {
			//do nothing
		}
		//server_info_messages
		if(msg instanceof ChosenTrumpInfoMessage)
			decoder.msgReceived((ChosenTrumpInfoMessage)msg);
		if(msg instanceof GameStartedInfoMessage)
			decoder.msgReceived((GameStartedInfoMessage)msg);
		if(msg instanceof NewRoundInfoMessage)
			decoder.msgReceived((NewRoundInfoMessage)msg);
		if(msg instanceof NewTurnInfoMessage)
			decoder.msgReceived((NewTurnInfoMessage)msg);
		if(msg instanceof PlayerChangedStateMessage)
			decoder.msgReceived((PlayerChangedStateMessage)msg);
		if(msg instanceof PlayerLeftLobbyInfoMessage)
			decoder.msgReceived((PlayerLeftLobbyInfoMessage)msg);
		if(msg instanceof PlayerMovedToLobbyInfoMessage)
			decoder.msgReceived((PlayerMovedToLobbyInfoMessage)msg);
		if(msg instanceof PlayerMovedToTableInfoMessage)
			decoder.msgReceived((PlayerMovedToTableInfoMessage)msg);
		if(msg instanceof TurnInfoMessage) {
			decoder.msgReceived((TurnInfoMessage)msg);
			if(msg instanceof StichInfoMessage) {
				decoder.msgReceived((StichInfoMessage)msg);
				if(msg instanceof EndOfRoundInfoMessage) {
					decoder.msgReceived((EndOfRoundInfoMessage)msg);
					if(msg instanceof EndOfGameInfoMessage)
						decoder.msgReceived((EndOfGameInfoMessage)msg);
				}
			}
		}
		if(msg instanceof WiisInfoMessage)
			decoder.msgReceived((WiisInfoMessage)msg);
		
		//server_messages
		if(msg instanceof ChooseTrumpMessage)
			decoder.msgReceived((ChooseTrumpMessage)msg);
		if(msg instanceof HandOutCardsMessage)
			decoder.msgReceived((HandOutCardsMessage)msg);
		if(msg instanceof LobbyStateMessage) {
			decoder.msgReceived((LobbyStateMessage)msg);
			if(msg instanceof GameStateMessage)
				decoder.msgReceived((GameStateMessage)msg);
		}
		if(msg instanceof ResultMessage)
			decoder.msgReceived((ResultMessage)msg);
		if(msg instanceof WrongCardMessage)
			decoder.msgReceived((WrongCardMessage)msg);
	}
	
	//server_info_messages
	void msgReceived(ChosenTrumpInfoMessage msg);
	void msgReceived(EndOfRoundInfoMessage msg);
	void msgReceived(EndOfGameInfoMessage msg);
	void msgReceived(GameStartedInfoMessage msg);
	void msgReceived(NewRoundInfoMessage msg);
	void msgReceived(NewTurnInfoMessage msg);
	void msgReceived(PlayerChangedStateMessage msg);
	void msgReceived(PlayerLeftLobbyInfoMessage msg);
	void msgReceived(PlayerMovedToLobbyInfoMessage msg);
	void msgReceived(PlayerMovedToTableInfoMessage msg);
	void msgReceived(StichInfoMessage msg);
	void msgReceived(TurnInfoMessage msg);
	void msgReceived(WiisInfoMessage msg);
	
	//server_messages
	void msgReceived(ChooseTrumpMessage msg);
	void msgReceived(GameStateMessage msg);
	void msgReceived(HandOutCardsMessage msg);
	void msgReceived(LobbyStateMessage msg);
	void msgReceived(ResultMessage msg);
	void msgReceived(WrongCardMessage msg);
}
