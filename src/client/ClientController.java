package client;

import shared.*;
import shared.client.*;

public class ClientController extends AbstractClient implements ViewObserver{
	//Fields
	private ClientModel model;
	private AbstractClientView view;
	
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
		view.changeView(ViewEnumeration.PLAYVIEW);
		view.updateView();
	}

	@Override
	public void updateScore(Score score) {
		model.updateScore(score);
		view.changeView(ViewEnumeration.PLAYVIEW);
		view.updateView();
	}

	@Override
	public void endRound() {
		model.setActiveSeatId(0);
		view.changeView(ViewEnumeration.PLAYVIEW);
		view.updateView();
	}

	@Override
	public void endGame(Team winner) {
		model.setActiveSeatId(0);
		model.setTrump(null);
		view.changeView(ViewEnumeration.GAMEOVERVIEW);
		view.updateView();
	}
	
	@Override
	protected void setSeat(int seatId) {
		model.setSeatId(seatId);
		view.changeView(ViewEnumeration.PLAYVIEW);
		view.updateView();
	}

	@Override
	protected void updateActiveSeat(int activeSeatId) {
		model.setActiveSeatId(activeSeatId);
		view.changeView(ViewEnumeration.PLAYVIEW);
		view.updateView();
	}

	@Override
	protected void updateDeck(int[] deckCardIds) {
		model.updateDeck(deckCardIds);
		view.changeView(ViewEnumeration.PLAYVIEW);
		view.updateView();
	}

	@Override
	protected void updateHand(int[] handCardIds) {
		model.updateHand(handCardIds);
		view.changeView(ViewEnumeration.PLAYVIEW);
		view.updateView();
	}
	
	@Override
	protected void requestTrump(boolean canSwitch) {
		model.setCanSwitch(canSwitch);
		view.changeView(ViewEnumeration.SELECTTRUMPVIEW);
		view.updateView();
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
