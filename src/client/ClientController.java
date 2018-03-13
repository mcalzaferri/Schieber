package client;

import shared.*;
import shared.client.*;

public class ClientController extends AbstractClient implements ViewObserver{
	//Fields
	ClientModel model;
	AbstractClientView view;
	
	//Constructor
	public ClientController(ClientCommunicationInterface com, ClientModel model, AbstractClientView view) {
		super(com);
		this.model = model;
		this.view = view;
		view.addObserver(this);
	}
	
	//Methods
	
	//Inherited methods from AbstractClient
	@Override
	public void setTrump(Trump trump) {
		model.setTrump(trump);
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
	
	@Override
	protected void setActivePlayer(Player activePlayer) {
		// TODO Auto-generated method stub
		
	}
	
	//Inherited methods from ViewObserver
	@Override
	public void btnConnectClick(ServerAddress serverAddress) {
		super.connect(serverAddress);
		
	}
	

	@Override
	public void btnRestartClick() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void btnDisconnectClick() {
		super.disconnect();
		
	}
	

	@Override
	public void btnTrumpClick(Trump trump) {
		super.publishChosenTrump(trump);
	}
	

	@Override
	public void btnCardClick(Card card) {
		super.publishChosenCard(card);
		
	}
}
