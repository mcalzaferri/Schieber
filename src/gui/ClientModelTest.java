package gui;

import java.util.Hashtable;
import java.util.Map;

import ch.ntb.jass.common.entities.PlayerEntity;
import client.shared.ClientModel;
import shared.*;

import java.util.Random;

import shared.Card;
import shared.CardColor;
import shared.CardList;
import shared.CardValue;

public class ClientModelTest extends ClientModel{
	public ClientModelTest() {
		//Set players
		Player player1 = new Player(null, "Peter", Seat.SEAT2);
		Player player2 = new Player(null, "Hannah", Seat.NOTATTABLE); //TODO maybe this is partner? mcalzaferri
		Player player3 = new Player(null, "Luise", Seat.SEAT4);
		new PlayerEntity();
		Player actPlayer = new Player(null,"Leon",Seat.NOTATTABLE); //TODO maybe this is client? mcalzaferri
		
		Map<Integer,Player> players = new Hashtable<>();
		players.put(1, player1);
		players.put(2, player2);
		players.put(3, player3);
		
		this.setThisPlayer(actPlayer);
		this.setPlayers(players);
		
		//Set cards
		CardList cards = new CardList();
		for(int i = 0; i < 6; i++) {			
			cards.add(ClientModelTest.createRandomCard());
		}
		//this.setDeck(cards);
		cards = new CardList();
		for(int i = 0; i < 6; i++) {			
			cards.add(ClientModelTest.createRandomCard());
		}
		this.setHand(cards);
		
		new Random();

		Weis[] weis = new Weis[3];
		/*for(int i = 0; i < weis.length; i++) {	
			weis[i] = this.createRandomWeis();
		}*/
		
		Trump trump = Trump.UNEUFE;
		this.setTrump(trump);
		weis[0]= new Weis(WeisType.VIERGLEICHE, new Card(CardColor.SCHELLE,CardValue.NEUN));
		
		this.setTrump(trump);
		weis[1]= new Weis(WeisType.VIERBLATT, new Card(CardColor.EICHEL,CardValue.SIEBEN));
		
		this.setTrump(trump);
		weis[2]= new Weis(WeisType.NEUNBLATT, new Card(CardColor.SCHILTE,CardValue.SECHS));
		
		/*Trump trump = Trump.EICHEL;
		this.setTrump(trump);
		weis[0]= new Weis(WeisType.VIERGLEICHE, new Card(CardColor.SCHELLE,CardValue.OBER));

		this.setTrump(trump);
		weis[1]= new Weis(WeisType.ACHTBLATT, new Card(CardColor.EICHEL,CardValue.KOENIG));
		
		this.setTrump(trump);
		weis[2]= new Weis(WeisType.NEUNBLATT, new Card(CardColor.SCHILTE,CardValue.ASS));*/
		
		this.setPossibleWiis(weis);
		
		
	}
	
	public static Card createRandomCard() {
		Random r = new Random();
		return new Card(CardColor.getById(r.nextInt(4) + 1), CardValue.getById(r.nextInt(8) + 1));
	}
	
	public static  Weis createRandomWeis() {
		Random r = new Random();
		return new Weis(WeisType.getById(r.nextInt(11) + 1), new Card(CardColor.getById(r.nextInt(4) + 1), CardValue.getById(r.nextInt(8) + 1)));
	}
	
	public static Trump createRandomTrump() {
		Random r = new Random();
		return Trump.getById(r.nextInt(7)+1);
	}
}
