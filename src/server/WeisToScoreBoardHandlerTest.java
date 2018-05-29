package server;

import static org.junit.Assert.assertEquals;
import java.util.LinkedHashMap;
import org.junit.Test;
import ch.ntb.jass.common.entities.*;
import shared.Player;
import shared.Trump;

/**
 * Unit tests for the WeisToScoreBoardHandler.
 * 
 * Weisrules: 	Only the highest Weis of each player will be assessed.
 * 				1. Player with the highest Weis value gets the points.
 * 				2. Player with the highest amount of cards gets the points.
 * 				3. Player with Weis of trump gets the points.
 * 				4. First player gets the points.
 */
public class WeisToScoreBoardHandlerTest {
	
	@Test
	public void highestWeisCounts_Player1HasOneWeis_Team1WinsWithScore20() throws Exception {		
		/*
		 * testPlayer1 
		 * 
		 * Dreiblatt
		 * Eichel
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity1 = new PlayerEntity();
		testPlayerEntity1.name = "player1";
		testPlayerEntity1.seat = SeatEntity.SEAT1;
		testPlayerEntity1.isBot = true;
		testPlayerEntity1.id = 1;
		Player testPlayer1 = new Player(testPlayerEntity1);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity11 = WeisTypeEntity.DREIBLATT;
		CardColorEntity cardColorEntity11 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity11 = CardValueEntity.KOENIG;		
		CardEntity cardEntity11 = new CardEntity();
		cardEntity11.color = cardColorEntity11;
		cardEntity11.value = cardValueEntity11;
		WeisEntity weisEntity11 = new WeisEntity();
		weisEntity11.type = weisTypeEntity11;
		weisEntity11.originCard = cardEntity11;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray11 = new WeisEntity[1]; 
		weisEntityArray11[0] = weisEntity11;
		
		//Arrange testWeise
		LinkedHashMap<Player, WeisEntity[]> testWeise = new LinkedHashMap<>();
		testWeise.put(testPlayer1, weisEntityArray11);
		//Arrange trump
		Trump trump = Trump.ROSE;
		//Arrange testee
		WeisToScoreBoardHandler testee = new WeisToScoreBoardHandler(testWeise, trump);
		
		
		//Act testee
		testee.execute();
		
		
		//Assert testee
		assertEquals(1, testee.getTeamId());
		assertEquals(20, testee.getWeisScore());
	}
	
	@Test
	public void highestWeisCounts_Player2HasOneWeis_Team2WinsWithScore20() throws Exception {		
		/*
		 * testPlayer2
		 * 
		 * Dreiblatt
		 * Eichel
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity2 = new PlayerEntity();
		testPlayerEntity2.name = "player2";
		testPlayerEntity2.seat = SeatEntity.SEAT2;
		testPlayerEntity2.isBot = true;
		testPlayerEntity2.id = 1;
		Player testPlayer2 = new Player(testPlayerEntity2);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity21 = WeisTypeEntity.DREIBLATT;
		CardColorEntity cardColorEntity21 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity21 = CardValueEntity.KOENIG;		
		CardEntity cardEntity21 = new CardEntity();
		cardEntity21.color = cardColorEntity21;
		cardEntity21.value = cardValueEntity21;
		WeisEntity weisEntity21 = new WeisEntity();
		weisEntity21.type = weisTypeEntity21;
		weisEntity21.originCard = cardEntity21;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray21 = new WeisEntity[1]; 
		weisEntityArray21[0] = weisEntity21;
		
		//Arrange testWeise
		LinkedHashMap<Player, WeisEntity[]> testWeise = new LinkedHashMap<>();
		testWeise.put(testPlayer2, weisEntityArray21);
		//Arrange trump
		Trump trump = Trump.ROSE;
		//Arrange testee
		WeisToScoreBoardHandler testee = new WeisToScoreBoardHandler(testWeise, trump);
		
		
		//Act testee
		testee.execute();
		
		
		//Assert testee
		assertEquals(2, testee.getTeamId());
		assertEquals(20, testee.getWeisScore());
	}
	
	@Test
	public void amountOfCardsCounts_EachPlayerHasOneWeisWithDifferentWeisValues_Player3HasWinningWeisTeam1WinsWithScore270() throws Exception {		
		/*
		 * testPlayer1 
		 * 
		 * Dreiblatt
		 * Eichel
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity1 = new PlayerEntity();
		testPlayerEntity1.name = "player1";
		testPlayerEntity1.seat = SeatEntity.SEAT1;
		testPlayerEntity1.isBot = true;
		testPlayerEntity1.id = 1;
		Player testPlayer1 = new Player(testPlayerEntity1);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity11 = WeisTypeEntity.DREIBLATT;
		CardColorEntity cardColorEntity11 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity11 = CardValueEntity.KOENIG;		
		CardEntity cardEntity11 = new CardEntity();
		cardEntity11.color = cardColorEntity11;
		cardEntity11.value = cardValueEntity11;
		WeisEntity weisEntity11 = new WeisEntity();
		weisEntity11.type = weisTypeEntity11;
		weisEntity11.originCard = cardEntity11;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray11 = new WeisEntity[1]; 
		weisEntityArray11[0] = weisEntity11;
		
		/*
		 * testPlayer2 
		 * 
		 * Vierblatt
		 * Eichel
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity2 = new PlayerEntity();
		testPlayerEntity2.name = "player2";
		testPlayerEntity2.seat = SeatEntity.SEAT2;
		testPlayerEntity2.isBot = true;
		testPlayerEntity2.id = 2;
		Player testPlayer2 = new Player(testPlayerEntity2);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity21 = WeisTypeEntity.VIERBLATT;
		CardColorEntity cardColorEntity21 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity21 = CardValueEntity.KOENIG;		
		CardEntity cardEntity21 = new CardEntity();
		cardEntity21.color = cardColorEntity21;
		cardEntity21.value = cardValueEntity21;
		WeisEntity weisEntity21 = new WeisEntity();
		weisEntity21.type = weisTypeEntity21;
		weisEntity21.originCard = cardEntity21;
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray21 = new WeisEntity[1]; 
		weisEntityArray21[0] = weisEntity21;
		
		/*
		 * testPlayer3		--	Winning Player
		 * 
		 * Achtblatt
		 * Eichel
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity3 = new PlayerEntity();
		testPlayerEntity3.name = "player3";
		testPlayerEntity3.seat = SeatEntity.SEAT3;
		testPlayerEntity3.isBot = true;
		testPlayerEntity3.id = 3;
		Player testPlayer3 = new Player(testPlayerEntity3);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity31 = WeisTypeEntity.ACHTBLATT;
		CardColorEntity cardColorEntity31 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity31 = CardValueEntity.KOENIG;		
		CardEntity cardEntity31 = new CardEntity();
		cardEntity31.color = cardColorEntity31;
		cardEntity31.value = cardValueEntity31;
		WeisEntity weisEntity31 = new WeisEntity();
		weisEntity31.type = weisTypeEntity31;
		weisEntity31.originCard = cardEntity31;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray31 = new WeisEntity[1]; 
		weisEntityArray31[0] = weisEntity31;
		
		/*
		 * testPlayer4 
		 * 
		 * Fünfblatt
		 * Eichel
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity4 = new PlayerEntity();
		testPlayerEntity4.name = "player4";
		testPlayerEntity4.seat = SeatEntity.SEAT4;
		testPlayerEntity4.isBot = true;
		testPlayerEntity4.id = 4;
		Player testPlayer4 = new Player(testPlayerEntity4);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity41 = WeisTypeEntity.FUENFBLATT;
		CardColorEntity cardColorEntity41 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity41 = CardValueEntity.KOENIG;		
		CardEntity cardEntity41 = new CardEntity();
		cardEntity41.color = cardColorEntity41;
		cardEntity41.value = cardValueEntity41;
		WeisEntity weisEntity41 = new WeisEntity();
		weisEntity41.type = weisTypeEntity41;
		weisEntity41.originCard = cardEntity41;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray41 = new WeisEntity[1]; 
		weisEntityArray41[0] = weisEntity41;
		
		//Arrange testWeise
		LinkedHashMap<Player, WeisEntity[]> testWeise = new LinkedHashMap<>();
		testWeise.put(testPlayer1, weisEntityArray11);
		testWeise.put(testPlayer2, weisEntityArray21);
		testWeise.put(testPlayer3, weisEntityArray31);
		testWeise.put(testPlayer4, weisEntityArray41);
		//Arrange trump
		Trump trump = Trump.ROSE;
		//Arrange testee
		WeisToScoreBoardHandler testee = new WeisToScoreBoardHandler(testWeise, trump);
		
		
		//Act testee
		testee.execute();
		
		
		//Assert testee
		assertEquals(1, testee.getTeamId());
		assertEquals(270, testee.getWeisScore());
	}
	
	@Test
	public void highestWeisCounts_EachPlayerHasOneWeisWithSameValueAndDifferentAmountOfCards_Player3HasWinningWeisTeam1WinsWithScore200() throws Exception {
		/*
		 * testPlayer1 
		 * 
		 * Viergleiche
		 * Eichel
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity1 = new PlayerEntity();
		testPlayerEntity1.name = "player1";
		testPlayerEntity1.seat = SeatEntity.SEAT1;
		testPlayerEntity1.isBot = true;
		testPlayerEntity1.id = 1;
		Player testPlayer1 = new Player(testPlayerEntity1);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity11 = WeisTypeEntity.VIERGLEICHE;
		CardColorEntity cardColorEntity11 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity11 = CardValueEntity.KOENIG;		
		CardEntity cardEntity11 = new CardEntity();
		cardEntity11.color = cardColorEntity11;
		cardEntity11.value = cardValueEntity11;
		WeisEntity weisEntity11 = new WeisEntity();
		weisEntity11.type = weisTypeEntity11;
		weisEntity11.originCard = cardEntity11;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray11 = new WeisEntity[1]; 
		weisEntityArray11[0] = weisEntity11;
		
		/*
		 * testPlayer2 
		 * 
		 * Viergleiche
		 * Schilte
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity2 = new PlayerEntity();
		testPlayerEntity2.name = "player2";
		testPlayerEntity2.seat = SeatEntity.SEAT2;
		testPlayerEntity2.isBot = true;
		testPlayerEntity2.id = 2;
		Player testPlayer2 = new Player(testPlayerEntity2);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity21 = WeisTypeEntity.VIERGLEICHE;
		CardColorEntity cardColorEntity21 = CardColorEntity.SCHILTE; 
		CardValueEntity cardValueEntity21 = CardValueEntity.KOENIG;		
		CardEntity cardEntity21 = new CardEntity();
		cardEntity21.color = cardColorEntity21;
		cardEntity21.value = cardValueEntity21;
		WeisEntity weisEntity21 = new WeisEntity();
		weisEntity21.type = weisTypeEntity21;
		weisEntity21.originCard = cardEntity21;
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray21 = new WeisEntity[1]; 
		weisEntityArray21[0] = weisEntity21;
		
		/*
		 * testPlayer3 		--	Winning Player
		 * 
		 * Fünfblatt
		 * Schilte
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity3 = new PlayerEntity();
		testPlayerEntity3.name = "player3";
		testPlayerEntity3.seat = SeatEntity.SEAT3;
		testPlayerEntity3.isBot = true;
		testPlayerEntity3.id = 3;
		Player testPlayer3 = new Player(testPlayerEntity3);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity31 = WeisTypeEntity.FUENFBLATT;
		CardColorEntity cardColorEntity31 = CardColorEntity.SCHILTE; 
		CardValueEntity cardValueEntity31 = CardValueEntity.KOENIG;		
		CardEntity cardEntity31 = new CardEntity();
		cardEntity31.color = cardColorEntity31;
		cardEntity31.value = cardValueEntity31;
		WeisEntity weisEntity31 = new WeisEntity();
		weisEntity31.type = weisTypeEntity31;
		weisEntity31.originCard = cardEntity31;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray31 = new WeisEntity[1]; 
		weisEntityArray31[0] = weisEntity31;
		
		/*
		 * testPlayer4 
		 * 
		 * Viergleiche
		 * Schelle
		 * Koenig
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity4 = new PlayerEntity();
		testPlayerEntity4.name = "player4";
		testPlayerEntity4.seat = SeatEntity.SEAT4;
		testPlayerEntity4.isBot = true;
		testPlayerEntity4.id = 4;
		Player testPlayer4 = new Player(testPlayerEntity4);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity41 = WeisTypeEntity.VIERGLEICHE;
		CardColorEntity cardColorEntity41 = CardColorEntity.SCHELLE; 
		CardValueEntity cardValueEntity41 = CardValueEntity.KOENIG;		
		CardEntity cardEntity41 = new CardEntity();
		cardEntity41.color = cardColorEntity41;
		cardEntity41.value = cardValueEntity41;
		WeisEntity weisEntity41 = new WeisEntity();
		weisEntity41.type = weisTypeEntity41;
		weisEntity41.originCard = cardEntity41;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray41 = new WeisEntity[1]; 
		weisEntityArray41[0] = weisEntity41;
		
		//Arrange testWeise
		LinkedHashMap<Player, WeisEntity[]> testWeise = new LinkedHashMap<>();
		testWeise.put(testPlayer1, weisEntityArray11);
		testWeise.put(testPlayer2, weisEntityArray21);
		testWeise.put(testPlayer3, weisEntityArray31);
		testWeise.put(testPlayer4, weisEntityArray41);
		//Arrange trump
		Trump trump = Trump.ROSE;
		//Arrange testee
		WeisToScoreBoardHandler testee = new WeisToScoreBoardHandler(testWeise, trump);
		
		
		//Act testee
		testee.execute();
		
		
		//Assert testee
		assertEquals(1, testee.getTeamId());
		assertEquals(200, testee.getWeisScore());
	}
	
	@Test
	public void trumpCounts_EachPlayerHasOneWeisOnlyOneHasTrump_Player2HasWinningWeisTeam2WinsWithScore400() throws Exception {
		/*
		 * testPlayer1 
		 * 
		 * Siebenblatt
		 * Eichel
		 * Ober
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity1 = new PlayerEntity();
		testPlayerEntity1.name = "player1";
		testPlayerEntity1.seat = SeatEntity.SEAT1;
		testPlayerEntity1.isBot = true;
		testPlayerEntity1.id = 1;
		Player testPlayer1 = new Player(testPlayerEntity1);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity11 = WeisTypeEntity.SIEBENBLATT;
		CardColorEntity cardColorEntity11 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity11 = CardValueEntity.OBER;		
		CardEntity cardEntity11 = new CardEntity();
		cardEntity11.color = cardColorEntity11;
		cardEntity11.value = cardValueEntity11;
		WeisEntity weisEntity11 = new WeisEntity();
		weisEntity11.type = weisTypeEntity11;
		weisEntity11.originCard = cardEntity11;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray11 = new WeisEntity[1]; 
		weisEntityArray11[0] = weisEntity11;
		
		/*
		 * testPlayer2 		-- Winning Player
		 * 
		 * Siebenblatt
		 * Rose				-- is Trumpf
		 * Ober
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity2 = new PlayerEntity();
		testPlayerEntity2.name = "player2";
		testPlayerEntity2.seat = SeatEntity.SEAT2;
		testPlayerEntity2.isBot = true;
		testPlayerEntity2.id = 2;
		Player testPlayer2 = new Player(testPlayerEntity2);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity21 = WeisTypeEntity.SIEBENBLATT;
		CardColorEntity cardColorEntity21 = CardColorEntity.ROSE; 
		CardValueEntity cardValueEntity21 = CardValueEntity.OBER;		
		CardEntity cardEntity21 = new CardEntity();
		cardEntity21.color = cardColorEntity21;
		cardEntity21.value = cardValueEntity21;
		WeisEntity weisEntity21 = new WeisEntity();
		weisEntity21.type = weisTypeEntity21;
		weisEntity21.originCard = cardEntity21;
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray21 = new WeisEntity[1]; 
		weisEntityArray21[0] = weisEntity21;
		
		/*
		 * testPlayer3 
		 * 
		 * Siebenblatt
		 * Schilte
		 * Ober
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity3 = new PlayerEntity();
		testPlayerEntity3.name = "player3";
		testPlayerEntity3.seat = SeatEntity.SEAT3;
		testPlayerEntity3.isBot = true;
		testPlayerEntity3.id = 3;
		Player testPlayer3 = new Player(testPlayerEntity3);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity31 = WeisTypeEntity.SIEBENBLATT;
		CardColorEntity cardColorEntity31 = CardColorEntity.SCHILTE; 
		CardValueEntity cardValueEntity31 = CardValueEntity.OBER;		
		CardEntity cardEntity31 = new CardEntity();
		cardEntity31.color = cardColorEntity31;
		cardEntity31.value = cardValueEntity31;
		WeisEntity weisEntity31 = new WeisEntity();
		weisEntity31.type = weisTypeEntity31;
		weisEntity31.originCard = cardEntity31;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray31 = new WeisEntity[1]; 
		weisEntityArray31[0] = weisEntity31;
		
		/*
		 * testPlayer4 
		 * 
		 * Siebenblatt
		 * Schelle
		 * Ober
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity4 = new PlayerEntity();
		testPlayerEntity4.name = "player4";
		testPlayerEntity4.seat = SeatEntity.SEAT4;
		testPlayerEntity4.isBot = true;
		testPlayerEntity4.id = 4;
		Player testPlayer4 = new Player(testPlayerEntity4);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity41 = WeisTypeEntity.SIEBENBLATT;
		CardColorEntity cardColorEntity41 = CardColorEntity.SCHELLE; 
		CardValueEntity cardValueEntity41 = CardValueEntity.OBER;		
		CardEntity cardEntity41 = new CardEntity();
		cardEntity41.color = cardColorEntity41;
		cardEntity41.value = cardValueEntity41;
		WeisEntity weisEntity41 = new WeisEntity();
		weisEntity41.type = weisTypeEntity41;
		weisEntity41.originCard = cardEntity41;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray41 = new WeisEntity[1]; 
		weisEntityArray41[0] = weisEntity41;
		
		//Arrange testWeise
		LinkedHashMap<Player, WeisEntity[]> testWeise = new LinkedHashMap<>();
		testWeise.put(testPlayer1, weisEntityArray11);
		testWeise.put(testPlayer2, weisEntityArray21);
		testWeise.put(testPlayer3, weisEntityArray31);
		testWeise.put(testPlayer4, weisEntityArray41);
		//Arrange trump
		Trump trump = Trump.ROSE;
		//Arrange testee
		WeisToScoreBoardHandler testee = new WeisToScoreBoardHandler(testWeise, trump);
		
		
		//Act testee
		testee.execute();
		
		
		//Assert testee
		assertEquals(2, testee.getTeamId());
		assertEquals(400, testee.getWeisScore());
	}
	
	@Test
	public void firstOneGetsWeis_EachPlayerHasOneWeisEveryoneSameWeis_Player1HasWinningWeisTeam1WinsWithScore200() throws Exception {
		/*
		 * testPlayer1 		-- Winning Player
		 * 
		 * Viergleiche
		 * Eichel
		 * Acht
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity1 = new PlayerEntity();
		testPlayerEntity1.name = "player1";
		testPlayerEntity1.seat = SeatEntity.SEAT1;
		testPlayerEntity1.isBot = true;
		testPlayerEntity1.id = 1;
		Player testPlayer1 = new Player(testPlayerEntity1);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity11 = WeisTypeEntity.VIERGLEICHE;
		CardColorEntity cardColorEntity11 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity11 = CardValueEntity.ACHT;		
		CardEntity cardEntity11 = new CardEntity();
		cardEntity11.color = cardColorEntity11;
		cardEntity11.value = cardValueEntity11;
		WeisEntity weisEntity11 = new WeisEntity();
		weisEntity11.type = weisTypeEntity11;
		weisEntity11.originCard = cardEntity11;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray11 = new WeisEntity[1]; 
		weisEntityArray11[0] = weisEntity11;
		
		/*
		 * testPlayer2 
		 * 
		 * Viergleiche
		 * Eichel
		 * Acht
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity2 = new PlayerEntity();
		testPlayerEntity2.name = "player2";
		testPlayerEntity2.seat = SeatEntity.SEAT2;
		testPlayerEntity2.isBot = true;
		testPlayerEntity2.id = 2;
		Player testPlayer2 = new Player(testPlayerEntity2);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity21 = WeisTypeEntity.VIERGLEICHE;
		CardColorEntity cardColorEntity21 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity21 = CardValueEntity.ACHT;		
		CardEntity cardEntity21 = new CardEntity();
		cardEntity21.color = cardColorEntity21;
		cardEntity21.value = cardValueEntity21;
		WeisEntity weisEntity21 = new WeisEntity();
		weisEntity21.type = weisTypeEntity21;
		weisEntity21.originCard = cardEntity21;
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray21 = new WeisEntity[1]; 
		weisEntityArray21[0] = weisEntity21;
		
		/*
		 * testPlayer3 
		 * 
		 * Viergleiche
		 * Eichel
		 * Acht
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity3 = new PlayerEntity();
		testPlayerEntity3.name = "player3";
		testPlayerEntity3.seat = SeatEntity.SEAT3;
		testPlayerEntity3.isBot = true;
		testPlayerEntity3.id = 3;
		Player testPlayer3 = new Player(testPlayerEntity3);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity31 = WeisTypeEntity.VIERGLEICHE;
		CardColorEntity cardColorEntity31 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity31 = CardValueEntity.ACHT;		
		CardEntity cardEntity31 = new CardEntity();
		cardEntity31.color = cardColorEntity31;
		cardEntity31.value = cardValueEntity31;
		WeisEntity weisEntity31 = new WeisEntity();
		weisEntity31.type = weisTypeEntity31;
		weisEntity31.originCard = cardEntity31;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray31 = new WeisEntity[1]; 
		weisEntityArray31[0] = weisEntity31;
		
		/*
		 * testPlayer4 
		 * 
		 * Viergleiche
		 * Eichel
		 * Acht
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity4 = new PlayerEntity();
		testPlayerEntity4.name = "player4";
		testPlayerEntity4.seat = SeatEntity.SEAT4;
		testPlayerEntity4.isBot = true;
		testPlayerEntity4.id = 4;
		Player testPlayer4 = new Player(testPlayerEntity4);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity41 = WeisTypeEntity.VIERGLEICHE;
		CardColorEntity cardColorEntity41 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity41 = CardValueEntity.ACHT;		
		CardEntity cardEntity41 = new CardEntity();
		cardEntity41.color = cardColorEntity41;
		cardEntity41.value = cardValueEntity41;
		WeisEntity weisEntity41 = new WeisEntity();
		weisEntity41.type = weisTypeEntity41;
		weisEntity41.originCard = cardEntity41;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray41 = new WeisEntity[1]; 
		weisEntityArray41[0] = weisEntity41;
		
		//Arrange testWeise
		LinkedHashMap<Player, WeisEntity[]> testWeise = new LinkedHashMap<>();
		testWeise.put(testPlayer1, weisEntityArray11);
		testWeise.put(testPlayer2, weisEntityArray21);
		testWeise.put(testPlayer3, weisEntityArray31);
		testWeise.put(testPlayer4, weisEntityArray41);
		//Arrange trump
		Trump trump = Trump.ROSE;
		//Arrange testee
		WeisToScoreBoardHandler testee = new WeisToScoreBoardHandler(testWeise, trump);
		
		
		//Act testee
		testee.execute();
		
		
		//Assert testee
		assertEquals(1, testee.getTeamId());
		assertEquals(200, testee.getWeisScore());
	}
	
	@Test
	public void allOverTesting_PlayerHasAmongOthersMultipleWeisTrumpDecidesWhoWins_Team1WinsWithScore200() throws Exception {
		/*
		 * testPlayer1 
		 * 
		 * Dreiblatt
		 * Eichel
		 * Acht
		 * 
		 * Fünfblatt
		 * Schelle
		 * Zehn
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity1 = new PlayerEntity();
		testPlayerEntity1.name = "player1";
		testPlayerEntity1.seat = SeatEntity.SEAT1;
		testPlayerEntity1.isBot = true;
		testPlayerEntity1.id = 1;
		Player testPlayer1 = new Player(testPlayerEntity1);
		//Arrange Weis1
		WeisTypeEntity weisTypeEntity11 = WeisTypeEntity.DREIBLATT;
		CardColorEntity cardColorEntity11 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity11 = CardValueEntity.ZEHN;		
		CardEntity cardEntity11 = new CardEntity();
		cardEntity11.color = cardColorEntity11;
		cardEntity11.value = cardValueEntity11;
		WeisEntity weisEntity11 = new WeisEntity();
		weisEntity11.type = weisTypeEntity11;
		weisEntity11.originCard = cardEntity11;	
		//Arrange Weis2
		WeisTypeEntity weisTypeEntity12 = WeisTypeEntity.FUENFBLATT;
		CardColorEntity cardColorEntity12 = CardColorEntity.SCHELLE; 
		CardValueEntity cardValueEntity12 = CardValueEntity.ACHT;		
		CardEntity cardEntity12 = new CardEntity();
		cardEntity12.color = cardColorEntity12;
		cardEntity12.value = cardValueEntity12;
		WeisEntity weisEntity12 = new WeisEntity();
		weisEntity12.type = weisTypeEntity12;
		weisEntity12.originCard = cardEntity11;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray1 = new WeisEntity[2]; 
		weisEntityArray1[0] = weisEntity11;
		weisEntityArray1[1] = weisEntity12;
		
		/*
		 * testPlayer2 
		 * 
		 * Dreiblatt
		 * Schelle
		 * Ass
		 * 
		 * Sechsblatt
		 * Eichel
		 * Ass
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity2 = new PlayerEntity();
		testPlayerEntity2.name = "player2";
		testPlayerEntity2.seat = SeatEntity.SEAT2;
		testPlayerEntity2.isBot = true;
		testPlayerEntity2.id = 2;
		Player testPlayer2 = new Player(testPlayerEntity2);
		//Arrange Weis1
		WeisTypeEntity weisTypeEntity21 = WeisTypeEntity.DREIBLATT;
		CardColorEntity cardColorEntity21 = CardColorEntity.SCHELLE; 
		CardValueEntity cardValueEntity21 = CardValueEntity.ASS;		
		CardEntity cardEntity21 = new CardEntity();
		cardEntity21.color = cardColorEntity21;
		cardEntity21.value = cardValueEntity21;
		WeisEntity weisEntity21 = new WeisEntity();
		weisEntity21.type = weisTypeEntity21;
		weisEntity21.originCard = cardEntity21;
		//Arrange Weis2
		WeisTypeEntity weisTypeEntity22 = WeisTypeEntity.SECHSBLATT;
		CardColorEntity cardColorEntity22 = CardColorEntity.EICHEL; 
		CardValueEntity cardValueEntity22 = CardValueEntity.ASS;		
		CardEntity cardEntity22 = new CardEntity();
		cardEntity22.color = cardColorEntity22;
		cardEntity22.value = cardValueEntity22;
		WeisEntity weisEntity22 = new WeisEntity();
		weisEntity22.type = weisTypeEntity22;
		weisEntity22.originCard = cardEntity22;
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray2 = new WeisEntity[2]; 
		weisEntityArray2[0] = weisEntity21;
		weisEntityArray2[1] = weisEntity22;
		
		/*
		 * testPlayer3 		-- Winning Player
		 * 
		 * Sechsblatt
		 * Rose				-- Trumpf
		 * Ass		
		 * 
		 * Dreiblatt
		 * Schilte
		 * Neun
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity3 = new PlayerEntity();
		testPlayerEntity3.name = "player3";
		testPlayerEntity3.seat = SeatEntity.SEAT3;
		testPlayerEntity3.isBot = true;
		testPlayerEntity3.id = 3;
		Player testPlayer3 = new Player(testPlayerEntity3);
		//Arrange Weis1
		WeisTypeEntity weisTypeEntity31 = WeisTypeEntity.SECHSBLATT;
		CardColorEntity cardColorEntity31 = CardColorEntity.ROSE; 
		CardValueEntity cardValueEntity31 = CardValueEntity.ASS;		
		CardEntity cardEntity31 = new CardEntity();
		cardEntity31.color = cardColorEntity31;
		cardEntity31.value = cardValueEntity31;
		WeisEntity weisEntity31 = new WeisEntity();
		weisEntity31.type = weisTypeEntity31;
		weisEntity31.originCard = cardEntity31;	
		//Arrange Weis1
		WeisTypeEntity weisTypeEntity32 = WeisTypeEntity.DREIBLATT;
		CardColorEntity cardColorEntity32 = CardColorEntity.SCHILTE; 
		CardValueEntity cardValueEntity32 = CardValueEntity.NEUN;		
		CardEntity cardEntity32 = new CardEntity();
		cardEntity32.color = cardColorEntity32;
		cardEntity32.value = cardValueEntity32;
		WeisEntity weisEntity32 = new WeisEntity();
		weisEntity32.type = weisTypeEntity32;
		weisEntity32.originCard = cardEntity32;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray3 = new WeisEntity[2]; 
		weisEntityArray3[0] = weisEntity31;
		weisEntityArray3[1] = weisEntity32;
		
		/*
		 * testPlayer4 
		 * 
		 * Dreiblatt
		 * Rose
		 * Acht
		 */		
		
		//Arrange Player
		PlayerEntity testPlayerEntity4 = new PlayerEntity();
		testPlayerEntity4.name = "player4";
		testPlayerEntity4.seat = SeatEntity.SEAT4;
		testPlayerEntity4.isBot = true;
		testPlayerEntity4.id = 4;
		Player testPlayer4 = new Player(testPlayerEntity4);
		//Arrange Weis
		WeisTypeEntity weisTypeEntity41 = WeisTypeEntity.DREIBLATT;
		CardColorEntity cardColorEntity41 = CardColorEntity.ROSE; 
		CardValueEntity cardValueEntity41 = CardValueEntity.ACHT;		
		CardEntity cardEntity41 = new CardEntity();
		cardEntity41.color = cardColorEntity41;
		cardEntity41.value = cardValueEntity41;
		WeisEntity weisEntity41 = new WeisEntity();
		weisEntity41.type = weisTypeEntity41;
		weisEntity41.originCard = cardEntity41;	
		//Arrange weisEntityArray
		WeisEntity[] weisEntityArray4 = new WeisEntity[1]; 
		weisEntityArray4[0] = weisEntity41;
		
		//Arrange testWeise
		LinkedHashMap<Player, WeisEntity[]> testWeise = new LinkedHashMap<>();
		testWeise.put(testPlayer1, weisEntityArray1);
		testWeise.put(testPlayer2, weisEntityArray2);
		testWeise.put(testPlayer3, weisEntityArray3);
		testWeise.put(testPlayer4, weisEntityArray4);
		//Arrange trump
		Trump trump = Trump.ROSE;
		//Arrange testee
		WeisToScoreBoardHandler testee = new WeisToScoreBoardHandler(testWeise, trump);
		
		
		//Act testee
		testee.execute();
		
		
		//Assert testee
		assertEquals(1, testee.getTeamId());
		assertEquals(290, testee.getWeisScore());
	}
}
