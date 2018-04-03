package client;

import java.net.InetSocketAddress;

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
	}
	
	//Methods
	
	//Inherited methods from AbstractClient
	/** Store broadcasted trump in the model and update view.
	 * @param trump Trump which was broadcasted
	 */
	@Override
	public void doSetTrump(Trump trump) {
		view.changeView(ViewEnumeration.PLAYVIEW);
	}

	/** Store broadcasted score in the model and update view.
	 * @param score New score which was broadcasted.
	 */
	@Override
	public void doUpdateScore(Score score) {
		view.changeView(ViewEnumeration.PLAYVIEW);
	}

	/** Round finished. Set active Seat to 0 (No seat active)
	 * 
	 */
	@Override
	public void doEndRound() {
		view.changeView(ViewEnumeration.PLAYVIEW);
	}

	/** Game finished. Set view to GameOver.
	 *  @param winner Winnerteam
	 */
	@Override
	public void doEndGame() {
		view.changeView(ViewEnumeration.GAMEOVERVIEW);
	}
	
	/** Store seatId of this Client in the Model and update view.
	 *  Usually this is called when the game is about to start.
	 *  @param seatId SeatId which was broadcasted for this Client.
	 */
	@Override
	public void doSetSeat(int seatId) {
		view.changeView(ViewEnumeration.PLAYVIEW);
	}

	/** Store activeSeatId in the model and update view.
	 *  If the activeSeatId is equal to this client's seatId, this client needs to choose a card.
	 *  @param activeSeatId Currently active seat which was broadcasted by the server.
	 */
	@Override
	public void doUpdateActiveSeat(int activeSeatId) {
		view.changeView(ViewEnumeration.PLAYVIEW);
	}

	/** Store the cards on the deck in the model. The model is only updated, already existing objects are not recreated.
	 * This is usefull as no references need to be updated.
	 * @param deckCardIds The ids of the cards which represent the current deck.
	 */
	@Override
	public void doUpdateDeck(Card[] deckCards) {
		view.changeView(ViewEnumeration.PLAYVIEW);
	}

	/** Store the cards on the hand in the model. The model is only updated, already existing objects are not recreated.
	 * This is usefull as no references need to be updated.
	 * @param handCardIds The ids of the cards which represent all cards on the hand of this client.
	 */
	@Override
	public void doUpdateHand(Card[] handCards) {
		view.changeView(ViewEnumeration.PLAYVIEW);
	}
	
	/** This method is called when this Client needs to define the trump.
	 * The SelectTrumpView is called on the view.
	 * @param canSwich If true the Client has the opportunity to return the SCHIEBEN GameMode
	 */
	@Override
	public void doRequestTrump(boolean canSwitch) {
		view.changeView(ViewEnumeration.SELECTTRUMPVIEW);
	}

	@Override
	public void doConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doRequestWeis() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doRequestCard(boolean selectWiis) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void doPlayerShowedWiis(Weis[] wiis, Player player) {
		// TODO Auto-generated method stub
		
	}
	
	//Inherited methods from ViewObserver
	/** The Player pressed the connect button on the gui.
	 * @param serverAddress Address where the player wants to connect to.
	 */
	@Override
	public void btnConnectClick(InetSocketAddress serverAddress) {
		super.connect(serverAddress);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnWeisAllowed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void btnWeisDenied() {
		// TODO Auto-generated method stub
		
	}
}
