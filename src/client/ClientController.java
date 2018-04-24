package client;

import java.net.InetSocketAddress;

import gui.Gui.MessageType;
import shared.*;
import shared.client.*;

public class ClientController extends AbstractClient implements ViewObserver{
	//Fields
	private AbstractClientView view;
	
	//Constructor
	public ClientController(ClientCommunication com, ClientModel model, AbstractClientView view) {
		super(com, model);
		this.view = view;
		view.addObserver(this);
		view.changeView(ViewEnumeration.SELECTHOSTVIEW);
	}
	
	//Methods
	/** if the guis don't want to do this i will. :)
	 * @param v
	 */
	public void changeOrUpdateView(ViewEnumeration v) {
		if(view.getCurrentView() != v)
			view.changeView(v);
		else
			view.updateView(v);
	}
	
	//Inherited methods from AbstractClient
	/** Store broadcasted trump in the model and update view.
	 * @param trump Trump which was broadcasted
	 */
	@Override
	public void doSetTrump(Trump trump) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
		view.publishMessage("New Trump is " + trump.toString() + " (Just in case you didn't notice the huge picture above)/n");
	}

	/** Store broadcasted score in the model and update view.
	 * @param score New score which was broadcasted.
	 */
	@Override
	public void doUpdateScore(Score score) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	/** Round finished. Set active Seat to 0 (No seat active)
	 * 
	 */
	@Override
	public void doEndRound() {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	/** Game finished. Set view to GameOver.
	 *  @param winner Winnerteam
	 */
	@Override
	public void doEndGame() {
		changeOrUpdateView(ViewEnumeration.GAMEOVERVIEW);
		view.publishMessage("This game is finally over. Took some time eh/n");
	}
	
	/** Store seatId of this Client in the Model and update view.
	 *  Usually this is called when the game is about to start.
	 *  @param seatId SeatId which was broadcasted for this Client.
	 */
	@Override
	public void doSetSeat(int seatId) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
		//TODO is this still necessary? /Maurus
	}

	/** Store activeSeatId in the model and update view.
	 *  If the activeSeatId is equal to this client's seatId, this client needs to choose a card.
	 *  @param activeSeatId Currently active seat which was broadcasted by the server.
	 */
	@Override
	public void doUpdateActiveSeat(int activeSeatId) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	/** Store the cards on the deck in the model. The model is only updated, already existing objects are not recreated.
	 * This is usefull as no references need to be updated.
	 * @param deckCardIds The ids of the cards which represent the current deck.
	 */
	@Override
	public void doUpdateDeck(Card[] deckCards) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	/** Store the cards on the hand in the model. The model is only updated, already existing objects are not recreated.
	 * This is usefull as no references need to be updated.
	 * @param handCardIds The ids of the cards which represent all cards on the hand of this client.
	 */
	@Override
	public void doUpdateHand(Card[] handCards) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}
	
	/** This method is called when this Client needs to define the trump.
	 * The SelectTrumpView is called on the view.
	 * @param canSwich If true the Client has the opportunity to return the SCHIEBEN GameMode
	 */
	@Override
	public void doRequestTrump(boolean canSwitch) {
		changeOrUpdateView(ViewEnumeration.SELECTTRUMPVIEW);
	}

	@Override
	public void doConnected() {
		System.out.println("Successfully connected");
		
	}

	@Override
	public void doDisconnected() {
		System.out.println("Successfully disconnected");
		
	}

	@Override
	public void doRequestWeis() {
		view.changeView(ViewEnumeration.WEISVIEW);
	}

	@Override
	protected void doRequestCard(boolean selectWiis) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
		
	}
	
	@Override
	protected void doPlayerShowedWiis(Weis[] wiis, Player player) {
		StringBuilder sb = new StringBuilder();
		if(wiis == null || wiis.length == 0) {
			sb.append(player.getName() + " had no wiis to show!");
			if(Team.getTeamThatContainsPlayer(model.getTeams(), player).contains(model.getThisPlayer())) {
				if(player.equals(model.getThisPlayer())) {
					sb.append(" (As if you wouldn't know, right?)/n");
				}else {
					sb.append(" You should consider looking for a new partner/n");
				}
			}else {
				sb.append(" Sucks to be him/n");
			}	
		}else {
			sb.append(player.getName());
			sb.append(" showed the following wiis:/n");
			for(Weis weis : wiis) {
				sb.append(weis.toString() + "/n");
			}
			if(player.equals(model.getThisPlayer())) {
				sb.append(" (As if you wouldn't know, right?)/n");
			}else {
				sb.append(" what a lucky guy/n");
			}
		}
		view.publishMessage(sb.toString());
	}
	
	@Override
	protected void doHandleBadResultException(BadResultException e) {
		view.showDialog("You had one job!/n" + e.getMessage(), MessageType.WARNING);
		System.err.println(e.getMessage());
	}

	@Override
	protected void playerChanged(Player player) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}
	
	//Inherited methods from ViewObserver
	/** The Player pressed the connect button on the gui.
	 * @param serverAddress Address where the player wants to connect to.
	 */
	@Override
	public void btnConnectClick(InetSocketAddress serverAddress, String username) {
		try {
			super.connect(serverAddress, username, false);
		} catch (BadResultException e) {
			System.err.println("Connect fehlgeschlagen mit Fehlermessage: " + e.getMessage());
			//TODO notify player about error
		}
	}
	
	/** On the GameOverView the player pressed the restart button as he wants to play another game.
	 * 
	 */
	@Override
	public void btnRestartClick() {
		// TODO Auto-generated method stub
		
	}
	
	/** The player pressed the disconnect button on the GameOverView or closed the gui.
	 * 
	 */
	@Override
	public void btnDisconnectClick() {
		super.disconnect();
	}

	/** The player made a trump selection on the TrumpSelectView.
	 * @param trump Trump the player selected.
	 */
	@Override
	public void btnTrumpClick(Trump trump) {
		super.publishChosenTrump(trump);
	}

	/** The player wants to play this card.
	 * @param card Card the player wants to play.
	 */
	@Override
	public void btnCardClick(Card card) {
		super.publishChosenCard(card);
	}

	@Override
	public void btnCloseWindowClick(ViewEnumeration view) {
		super.disconnect();
		System.exit(0);
	}

	@Override
	public void btnWeisActionChosen(boolean allowBroadcast) {
		super.publishChosenWiis(model.getPossibleWiis());
	}

	@Override
	public void btnJoinTableClick(Seat preferedSeat) {
		super.joinTable(preferedSeat);
		
	}

	@Override
	public void btnChangeStateClick() {
		model.getThisPlayer().setReady(!model.getThisPlayer().isReady());
		super.publishChangedState(model.getThisPlayer().isReady());
	}
}
