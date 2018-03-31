package gui.playingView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.Writer;
import java.util.ArrayList;

import client.ViewEnumeration;
import client.ViewObserver;
import gui.AbstractView;
import shared.client.ClientModel;

public class PlayingFieldView extends AbstractView{
	private HandPane hand;
	private BlackBoardPane blackBoard;
	private CarpetPane carpet;
	
	public PlayingFieldView(ViewEnumeration viewType, ArrayList<ViewObserver> observers, ClientModel data) {
		super(viewType, observers);
		this.hand = new HandPane(data.getHand(), observers);
		this.blackBoard = new BlackBoardPane(null, observers);
		this.carpet = new CarpetPane(data, observers);
		layoutView();
	}
	
	private void layoutView() {
		this.setMinimumSize(this.getBounds().getSize());
		this.setLayout(new PlayingFieldLayout());
		this.add(this.hand, PlayingFieldLayout.HAND);
		this.add(this.blackBoard, PlayingFieldLayout.BLACKBOARD);
		this.add(this.carpet, PlayingFieldLayout.CARPET);
		this.setVisible(true);
		this.pack();
	}

	@Override
	public void update() {
		this.hand.update();
		this.blackBoard.update();
		this.carpet.update();
		
	}


}
