package client;

import gui.Dialog.MessageType;
import shared.client.ClientModel;

public abstract class AbstractClientView {
	protected ClientModel data;

	public AbstractClientView(ClientModel data) {
		this.data = data;
	}
	
	/**Adds observer to be notified
	 * @param observer Observer to be added
	 */
	public abstract void addObserver(ViewObserver observer);
	
	/**Changes active view
	 * All views will be closed and the selected view becomes visible. The main view 
	 * stays visible but becomes enabled.
	 * @param view View to be activated
	 */
	public abstract void changeView(ViewEnumeration view);
	/**Returns enum object of the currently active view
	 * @return active view enum
	 */
	public abstract ViewEnumeration getCurrentView();
	
	/**Updates the specified view of the gui
	 * While updating, the gui lets its subcomponents load the actual data
	 * and display it.
	 * 
	 * @param view view to be updated
	 */
	public abstract void updateView(ViewEnumeration view);	
	
	/**Updates all views of the gui
	 * While updating, the gui lets its subcomponents load the actual data
	 * and display it.
	 */
	public abstract void updateAll();
	
	/**This method is called whenever relevant information about the game
	 * must be displayed to the user
	 * @param text The text that shall be displayed.
	 */
	public abstract void publishMessage(String text);
	
	/**Shows the message as a dialog
	 * The dialog is implemented as a JOptionPane with a OK button.
	 * Unless the user confirms the dialog, the graphical components of
	 * the gui are disabled.
	 * 
	 * @param message Message to be displayed in the dialog
	 * @param type	Type of the message
	 */
	public abstract void showDialog(String message, MessageType type);
	
	/** Closes any open dialogs
	 * If there are no dialogs, none of them will be closed.
	 */
	public abstract void closeDialog();
}