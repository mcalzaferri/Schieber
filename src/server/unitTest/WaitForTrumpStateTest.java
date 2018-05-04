package server.unitTest;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.ntb.jass.common.proto.player_messages.ChosenTrumpMessage;
import ch.ntb.jass.common.entities.*;
import server.states.GameState;
import server.states.StateMachine;
import server.states.WaitForTrumpState;
import server.*;
import shared.Communication;
import shared.Player;

public class WaitForTrumpStateTest {
	
	@Test
	public void handleMessage_RoundStarterSetEichel_TrumpSet() throws Exception{
		
		//TODO: Doesn't work yet, need to discuss if mockito is the way to go.
		
//		//Arrange				
//		ServerTest testServer = new ServerTest();
//		testServer.setUp();
//		
//		GameLogic logic = new GameLogic();
//		Communication com = new Communication();
//		StateMachine stateMachine = new StateMachine();
//		GameState.init(stateMachine, com, logic);
//		
//		WaitForTrumpState testee = new WaitForTrumpState();		
//		stateMachine.changeState(testee);
//		
//		PlayerEntity entity = new PlayerEntity();
//		entity.name = "testPlayer";
//		entity.seat = SeatEntity.SEAT1;		
//		Player player = new Player(entity);
//		
//		ChosenTrumpMessage toServerMessage = new ChosenTrumpMessage();
//		toServerMessage.trump = TrumpEntity.EICHEL;
//				
//		//Act
//		testee.handleMessage(player, toServerMessage);
//		
//		//Assert
//		assertEquals("1", "1");
//		fail();
	}

}
