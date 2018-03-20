package gui;

import java.awt.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import client.*;

public abstract class AbstractView extends JFrame{
	protected ArrayList<ViewObserver> observers;	//Registered observers on the view
	protected ViewEnumeration viewType;
	
	public AbstractView(ViewEnumeration viewType, ArrayList<ViewObserver> observers) {
		super();
		this.viewType = viewType;
		this.observers = observers;
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);	//Prevents user from closing windows
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				for(ViewObserver o : observers) {
					o.btnCloseWindowClick(getViewType());
				}
			}
		});
	}
	
	/**Updates view
	 * 
	 */
	public abstract void update();
	/**Returns view type defined in a enumeration
	 * @return enumeration object representing the view
	 */
	public ViewEnumeration getViewType() {
		return this.viewType;
	}
	
}
