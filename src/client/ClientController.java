package client;

import shared.Card;
import shared.CardList;
import shared.Player;
import shared.Score;
import shared.Team;
import shared.Trumpf;
import shared.Weis;
import shared.client.*;

public class ClientController extends AbstractClient{
	//Fields
	ClientModel model;
	ClientViewInterface view;
	
	//Constructor
	public ClientController(ClientModel model, ClientViewInterface view) {
		this.model = model;
		this.view = view;
		addObserverToView();
	}
	
	//Methods
	private void addObserverToView() {
		this.view.addObserver(new ViewObserver() {

			@Override
			public void btnConnectClick() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnDisconnectClick() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnTrumpfClick(Trumpf trumpf) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void btnCardClick(Card card) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	//Inherited Methods
	@Override
	public void setTrumpf(Trumpf trumpf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveCardToDeck(Player source, Card card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDeck(CardList deck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateHand(CardList hand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publishWeis(Weis weis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publishStich(Player winner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScore(Score score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endGame(Team winner) {
		// TODO Auto-generated method stub
		
	}
}
