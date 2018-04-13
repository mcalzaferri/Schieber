package gui.playingView;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;
import client.ViewObserver;
import shared.Card;
import shared.client.ClientModel;

/**
 * Holds the clients hand and shows all the cards in a comfortable layout.
 * 
 * @author mstieger
 *
 */
public class HandPane extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8159517916094656762L;
	private ClientModel data;
	private ArrayList<ViewObserver> observers;
	
	public HandPane(ClientModel data, ArrayList<ViewObserver> observers) {
		super();
		if(data == null) {
			throw new IllegalArgumentException("Data can not be null");
		}
		this.data = data;
		this.observers = observers;
		initializeComponents();
	}
	
	/**
	 * Initialize the class' graphics
	 */
	private void initializeComponents() {
		this.setLayout(new HandLayout());
		this.update();
	}
	
	/**
	 * Creates a graphical representation of the clients hand.
	 * If the hand is null, all the existing components are just
	 * being removed and repainted.
	 */
	public void update() {
		this.removeAll();
		if(data.getHand() != null) {
			for(Card c : data.getHand()) {
				ViewableCard vc = null;
				try {
					vc = new ViewableCard(c, observers);
				} catch (IOException e) {
					e.printStackTrace();
				}
				add(vc);
			}
		}		
		validate();	//Redo the layout
		repaint();
	}
}
