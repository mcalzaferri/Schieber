package client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

import client.shared.*;
import gui.Dialog.MessageType;
import gui.animation.Animation;
import gui.animation.AnimationRegion;
import shared.*;

public class ClientController extends AbstractClient implements ViewObserver {
	// Fields
	private AbstractClientView view;
	private long lastRefresh;
	private long refreshDelay;
	private boolean waitUserInteraction;

	// Constructor
	public ClientController(ClientCommunication com, ClientModel model, AbstractClientView view) {
		super(com, model);
		this.view = view;
		view.addObserver(this);
		view.changeView(ViewEnumeration.SELECTHOSTVIEW);
		lastRefresh = System.currentTimeMillis();
		refreshDelay = 0;
		waitUserInteraction = true;
	}

	// Methods
	/**
	 * if the guis don't want to do this i will. :)
	 * 
	 * @param v
	 */
	public void changeOrUpdateView(ViewEnumeration v) {
		if (view.getCurrentView() != v) {
			view.changeView(v);
		}
		
		view.updateView(v);	//Update anyway
	}

	public void waitRefresh() {
		while (lastRefresh > System.currentTimeMillis() - refreshDelay) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		lastRefresh = System.currentTimeMillis();
	}

	public void waitUserInteraction() {
		waitUserInteraction = true;
		while (waitUserInteraction) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitUserInteraction(long timeOutDelay) {
		waitUserInteraction = true;
		long start = System.currentTimeMillis();
		while (waitUserInteraction && System.currentTimeMillis() < start + timeOutDelay) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void teamsChanged(Collection<Team> teams) {
		view.updateView(ViewEnumeration.PLAYVIEW);
		view.updateView(ViewEnumeration.LOBBYVIEW);
	}

	// Inherited methods from AbstractClient
	/**
	 * Store broadcasted trump in the model and update view.
	 * 
	 * @param trump
	 *            Trump which was broadcasted
	 */
	@Override
	public void doSetTrump(Trump trump) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	/**
	 * Store broadcasted score in the model and update view.
	 * 
	 * @param score
	 *            New score which was broadcasted.
	 */
	@Override
	public void doUpdateScore(Score score) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	/**
	 * Round finished. Set active Seat to 0 (No seat active)
	 * 
	 */
	@Override
	public void doEndRound() {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);

	}

	/**
	 * Game finished. Set view to GameOver.
	 * 
	 * @param winner
	 *            Winnerteam
	 */
	@Override
	public void doEndGame(Team teamThatWon) {
		changeOrUpdateView(ViewEnumeration.GAMEOVERVIEW);
		view.publishMessage("This game is finally over. Took some time eh\n");
		waitUserInteraction();
	}

	/**
	 * Store seatId of this Client in the Model and update view. Usually this is
	 * called when the game is about to start.
	 * 
	 * @param seatId
	 *            SeatId which was broadcasted for this Client.
	 */
	@Override
	public void doSetSeat(int seatId) {
		// changeOrUpdateView(ViewEnumeration.PLAYVIEW);
		// TODO is this still necessary? /Maurus
	}

	/**
	 * Store activeSeatId in the model and update view. If the activeSeatId is equal
	 * to this client's seatId, this client needs to choose a card.
	 * 
	 * @param activeSeatId
	 *            Currently active seat which was broadcasted by the server.
	 */
	@Override
	public void doUpdateActiveSeat(int activeSeatId) {
		// changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	/**
	 * Store the cards on the deck in the model. The model is only updated, already
	 * existing objects are not recreated. This is usefull as no references need to
	 * be updated.
	 * 
	 * @param deckCardIds
	 *            The ids of the cards which represent the current deck.
	 */
	@Override
	public void doUpdateDeck(Card[] deckCards) {
		waitRefresh();
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	/**
	 * Store the cards on the hand in the model. The model is only updated, already
	 * existing objects are not recreated. This is usefull as no references need to
	 * be updated.
	 * 
	 * @param handCardIds
	 *            The ids of the cards which represent all cards on the hand of this
	 *            client.
	 */
	@Override
	public void doUpdateHand(Card[] handCards) {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
		waitRefresh();
	}

	/**
	 * This method is called when this Client needs to define the trump. The
	 * SelectTrumpView is called on the view.
	 * 
	 * @param canSwich
	 *            If true the Client has the opportunity to return the SCHIEBEN
	 *            GameMode
	 */
	@Override
	public void doRequestTrump(boolean canSwitch) {
		view.updateView(ViewEnumeration.PLAYVIEW);
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
	protected void doRequestCard(boolean selectWiis) {
		if (selectWiis)
			changeOrUpdateView(ViewEnumeration.WEISVIEW);
		else
			changeOrUpdateView(ViewEnumeration.PLAYVIEW);

	}

	@Override
	protected void doPlayerShowedWiis(Weis[] wiis, Player player) {
		StringBuilder sb = new StringBuilder();
		if (wiis == null || wiis.length == 0) {
			sb.append(player.getName() + " had no wiis to show!");
			if (Team.getTeamThatContainsPlayer(model.getTeams().values(), player).contains(model.getThisPlayer())) {
				if (player.equals(model.getThisPlayer())) {
					sb.append(" (As if you wouldn't know, right?)\n");
				} else {
					sb.append(" You should consider looking for a new partner\n");
				}
			} else {
				sb.append(" Sucks to be him\n");
			}
		} else {
			sb.append(player.getName());
			sb.append(" showed the following wiis:\n");
			for (Weis weis : wiis) {
				sb.append(weis.toString() + "\n");
			}
			if (player.equals(model.getThisPlayer())) {
				sb.append(" (As if you wouldn't know, right?)\n");
			} else {
				sb.append(" what a lucky guy\n");
			}
		}
		view.publishMessage(sb.toString());
	}

	@Override
	protected void doHandleBadResultException(BadResultException e) {
		view.showDialog("You had one job!\n" + e.getMessage(), MessageType.WARNING);
		System.err.println(e.getMessage());
	}

	@Override
	protected void gameStarted() {
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	@Override
	protected void trumpInfo(Trump trump) {
		Animation.resetDurationScale();
		if (trump != null) {
			if (trump != Trump.SCHIEBEN)
				view.publishMessage("New Trump is " + trump.toString()
						+ " (Just in case you didn't notice the huge picture above)\n");
			else
				view.publishMessage("Spieler hat geschoben. Anderer spieler waehlt nun den Trumpf");
		}
	}

	@Override
	protected void stichInfo(Player playerWhoWonStich) {
		// TODO Wait for user to click on screen
		if (playerWhoWonStich.equals(model.getThisPlayer())) {
			view.publishMessage("You won this stich! Click anywhere to continue");
		} else {
			view.publishMessage(playerWhoWonStich.getName() + " won this stich! Click anywhere to continue");
		}
		waitUserInteraction(2000);
		Animation.resetDurationScale();
	}

	@Override
	protected void playerChanged(Player player) {
		// If the player is you, do something special
		if (player.equals(model.getThisPlayer())
				|| (model.getThisPlayer().getId() == 0 && player.getName().equals(model.getThisPlayer().getName()))) {
			if (player.getSeat() == Seat.NOTATTABLE) {
				changeOrUpdateView(ViewEnumeration.LOBBYVIEW);
			}
		}
		view.updateView(view.getCurrentView());

	}

	@Override
	protected void playerJoinedTable(Player player) {
		if (player.equals(model.getThisPlayer())) {
			view.showDialog("You joined the table at seat: " + player.getSeat(), MessageType.INFORMATION);
			// Change to lobbyview if necessary, update otherwise
			changeOrUpdateView(ViewEnumeration.LOBBYVIEW);
		} else {
			view.updateView(ViewEnumeration.LOBBYVIEW);
		}
	}

	@Override
	protected void showHandOutCardAnimation(Player cardReceiver, ArrayList<Card> newHand) {
		if (cardReceiver.equals(model.getThisPlayer())) {
			int i = cardReceiver.getCards().size();
			for (Card newCard : newHand) {
				// Only show animation if the user did not fast forward
				if (!cardReceiver.getCards().contains(newCard.getId())) {
					view.showMoveCardAnimation(newCard, Animation.handOutCardDuration, AnimationRegion.DEALER, 0, 0,
							AnimationRegion.HAND, i, cardReceiver.getCards().size() + 3, null);
					i++;
				}
			}
		} else {
			for (int i = cardReceiver.getCards().size(); i < newHand.size(); i++) {
				view.showMoveCardAnimation(newHand.get(i), Animation.handOutCardDuration, AnimationRegion.DEALER, 0, 0,
						cardReceiver.getSeat().getRelativeSeat(model.getThisPlayer().getSeat()).getId(), i,
						cardReceiver.getCards().size() + 3, null);
			}
		}
		view.sleepAnimationFinished();
	}

	@Override
	protected void showLayCardAnimation(Player player, Card card, int cardPos) {

		if (model.getThisPlayer().equals(player)) {
			// From Hand to Bottom Deck
			view.showMoveCardAnimation(card, Animation.layCardDuration, AnimationRegion.HAND, cardPos,
					player.getCards().size(), // Source
					AnimationRegion.DECK, RelativeSeat.BOTTOM.getId(), 0, // Destination
					null);
		} else {
			RelativeSeat seat = player.getSeat().getRelativeSeat(model.getThisPlayer().getSeat());
			view.showMoveCardAnimation(card, Animation.layCardDuration, seat.getId(), cardPos, player.getCards().size(), // Source
					AnimationRegion.DECK, seat.getId(), 0, // Destination
					null);
		}

	}

	@Override
	protected void waitEndAnimation() {
		view.sleepAnimationFinished();
	}

	@Override
	protected void clearAnimation() {
		view.removeFinishedAnimations();
	}

	@Override
	protected void showCardsToStackAnimation(Player player, Card[] cards) {
		if (cards.length == 4) {
			RelativeSeat seat = player.getSeat().getRelativeSeat(model.getThisPlayer().getSeat());
			int cardsOnStack = player.getCardsOnStack().size();
			for (int i = 0; i < cards.length; i++) {
				view.showMoveCardAnimation(cards[i], Animation.cardToStackDuration, AnimationRegion.DECK,
						model.getDeckCardOrientation(cards[i]).getId(), 4, // Source
						seat.getId() + 10, cardsOnStack + i, cardsOnStack + 4, // Destination
						null);
			}
		}
	}

	// Inherited methods from ViewObserver
	/**
	 * The Player pressed the connect button on the gui.
	 * 
	 * @param serverAddress
	 *            Address where the player wants to connect to.
	 */
	@Override
	public void btnConnectClick(InetSocketAddress serverAddress, String username) {
		try {
			super.connect(serverAddress, username, false);
		} catch (BadResultException e) {
			String err = "Connect fehlgeschlagen mit Fehlermessage: " + e.getMessage() + ".\r\n Bitte Verbindung zum Server sowie korrekte Addresse überprüfen";
			System.err.println(err);
			view.showDialog(err, MessageType.ERROR);
			// TODO notify player about error
		}
	}

	/**
	 * On the GameOverView the player pressed the restart button as he wants to play
	 * another game.
	 * 
	 */
	@Override
	public void btnRestartClick() {
		waitUserInteraction = false;

	}

	/**
	 * The player pressed the disconnect button on the GameOverView or closed the
	 * gui.
	 * 
	 */
	@Override
	public void btnDisconnectClick() {
		super.disconnect();
		System.exit(0);
	}

	/**
	 * The player made a trump selection on the TrumpSelectView.
	 * 
	 * @param trump
	 *            Trump the player selected.
	 */
	@Override
	public void btnTrumpClick(Trump trump) {
		super.publishChosenTrump(trump);
	}

	/**
	 * The player wants to play this card.
	 * 
	 * @param card
	 *            Card the player wants to play.
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
		if(allowBroadcast) {
			super.publishChosenWiis(model.getPossibleWiis());
		}
		changeOrUpdateView(ViewEnumeration.PLAYVIEW);
	}

	@Override
	public void btnJoinTableClick(Seat preferedSeat) {
		// Check if seat is available
		for (Player p : model.getPlayers().values()) {
			if (p.getSeat() == preferedSeat) {
				if (model.getThisPlayer().getSeat() == Seat.NOTATTABLE) {
					view.showDialog("There is already someone on this seat. Please select another one!",
							MessageType.ERROR);
				} else if (model.getThisPlayer().getSeat() == preferedSeat) {
					view.showDialog("You are already sitting here!", MessageType.INFORMATION);
				} else {
					view.showDialog("There is already someone on this seat. But you are already on seat: "
							+ model.getThisPlayer().getSeat() + " anyways.", MessageType.WARNING);
				}

				return;
			}
		}
		super.joinTable(preferedSeat);

	}

	@Override
	public void btnChangeStateClick() {
		model.getThisPlayer().setReady(!model.getThisPlayer().isReady());
		super.publishChangedState(model.getThisPlayer().isReady());
	}

	// Getters and setters
	public void setRefreshDelay(long refreshDelay) {
		this.refreshDelay = refreshDelay;
	}

	public long getRefreshDelay() {
		return refreshDelay;
	}

	@Override
	public void playViewClick() {
		waitUserInteraction = false;
		if (view.hasAnimations()) {
			// Speed up Animations
			Animation.setDurationScale(3.0);
		}
	}

	@Override
	public void btnFillWithBots() {
		super.publishFillSeatsWithBots();
	}

}
