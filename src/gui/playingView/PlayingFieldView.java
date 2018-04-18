package gui.playingView;

import java.util.ArrayList;
import javax.swing.JPanel;
import client.ViewEnumeration;
import client.ViewObserver;
import gui.ObservableView;
import gui.Viewable;
import shared.client.ClientModel;

/**
 * This class represents a playing field with a hand panel showing the
 * players hand, a black board panel showing the team's score and a carpet
 * panel showing the current deck.
 * 
 * @author mstieger
 *
 */
public class PlayingFieldView extends ObservableView implements Viewable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9149324107653292469L;
	private HandPane hand;
	private BlackBoardPane blackBoard;
	private CarpetPane carpet;
	
	public PlayingFieldView(ClientModel data, ArrayList<ViewObserver> observers) {
		super(data, observers);
		this.hand = new HandPane(data, observers);
		this.blackBoard = new BlackBoardPane(data, observers);
		this.carpet = new CarpetPane(data, observers);
		initializeComponents();
	}
	
	private void initializeComponents() {
		PlayingFieldLayout l = new PlayingFieldLayout();
		this.setLayout(l);

		this.add(this.hand, PlayingFieldLayout.HAND);
		this.add(this.blackBoard, PlayingFieldLayout.BLACKBOARD);
		this.add(this.carpet, PlayingFieldLayout.CARPET);
		this.setMinimumSize(l.minimumLayoutSize(this.getParent()));
		this.setDoubleBuffered(true);
		this.setVisible(true);
	}

	@Override
	public void update() {
		this.hand.update();
		this.blackBoard.repaint();
		this.carpet.repaint();	
		this.repaint();
	}

	@Override
	public JPanel getContent() {
		return this;
	}

	@Override
	public ViewEnumeration getType() {
		return ViewEnumeration.PLAYVIEW;
	}
}
