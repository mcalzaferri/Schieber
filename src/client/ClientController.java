package client;

import shared.*;
import shared.client.*;

public class ClientController extends AbstractClient implements ViewObserver{
	//Fields
	ClientModel model;
	AbstractClientView view;
	
	//Constructor
	public ClientController(ClientModel model, AbstractClientView view) {
		this.model = model;
		this.view = view;
		view.addObserver(this);
	}
	
	//Methods
	
	//Inherited methods from AbstractClient
	@Override
	public void setTrumpf(Trumpf trumpf) {
		// TODO Auto-generated method stub
		model.setTrumpf(trumpf);
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
	
	//Inherited methods from ViewObserver

	@Override
	public void btnConnectClick(ServerAddress serverAddress) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void btnRestartClick() {
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

}
