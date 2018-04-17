package client;

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
	
	/**This method is called whenever information must be displayed to the user
	 * @param text The text that shall be displayed.
	 */
	public abstract void publishMessage(String text);
}