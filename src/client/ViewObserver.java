package client;

import java.net.InetSocketAddress;

import shared.*;

public interface ViewObserver {
	public abstract void btnConnectClick(InetSocketAddress serverAddress, String username);
	public abstract void btnRestartClick();
	public abstract void btnDisconnectClick();
	public abstract void btnTrumpClick(Trump trump);
	public abstract void btnCardClick(Card card);
	public abstract void btnCloseWindowClick(ViewEnumeration view);
	/** The player has made his decision if he wants to publish his wiis or not.
	 * @param allowBroadcast = true if the wiis shall be published to all players (And therefore receive the points) or = false if 
	 * the player prefers to keep the weis for himself.
	 */
	public abstract void btnWeisActionChosen(boolean allowBroadcast);
	/** The player wants to join the table at the specified seat (He will be placed somewhere else if this is not possible)
	 * @param preferedSeat The seat the player wants to sit at (Possible number: 0 if random, 1 - 4 if specific seat)
	 */
	public abstract void btnJoinTableClick(Seat preferedSeat);
	
	/** The player is at the table and wants to change his ready state. 
	 * The state is simply inverted. (If he was ready he wont be anymore...) The current state of the player is visible in the ClientModel 
	 * (The isReady field of thisPlayer)
	 */
	public abstract void btnChangeStateClick();
	
	/** The player clicked the play view
	 * 
	 */
	public abstract void playViewClick();
	
	/** Fills the remaining seats with bots
	 * 
	 */
	public abstract void btnFillWithBots();
}
